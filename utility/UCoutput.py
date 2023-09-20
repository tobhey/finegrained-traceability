from datasets.Dataset import Dataset, Libest, Albergate
from embeddingCreator.CodeEmbeddingCreator import MockCodeEmbeddingCreator
from embeddingCreator.RequirementEmbeddingCreator import MockUCEmbeddingCreator
from embeddingCreator.WordChooser import MethodSignatureChooser, SentenceChooser, \
    ClassnameWordChooser, MethodCommentSignatureChooser, \
    UCNameDescFlowWordChooser, UCAllWordChooser
from embeddingCreator.WordEmbeddingCreator import FastTextEmbeddingCreator, RandomWordEmbeddingCreator
from evaluation.OutputService import F1ExcelOutputService, MAPOutputService
from precalculating.TraceLinkDataStructure import ElementLevelTraceLinkDataStructure
from precalculating.TraceLinkDataStructureFactory import ElementLevelTraceLinkDataStructureFactory
from preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer
from preprocessing.FileRepresentation import UseCaseFileRepresentation, TextFileGroupedRepresentation
from preprocessing.Preprocessor import CamelCaseSplitter, LowerCaseTransformer, \
    NonLetterFilter, UrlRemover, Separator, JavaCodeStopWordRemover, \
    StopWordRemover, Lemmatizer, WordLengthFilter, Preprocessor
from preprocessing.Tokenizer import JavaDocDescriptionOnlyTokenizer, \
    WordAndSentenceTokenizer, UCTokenizer, NameAndDescriptionTokenizer
from traceLinkProcessing.ElementFilter import ElementFilter
from traceLinkProcessing.NeighborHandler import NeighborStrategy
from traceLinkProcessing.SimilarityFilter import SimilarityFilter
from traceLinkProcessing.TraceLinkCreator import CallGraphTraceLinkAggregator
from traceLinkProcessing.TraceLinkProcessor import MajProcessor
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans
from utility import FileUtil
from utility import Util

dataset = Itrust()

CAMEL = CamelCaseSplitter()
LOWER = LowerCaseTransformer()
LETTER = NonLetterFilter()
URL = UrlRemover()
SEP = Separator()
JAVASTOP = JavaCodeStopWordRemover()
JAVASTOP_IT = JavaCodeStopWordRemover(True)
STOP_IT = StopWordRemover(True)
STOP = StopWordRemover()
LEMMA_IT = Lemmatizer(Lemmatizer.LemmatizerType.italian_spacy)
LEMMA = Lemmatizer(Lemmatizer.LemmatizerType.english_spacy)
W_LENGTH = WordLengthFilter(2)

REQ_PREPROCESSOR = Preprocessor([URL, LETTER, CAMEL])
REQ_PREPROCESSOR_IT = Preprocessor([URL, LETTER, CAMEL])

ENGLISH_FASTTEXT_MODEL_PATH = "../../models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "../../models/cc.it.300.bin"

fasttext_model_path = ENGLISH_FASTTEXT_MODEL_PATH if dataset.is_english() else ITALIAN_FASTTEXT_MODEL_PATH

req_preprocessor = REQ_PREPROCESSOR if dataset.is_english() else REQ_PREPROCESSOR_IT
req_tokenizer = WordAndSentenceTokenizer(dataset, not dataset.is_english())
if dataset.has_UCT():
    if isinstance(dataset, Libest):
        req_tokenizer = NameAndDescriptionTokenizer(dataset, not dataset.is_english())
    else:
        req_tokenizer = UCTokenizer(dataset, not dataset.is_english())

all_filenames = FileUtil.get_files_in_directory(dataset.req_folder())
output = []
output.append("file,ID,text")
for file_path in all_filenames:
    file_representation = req_tokenizer.tokenize(file_path)
    file_representation.preprocess(req_preprocessor)
    if isinstance(file_representation, UseCaseFileRepresentation) or isinstance(file_representation, TextFileGroupedRepresentation):
        print(file_representation.get_csv_string())
        output.append(file_representation.get_csv_string())

FileUtil.write_file(dataset.folder() / "preprocessed.csv", "\n".join(output))
#req_embedding_containers = MockUCEmbeddingCreator(UCAllWordChooser(), req_preprocessor,
#                                                          RandomWordEmbeddingCreator(), req_tokenizer).create_all_embeddings(
#            dataset.req_folder())
#print(req_embedding_containers)
