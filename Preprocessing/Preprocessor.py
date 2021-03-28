from abc import ABC, abstractmethod
from nltk.stem import WordNetLemmatizer 
from nltk.stem.snowball import SnowballStemmer
import logging, re
from nltk.corpus import stopwords
from pathlib import Path
import FileUtil
from TFIDFData import TFIDFData
import it_core_news_lg, en_core_web_trf
import Paths
from enum import Enum
from Dataset import Dataset
import pandas
log = logging.getLogger(__name__)
CODE_STOPWORD_FILEPATH = Path(__file__).parent / "resources/CodeStopWords.txt"
ITAL_CODE_STOPWORD_FILEPATH = Path(__file__).parent / "resources/ItalianCodeStopWords.txt"

class PreprocessingStep(ABC):
    
    @abstractmethod
    def execute(self, token_list: [str]):
        pass
    
    
class Preprocessor(ABC):
    """
    The preprocessor consists of a pipeline of preprocessing steps
    The preprocessing steps are handed over in the constructor
    
    By calling run_preprocession, a given list of strings passed through 
    the preprocessing steps in order of appearance
    """
    
    def __init__(self, preprocessing_steps: [PreprocessingStep]=[]):
        self._preprocessing_steps = preprocessing_steps
        self.javadoc = False
        
    def run_preprocessing(self, tokens: [str], file_name) -> [str]:
        for step in self._preprocessing_steps:
            tokens = step.execute(tokens, file_name, self.javadoc)
        self.javadoc = False
        return tokens
    
class Preprocessable(ABC):
    """Classes who represent text or code data that can be preprocessed
       Returns True, if the preprocessed object became empty after preprocessing
    """
    @abstractmethod
    def preprocess(self, preprocessor) -> bool:
        pass
    
class CamelCaseSplitter(PreprocessingStep):
    """
    Join = True: joins the splitted subwords together with whitespaces to a single string
    Join = False: returns the splitted subwords as independent strings
    """
    
    def __init__(self, join=False):
        self._join = join
    def execute(self, text_tokens, file_name, javadoc):
        result_list = []
        for token in text_tokens:
            if self._join:
                result_list.append(" ".join(re.sub('([A-Z][a-z]+)', r' \1', re.sub('([A-Z]+)', r' \1', token)).split()))
            else:
                result_list.extend(re.sub('([A-Z][a-z]+)', r' \1', re.sub('([A-Z]+)', r' \1', token)).split())
        return result_list
class Lemmatizer(PreprocessingStep):
    COLUMN_LEMMA = "lemma"
    
    class LemmatizerType(Enum):
        english_nltk = 1
        english_spacy = 2
        italian_nltk = 3 # is a stemmer, nltk does not have an italian lemmatizer
        italian_spacy = 4
        
    def __init__(self, lemmatizer_type=LemmatizerType.english_nltk):
        self._lemmatizer_type = lemmatizer_type
        self._lemmatizer = None
        if lemmatizer_type == self.LemmatizerType.english_nltk:
            self._lemmatizer = WordNetLemmatizer()
        elif lemmatizer_type == self.LemmatizerType.english_spacy:
            # Use precalculated files for spacy since google colab can't handle fasttext model and spacy lemmatizer at once
            self._lemmatizer = FileUtil.read_csv_to_dataframe(Paths.PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV)
        elif lemmatizer_type == self.LemmatizerType.italian_nltk:
            self._lemmatizer = SnowballStemmer("italian")
        elif lemmatizer_type == self.LemmatizerType.italian_spacy:
            # Use precalculated files for spacy since google colab can't handle fasttext model and spacy lemmatizer at once
            self._lemmatizer = FileUtil.read_csv_to_dataframe(Paths.PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV)
        else:
            log.error(f"Unknown case for LemmatizerType: {lemmatizer_type}")
        
    def execute(self, text_tokens, file_name, javadoc):
        if self._lemmatizer_type == self.LemmatizerType.english_nltk:
            return [self._lemmatizer.lemmatize(token) for token in text_tokens]
        elif self._lemmatizer_type == self.LemmatizerType.english_spacy or self._lemmatizer_type == self.LemmatizerType.italian_spacy:
            return [self._lemmatizer.at[token, self.COLUMN_LEMMA] if token in self._lemmatizer.index else token for token in text_tokens]
        if self._lemmatizer_type == self.LemmatizerType.italian_nltk:
            return [self._lemmatizer.stem(token) for token in text_tokens]
            
    @classmethod
    def _precalculate_spacy_lemmatizer(cls, spacy_lemmatizer, dataset_tuple, output_path):
        word_to_lemma_map = {}
        
        def iterate_files(tokenizer, preprecessor, folder):
            for file in FileUtil.get_files_in_directory(folder, True):
                file_representation = tokenizer.tokenize(file)
                file_representation.preprocess(preprecessor)
                for word in file_representation.token_list:
                    lemma = [token.lemma_ for token in spacy_lemmatizer(word)]
                    if len(lemma) > 1:
                        log.info(f"More than one lemma {lemma} for \"{word}\". Using \"{''.join(lemma)}\" as lemma")
                    lemma = "".join(lemma)
                    if word in word_to_lemma_map:
                        if not word_to_lemma_map[word] == lemma:
                            log.info(f"Different Duplicate Lemma for {word}: {word_to_lemma_dataframe[word]} <-> {lemma}")
                    else:
                        word_to_lemma_map[word] = lemma
        for dataset, code_pre, code_tok, req_pre, req_tok in dataset_tuple:
            
            iterate_files(req_tok, req_pre, dataset.req_folder())
            iterate_files(code_tok, code_pre, dataset.code_folder())
        
        word_to_lemma_dataframe = pandas.DataFrame.from_dict(word_to_lemma_map, orient="index", columns=[cls.COLUMN_LEMMA])
        FileUtil.write_dataframe_to_csv(word_to_lemma_dataframe, output_path)

    @classmethod
    def precalculate_spacy_english_lemmatizer(cls, dataset_tuple):
        cls._precalculate_spacy_lemmatizer(en_core_web_trf.load(disable=['ner', 'parser']), dataset_tuple, Paths.PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV) # we only need the lemmatizer component, disable the other
        
    @classmethod
    def precalculate_spacy_italian_lemmatizer(cls, dataset_tuple):
        cls._precalculate_spacy_lemmatizer(it_core_news_lg.load(disable=['ner', 'parser']), dataset_tuple, Paths.PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV)
        

"""
req_tokenizer = WordTokenizer(EANCI(), True)
req_pre = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER])
code_tokenizer = JavaCodeASTTokenizer(EANCI(), WordTokenizer(EANCI(), True))
code_pre = Preprocessor([URL, SEP, LETTER, CAMEL, JAVASTOP, LOWER])
dataset_tuple = [(EANCI(), code_pre, code_tokenizer, req_pre, req_tokenizer)]

"""
class LowerCaseTransformer(PreprocessingStep):
    
    def execute(self, text_tokens: [str], file_name, javadoc):
        return [token.lower() for token in text_tokens]

class Separator(PreprocessingStep):
    """Separates tokens that with slashes and points
       [high/higher] -> [high, higher]
       [package.name] -> [package, name]
    Join = True: joins the splitted subwords together with whitespaces to a single string
    Join = False: returns the splitted subwords as independent strings
    """
    
    def __init__(self, join=False):
        self._join = join
    def execute(self, text_tokens: [str], file_name, javadoc):
        """
        if javadoc:
            result_sentences = []
            for sentence_word_list in text_tokens:
                result_words = []
                for word in sentence_word_list:
                    result_words += word.split("/")
                result_sentences += [result_words]
            
            result_sentences_2 = []
            for sentence_word_list in result_sentences:
                result_words = []
                for word in sentence_word_list:
                    result_words += word.split(".")
                result_sentences_2 += [result_words]
            
            result_sentences_3 = []
            for sentence_word_list in result_sentences_2:
                result_words = []
                for word in sentence_word_list:
                    result_words += word.split("_")
                result_sentences_3 += [result_words]
            return result_sentences_3
        """
        result = []
        for token in text_tokens:
            if self._join:
                result += [" ".join(token.split("/"))]
            else:
                result += token.split("/")
        result2 = []
        for token in result:
            if self._join:
                result2 += [" ".join(token.split("."))]
            else:
                result2 += token.split(".")
        result3 = []
        for token in result2:
            if self._join:
                result3 += [" ".join(token.split("_"))]
            else:
                result3 += token.split("_")
        return result3

class StopWordRemover(PreprocessingStep):
    
    def __init__(self, italian=False):
        self._stop_words = None
        if italian:
            self._stop_words = set(stopwords.words('italian'))
        else:
            self._stop_words = set(stopwords.words('english'))
        
    def execute(self, text_tokens, file_name, javadoc):
        return [token for token in text_tokens if token not in self._stop_words]
    
class JavaCodeStopWordRemover(StopWordRemover):
    def __init__(self, ital=False):
        
        if ital:
            stopwords_as_string = FileUtil.read_textfile_into_string(ITAL_CODE_STOPWORD_FILEPATH)
        else:
            stopwords_as_string = FileUtil.read_textfile_into_string(CODE_STOPWORD_FILEPATH)
        self._stop_words = stopwords_as_string.split("\n")
        
        
class TFIDFStopWordRemover(PreprocessingStep):
    
    def __init__(self, threshold, tfidf_weights_file):
        self._threshold = threshold
        self._tfidf_weights_data = TFIDFData(tfidf_weights_file)
        
    def execute(self, text_tokens, file_name, javadoc):
        
        result_tokens = []
        for token in text_tokens:
            tfidf_weight = None
            try:
                tfidf_weight = self._tfidf_weights_data.get_weight(file_name, token)
            except KeyError:
                log.debug("SKIPPED: word \"{}\" of {} not found in {}".format(token, file_name, self._tfidf_weights_data._file_path))
            if tfidf_weight is not None and tfidf_weight > self._threshold:
                result_tokens.append(token)
        return result_tokens
    
class TokenFilter(PreprocessingStep):
    """
    This preprocessing step filters the tokens with the given filter_function
    
    filter_function: String -> Bool | Takes a token as string and maps it to an boolean value
    
    All tokens that fulfills the function (returns true) are retained
    
    """
    def __init__(self, filter_function):
        self._filter_function = filter_function
        
    def execute(self, text_tokens, file_name, javadoc):
        return [token for token in text_tokens if self._filter_function(token)]

class UrlRemover(PreprocessingStep):
    """
    Removes urls
    """
    def execute(self, token_list, file_name, javadoc):
        result = []
        """
        if javadoc:
            token_list = [" ".join(word_list) for word_list in token_list]
        """
        result = [re.compile('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\), ]|(?:%[0-9a-fA-F][0-9a-fA-F]))+').sub("", token) for token in token_list]
        """
        if javadoc:
            result = [sentence_string.split() for sentence_string in result]
        """
        return result
              
class NonLetterFilter(PreprocessingStep):
    """
    Remove characters except A-Z, a-z, whitespace and -
    """
   
    def execute(self, token_list:[str], file_name, javadoc):
        return [re.compile('[^a-zA-Z- ]').sub("", token) for token in token_list]

class JavaDocFilter(PreprocessingStep):
    def __init__(self, word_and_sentence=False):
        """
        _word_and_sentence = true if token_list of execute is a nested list: list of sentences, which are list of words
        """
        self._word_and_sentence = word_and_sentence
        
    """
    Ignore complete tokens that contain @throws, @author or @version
    Remove @param, @link and @return
    """
    def execute(self, token_list:[str], file_name, javadoc):
        if not javadoc:
            return token_list
        processing_list = token_list
        if self._word_and_sentence:
            processing_list = [" ".join(word_list) for word_list in token_list]
        result = []
        for token in processing_list:
            token2 = token.lower() #Dont lowercase the original token
            if "@throws" in token2 or "@author" in token2 or "@version" in token2:
                continue 
            token = re.sub("@param", "", token, flags=re.RegexFlag.IGNORECASE)
            token = re.sub("@return", "", token, flags=re.RegexFlag.IGNORECASE)
            token = re.sub("@link", "", token, flags=re.RegexFlag.IGNORECASE)
            if token:
                result += token.split() # Remove white spaces to add single words
        return result
    
class DuplicateWhiteSpaceFilter(PreprocessingStep):
    """
    Replace consecutive white spaces with one single white space
    """
    def execute(self, token_list:[str], file_name, javadoc):
        return [" ".join(token.split()) for token in token_list]
    
class AddFullStop(PreprocessingStep):
    """
    Add full stop if necessary
    """
    def execute(self, token_list:[str], file_name, javadoc):
        result = []
        for token in token_list:
            if token and token[-1] not in [".", "!", ";", "?"] :
                result.append(token + ".")
            else:
                result.append(token)
        return result

class RemoveLastPunctuatioMark(PreprocessingStep):
    """
    Remove punctuation marks at the end if necessary
    """
    def execute(self, token_list:[str], file_name, javadoc):
        result = []
        for token in token_list:
            if token and token[-1] in [".", "!", ";", "?"]:
                without_punct = token[:-1]
                result.append(without_punct)
            else:
                result.append(token)
        return result
        
class WordLengthFilter(PreprocessingStep):
    """
    Discard word if len(word) <= length_to_discard
    """
    def __init__(self, length_to_discard: int):
        self._length_to_discard = length_to_discard
    
    def execute(self, token_list:[str], file_name, javadoc):
        return [token for token in token_list if len(token) > self._length_to_discard]