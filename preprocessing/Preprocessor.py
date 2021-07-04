from abc import ABC, abstractmethod
from enum import Enum
import logging, re

from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer 
from nltk.stem.snowball import SnowballStemmer
from pathlib import Path
import it_core_news_lg, en_core_web_trf
import pandas

from preprocessing.JavaCodeASTTokenizer import JavaCodeASTTokenizer
from preprocessing.Tokenizer import WordTokenizer
from utility import FileUtil
from utility import PandasUtil

log = logging.getLogger(__name__)

RESOURCES_FOLDER = Path(__file__).parent / "resources"
CODE_STOPWORD_FILEPATH = RESOURCES_FOLDER / "CodeStopWords.txt"
ITAL_CODE_STOPWORD_FILEPATH = RESOURCES_FOLDER / "ItalianCodeStopWords.txt"
PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV = RESOURCES_FOLDER / "precalculated_spacy_italian_lemmas.csv"
PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV = RESOURCES_FOLDER / "precalculated_spacy_english_lemmas.csv"


class PreprocessingStep(ABC):
    
    @abstractmethod
    def execute(self, token_list: [str]):
        pass
    
    
class Preprocessor(ABC):
    """
    The preprocessor consists of a pipeline with preprocessing steps
    The preprocessing steps are handed over in the constructor
    
    By calling run_preprocession, a given list of strings passed through 
    the preprocessing steps in order of appearance
    """
    
    def __init__(self, preprocessing_steps: [PreprocessingStep]=[]):
        self._preprocessing_steps = preprocessing_steps
        
    def run_preprocessing(self, tokens: [str]) -> [str]:
        for step in self._preprocessing_steps:
            tokens = step.execute(tokens)
        return tokens


class CamelCaseSplitter(PreprocessingStep):
    """
    Join = True: joins the splitted subwords together with whitespaces to a single string
    Join = False: returns the splitted subwords as independent strings
    """
    
    def __init__(self, join=False):
        self._join = join

    def execute(self, text_tokens):
        result_list = []
        for token in text_tokens:
            if self._join:
                result_list.append(" ".join(re.sub('([A-Z][a-z]+)', r' \1', re.sub('([A-Z]+)', r' \1', token)).split()))
            else:
                result_list.extend(re.sub('([A-Z][a-z]+)', r' \1', re.sub('([A-Z]+)', r' \1', token)).split())
        return result_list


class Lemmatizer(PreprocessingStep):
    COLUMN_LEMMA = "lemma"
    """
    spacy lemma file precalculation example:
    
    Lemmatizer().precalculate_spacy_english_lemmatizer([Etour(), Itrust()])
    
    """

    class LemmatizerType(Enum):
        english_nltk = 1
        english_spacy = 2
        italian_nltk = 3  # is a stemmer, nltk does not have an italian lemmatizer
        italian_spacy = 4
        
    def __init__(self, lemmatizer_type=LemmatizerType.english_nltk):
        self._lemmatizer_type = lemmatizer_type
        self._lemmatizer = None
        if lemmatizer_type == self.LemmatizerType.english_nltk:
            self._lemmatizer = WordNetLemmatizer()
        elif lemmatizer_type == self.LemmatizerType.english_spacy:
            # Use precalculated files for spacy since free google colab can't handle fasttext model and spacy lemmatizer at once
            if not FileUtil.file_exists(PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV):
                log.error(f"{PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV} does not exists. The spacy lemmatizer needs an precalculated lemma file.")
            self._lemmatizer = PandasUtil.read_csv_to_dataframe(PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV)
        elif lemmatizer_type == self.LemmatizerType.italian_nltk:
            self._lemmatizer = SnowballStemmer("italian")
        elif lemmatizer_type == self.LemmatizerType.italian_spacy:
            # Use precalculated files for spacy since free google colab can't handle fasttext model and spacy lemmatizer at once
            if not FileUtil.file_exists(PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV):
                log.error(f"{PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV} does not exists. The spacy lemmatizer needs an precalculated lemma file.")
            self._lemmatizer = PandasUtil.read_csv_to_dataframe(PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV)
        else:
            log.error(f"Unknown case for LemmatizerType: {lemmatizer_type}")
        
    def execute(self, text_tokens):
        if self._lemmatizer_type == self.LemmatizerType.english_nltk:
            return [self._lemmatizer.lemmatize(token) for token in text_tokens]
        elif self._lemmatizer_type == self.LemmatizerType.english_spacy or self._lemmatizer_type == self.LemmatizerType.italian_spacy:
            return [self._lemmatizer.at[token, self.COLUMN_LEMMA] if token in self._lemmatizer.index else token for token in text_tokens]
        if self._lemmatizer_type == self.LemmatizerType.italian_nltk:
            return [self._lemmatizer.stem(token) for token in text_tokens]
            
    @classmethod
    def _precalculate_spacy_lemmatizer(cls, spacy_lemmatizer, datasets, output_path):
        dataset_tuples = []
        for dataset in datasets:
            req_tokenizer = WordTokenizer(dataset, not dataset.is_english())
            req_pre = Preprocessor([UrlRemover(), Separator(), NonLetterFilter(), CamelCaseSplitter(), LowerCaseTransformer()])
            code_tokenizer = JavaCodeASTTokenizer(dataset, WordTokenizer(dataset, not dataset.is_english()))
            code_pre = Preprocessor([UrlRemover(), Separator(), NonLetterFilter(), CamelCaseSplitter(), JavaCodeStopWordRemover(not dataset.is_english()), LowerCaseTransformer()])
            dataset_tuples.append((dataset, code_pre, code_tokenizer, req_pre, req_tokenizer))
        
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

        for dataset, code_pre, code_tok, req_pre, req_tok in dataset_tuples:
            
            iterate_files(req_tok, req_pre, dataset.req_folder())
            iterate_files(code_tok, code_pre, dataset.code_folder())
        
        word_to_lemma_dataframe = pandas.DataFrame.from_dict(word_to_lemma_map, orient="index", columns=[cls.COLUMN_LEMMA])
        PandasUtil.write_dataframe_to_csv(word_to_lemma_dataframe, output_path)

    @classmethod
    def precalculate_spacy_english_lemmatizer(cls, datasets):
        cls._precalculate_spacy_lemmatizer(en_core_web_trf.load(disable=['ner', 'parser']), datasets, PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV)  # we only need the lemmatizer component, disable the other
        
    @classmethod
    def precalculate_spacy_italian_lemmatizer(cls, datasets):
        cls._precalculate_spacy_lemmatizer(it_core_news_lg.load(disable=['ner', 'parser']), datasets, PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV)


class LowerCaseTransformer(PreprocessingStep):
    
    def execute(self, text_tokens: [str]):
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

    def execute(self, text_tokens: [str]):
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
        
    def execute(self, text_tokens):
        return [token for token in text_tokens if token not in self._stop_words]

    
class JavaCodeStopWordRemover(StopWordRemover):

    def __init__(self, ital=False):
        
        if ital:
            stopwords_as_string = FileUtil.read_textfile_into_string(ITAL_CODE_STOPWORD_FILEPATH)
        else:
            stopwords_as_string = FileUtil.read_textfile_into_string(CODE_STOPWORD_FILEPATH)
        self._stop_words = stopwords_as_string.split("\n")

    
class UrlRemover(PreprocessingStep):
    """
    Removes urls
    """

    def execute(self, token_list):
        return [re.compile('http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\), ]|(?:%[0-9a-fA-F][0-9a-fA-F]))+').sub("", token) for token in token_list]

              
class NonLetterFilter(PreprocessingStep):
    """
    Remove characters except A-Z, a-z, whitespace and -
    """
   
    def execute(self, token_list:[str]):
        return [re.compile('[^a-zA-Z- ]').sub("", token) for token in token_list]

    
class WordLengthFilter(PreprocessingStep):
    """
    Discard word if len(word) <= length_to_discard
    """

    def __init__(self, length_to_discard: int):
        self._length_to_discard = length_to_discard
    
    def execute(self, token_list:[str]):
        return [token for token in token_list if len(token) > self._length_to_discard]
