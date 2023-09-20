from datasets.Dataset import Dataset
from embeddingCreator.CodeEmbeddingCreator import MockCodeEmbeddingCreator
from embeddingCreator.RequirementEmbeddingCreator import MockUCEmbeddingCreator
from embeddingCreator.WordChooser import MethodSignatureChooser, SentenceChooser, \
    ClassnameWordChooser, MethodCommentSignatureChooser, \
    UCNameDescFlowWordChooser
from embeddingCreator.WordEmbeddingCreator import FastTextEmbeddingCreator, RandomWordEmbeddingCreator
from evaluation.OutputService import F1ExcelOutputService, MAPOutputService
from precalculating.TraceLinkDataStructure import ElementLevelTraceLinkDataStructure
from precalculating.TraceLinkDataStructureFactory import ElementLevelTraceLinkDataStructureFactory
from preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer
from preprocessing.Preprocessor import CamelCaseSplitter, LowerCaseTransformer, \
    NonLetterFilter, UrlRemover, Separator, JavaCodeStopWordRemover, \
    StopWordRemover, Lemmatizer, WordLengthFilter, Preprocessor
from preprocessing.Tokenizer import JavaDocDescriptionOnlyTokenizer, \
    WordAndSentenceTokenizer, UCTokenizer
from traceLinkProcessing.ElementFilter import ElementFilter
from traceLinkProcessing.NeighborHandler import NeighborStrategy
from traceLinkProcessing.SimilarityFilter import SimilarityFilter
from traceLinkProcessing.TraceLinkCreator import CallGraphTraceLinkAggregator
from traceLinkProcessing.TraceLinkProcessor import MajProcessor
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans
from utility import FileUtil
from utility import Util

dataset = Eanci()

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

REQ_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, STOP, W_LENGTH])
CODE_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, JAVASTOP, STOP, W_LENGTH])
REQ_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA_IT, STOP_IT, W_LENGTH])
CODE_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA_IT, JAVASTOP_IT, STOP_IT, W_LENGTH])

code_preprocessor = CODE_PREPROCESSOR if dataset.is_english() else CODE_PREPROCESSOR_IT
code_tokenizer = JavaCodeASTTokenizer(dataset,
                                                   JavaDocDescriptionOnlyTokenizer(dataset, not dataset.is_english()))
ENGLISH_FASTTEXT_MODEL_PATH = "../../models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "../../models/cc.it.300.bin"

fasttext_model_path = ENGLISH_FASTTEXT_MODEL_PATH if dataset.is_english() else ITALIAN_FASTTEXT_MODEL_PATH


code_embedding_containers = MockCodeEmbeddingCreator(MethodCommentSignatureChooser(), ClassnameWordChooser(),
                                                             code_preprocessor, RandomWordEmbeddingCreator(),
                                                             code_tokenizer,
                                                             classname_as_optional_voter=True).create_all_embeddings(
            dataset.code_folder())
print(code_embedding_containers)
counts = dict()
for container in code_embedding_containers:
    for word in container.file_vector:
        if word in counts:
            counts[word] += 1
        else:
            counts[word] = 1
for w in sorted(counts, key=counts.get, reverse=True):
    print(w, counts[w])
