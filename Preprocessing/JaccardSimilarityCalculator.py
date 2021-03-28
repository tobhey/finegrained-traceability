import Paths, abc
from Preprocessing.FileRepresentation import UseCaseFileRepresentation
from Preprocessing.CodeFileRepresentation import CodeFileRepresentation
import pandas
from Dataset import Etour308
from Preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer
from Preprocessing.Tokenizer import UCTokenizer
from Preprocessing.Preprocessor import *


def calculate_jaccard(dataset, req_word_chooser, code_word_chooser, output_file=None, output_suffix=""):
    if not output_file:
        output_file = Paths.precalculated_jaccard_sims_csv_filename(dataset, type(req_word_chooser).__name__, type(code_word_chooser).__name__, output_suffix)
    req_dict = {} # req_dict[Filename] = [relevant, words, of, file]
    code_dict = {} # code_dict[Filename] = [relevant, words, of, file]
    
    for file in FileUtil.get_files_in_directory(dataset.req_folder()):
        req_dict[FileUtil.get_filename_from_path(file)] = req_word_chooser.get_words(file)
        
    for file in FileUtil.get_files_in_directory(dataset.code_folder()):
        code_dict[FileUtil.get_filename_from_path(file)] = code_word_chooser.get_words(file)
        
    df = pandas.DataFrame(None, index=req_dict.keys(), columns=code_dict.keys())
    for req_name in req_dict:
        for code_name in code_dict:
            df.at[req_name, code_name] = _calculate_jaccard_similarity(req_dict[req_name], code_dict[code_name])
            
    FileUtil.write_dataframe_to_csv(df, output_file)
            
def _calculate_jaccard_similarity(word_list_1, word_list_2):
    assert len(word_list_1) >= 0 and len(word_list_2) >= 0, "Can't calculate jaccard with negative length word lists"
    if len(word_list_1) == 0 and len(word_list_2) == 0:
        return 0
    word_list_1 = set(word_list_1)
    word_list_2 = set(word_list_2)
    intersection_length = len(word_list_1.intersection(word_list_2))
    return intersection_length / (len(word_list_1) +  len(word_list_2) - intersection_length) # Dont count duplicates twice
        
class WordChooser(abc.ABC):
    
    def __init__(self, tokenizer, preprocessor):
        self._tokenizer = tokenizer
        self._preprocessor = preprocessor
        
    def get_words(self, file):
        file_representation = self._tokenizer.tokenize(file)
        file_representation.preprocess(self._preprocessor)
        return self._choose_words_from_file_representation(file_representation)
    
    @abc.abstractmethod
    def _choose_words_from_file_representation(self, file_representation):
        pass
        
class UCNameDescFlowChooser(WordChooser):
    def _choose_words_from_file_representation(self, file_representation):
        assert isinstance(file_representation, UseCaseFileRepresentation), "Use a tokenizer that returns a UseCaseFileRepresentation"
        return file_representation.name_words + file_representation.description_words + file_representation.flow_of_events_words
    
class UCNameDescChooser(WordChooser):
    def _choose_words_from_file_representation(self, file_representation):
        assert isinstance(file_representation, UseCaseFileRepresentation), "Use a tokenizer that returns a UseCaseFileRepresentation"
        return file_representation.name_words + file_representation.description_words 
    
class UCNameChooser(WordChooser):
    def _choose_words_from_file_representation(self, file_representation):
        assert isinstance(file_representation, UseCaseFileRepresentation), "Use a tokenizer that returns a UseCaseFileRepresentation"
        return file_representation.name_words 
    
class CodeClassNameMethodSignatureChooser(WordChooser):
    def _choose_words_from_file_representation(self, file_representation):
        assert isinstance(file_representation, CodeFileRepresentation), "Use a tokenizer that returns a CodeFileRepresentation"
        chosen_words = []
        for cls in file_representation.classifiers:
            chosen_words += cls.get_name_words()
            for method in cls.methods:
                chosen_words += method.get_name_words() + method.get_param_plain_list() + method.get_returntype_words()
        return chosen_words
    
    
class RodriguezCodeChooser(WordChooser):
    """
    Chooses class names, method names, param names, left side identifiers in method body
    """
    def _choose_words_from_file_representation(self, file_representation):
        assert isinstance(file_representation, CodeFileRepresentation), "Use a tokenizer that returns a CodeFileRepresentation"
        chosen_words = []
        for cls in file_representation.classifiers:
            chosen_words += cls.get_name_words()
            for method in cls.methods:
                chosen_words += method.get_name_words()
                for param_tuple in method.get_param_tuples():
                    if param_tuple and param_tuple[1]:
                        chosen_words += param_tuple[1] # Only add the parameter names, not the types
                chosen_words += method.get_left_side_identifier_words()
        return chosen_words
    
"""    
CAMEL = CamelCaseSplitter()
LOWER = LowerCaseTransformer()
LETTER = NonLetterFilter()
URL = UrlRemover()
SEP = Separator()
JAVASTOP = JavaCodeStopWordRemover()
STOP_IT = StopWordRemover(True) # Italian stop word remover
STOP = StopWordRemover()
LEMMA_IT = Lemmatizer(Lemmatizer.LemmatizerType.italian_spacy) # Italian lemmatizer
LEMMA = Lemmatizer()
W_LENGTH = WordLengthFilter(2)

CODE_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, JAVASTOP, LEMMA, STOP, W_LENGTH])
REQ_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, STOP, W_LENGTH])
"""
#calculate_jaccard(Etour308(), UCNameDescFlowChooser(UCTokenizer(Etour308()), REQ_PREPROCESSOR), RodriguezCodeChooser(JavaCodeASTTokenizer(), CODE_PREPROCESSOR))