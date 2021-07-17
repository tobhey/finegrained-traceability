from _functools import partial
import logging

from pathlib import Path
import gensim

from datasets.Dataset import Dataset
from embeddingCreator.CodeEmbeddingCreator import MockCodeEmbeddingCreator
from embeddingCreator.RequirementEmbeddingCreator import MockUCEmbeddingCreator
from embeddingCreator.WordChooser import MethodSignatureChooser, SentenceChooser, \
    ClassnameWordChooser, MethodCommentSignatureChooser, \
    UCNameDescFlowWordChooser
from embeddingCreator.WordEmbeddingCreator import FastTextEmbeddingCreator
from evaluation.OutputService import F1ExcelOutputService, MAPOutputService
from precalculating.TraceLinkDataStructure import ElementLevelTraceLinkDataStructure
from precalculating.TraceLinkDataStructureFactory import ElementLevelTraceLinkDataStructureFactory
from preprocessing.JavaCodeASTTokenizer import JavaCodeASTTokenizer
from preprocessing.Preprocessor import CamelCaseSplitter, LowerCaseTransformer, \
    NonLetterFilter, UrlRemover, Separator, JavaCodeStopWordRemover, \
    StopWordRemover, Lemmatizer, WordLengthFilter, Preprocessor
from preprocessing.Tokenizer import JavaDocDescriptionOnlyTokenizer, \
    WordAndSentenceTokenizer, UCTokenizer
from traceLinkProcessing.NeighborHandler import NeighborStrategy
from traceLinkProcessing.SimilarityFilter import SimilarityFilter
from traceLinkProcessing.TraceLinkCreator import CallGraphTraceLinkAggregator
from traceLinkProcessing.TraceLinkProcessor import MajProcessor
from utility import FileUtil
from utility import Util

gensim.logger.setLevel(logging.ERROR)
logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)

OUTPUT_DIR = Path(__file__).parent / "output"


class TraceabilityRunner:
    
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
    CODE_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, JAVASTOP, LOWER, LEMMA, STOP, W_LENGTH])
    REQ_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA_IT, STOP_IT, W_LENGTH])
    CODE_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, JAVASTOP_IT, LOWER, LEMMA_IT, STOP_IT, W_LENGTH])

    def __init__(self, dataset):
        self.dataset = dataset
        self.req_preprocessor = self.REQ_PREPROCESSOR if dataset.is_english() else self.REQ_PREPROCESSOR_IT
        self.code_preprocessor = self.CODE_PREPROCESSOR if dataset.is_english() else self.CODE_PREPROCESSOR_IT
        self.code_tokenizer = JavaCodeASTTokenizer(dataset, JavaDocDescriptionOnlyTokenizer(dataset, not dataset.is_english()))


class WMDRunner(TraceabilityRunner):
    DEFAULT_MATRIX_FILE_PATH = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_matrix.csv"

    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        
        self.req_reduce_func = min
        self.code_reduce_function = min
        self.similarity_filter = SimilarityFilter(False)
    
    def default_matrix_path(self):
        return self.DEFAULT_MATRIX_FILE_PATH.format(dataset_folder=self.dataset.folder(), folder=self.DEFAULT_DATASOURCE_SUFFIX, dataset_name=self.dataset.name(), name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)


class BaseLineRunner(WMDRunner):
    ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_a2eMap.json"
    LABEL = "BaseLine"
    DEFAULT_DATASOURCE_SUFFIX = "BaseLine"
    WMD_VALUE_MAP_FUNCTION = partial(Util.map_value_range, 0, 2)  # Map the wmd distances from [0,2] to [0,1]
    
    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.req_tokenizer = WordAndSentenceTokenizer(self.dataset, not self.dataset.is_english())
        self.excel_output_file_path = OUTPUT_DIR / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"
        self.requirements_word_chooser = SentenceChooser()
        self.method_word_chooser = MethodSignatureChooser()
        self.classname_word_chooser = ClassnameWordChooser()
        self.callgraph_aggregator = None
        
    def calculate_f1(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        output_service = F1ExcelOutputService(self.dataset, self.excel_output_file_path)
        output_service.process_trace_link_2D_dict(self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))
        
    def calculate_map(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = MAPOutputService(self.dataset, True, False, None)
        output_service.process_trace_link_2D_dict(self._run([1], [1], matrix_file_path, artifact_map_file_path))
    
    def precalculate(self, fasttext_model_path, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = FastTextEmbeddingCreator(fasttext_model_path)
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()
            
        req_embedding_containers = MockUCEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor, word_emb_creator, self.req_tokenizer).create_all_embeddings(self.dataset.req_folder())
        code_embedding_containers = MockCodeEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser, self.code_preprocessor, word_emb_creator, self.code_tokenizer, classname_as_optional_voter=True).create_all_embeddings(self.dataset.code_folder())
        
        data_structure_factory = ElementLevelTraceLinkDataStructureFactory(req_embedding_containers, code_embedding_containers, word_emb_creator.word_movers_distance, self.WMD_VALUE_MAP_FUNCTION)
        trace_link_data_structure = data_structure_factory.create()
        trace_link_data_structure.write_data(matrix_file_path, artifact_map_file_path)

    def _run(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()
        if not FileUtil.file_exists(matrix_file_path):
            log.error(f"File does not exists: {matrix_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
        if not FileUtil.file_exists(artifact_map_file_path):
            log.error(f"File does not exists: {artifact_map_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
            
        trace_link_data_structure = ElementLevelTraceLinkDataStructure.load_data_from(matrix_file_path, artifact_map_file_path)
        trace_link_processor = MajProcessor(trace_link_data_structure, self.similarity_filter, self.req_reduce_func, self.code_reduce_function, final_thresholds, maj_thresholds, self.callgraph_aggregator)
        return trace_link_processor.run()
    
    def _default_a2eMap_path(self):
        return self.ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN.format(dataset_folder=self.dataset.folder(), folder=self.DEFAULT_DATASOURCE_SUFFIX, dataset_name=self.dataset.name(), name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)

    
class BaseLineMCRunner(BaseLineRunner):
    """
    BaseLine + Method Comments
    """

    LABEL = "BaseLineMc"
    DEFAULT_DATASOURCE_SUFFIX = "BaseLineMc"

    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.method_word_chooser = MethodCommentSignatureChooser()


class BaseLineCDRunner(BaseLineRunner):
    """
    BaseLine + Call Graph Dependency
    """
    LABEL = "BaseLineCd"
    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as BaseLine
    
    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())

        
class BaseLineUCTRunner(BaseLineRunner):
    """
    BaseLine + Use Case Templates
    """
    LABEL = "BaseLineUct"
    DEFAULT_DATASOURCE_SUFFIX = "BaseLineUct"
    
    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = UCNameDescFlowWordChooser()


class BaseLineUCTCDRunner(BaseLineUCTRunner):
    """
    BaseLine + Use Case Templates + Call Graph Dependency
    """
    LABEL = "BaseLineUctCd"
    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as BaseLineUct
    
    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class BaseLineMCCDRunner(BaseLineMCRunner):
    """
    BaseLine + Method Comments + Call Graph Dependency
    """
    LABEL = "BaseLineMcCd"
    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as BaseLineMc
    
    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())

        
class BaseLineUCTMCRunner(BaseLineUCTRunner):
    """
    BaseLine + Method Comments + Use Case Templates
    """

    LABEL = "BaseLineUctMc"
    DEFAULT_DATASOURCE_SUFFIX = "BaseLineUctMc"

    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.method_word_chooser = MethodCommentSignatureChooser()


class BaseLineUCTMCCDRunner(BaseLineUCTMCRunner):
    """
    BaseLine + Method Comments + Use Case Templates + Call Dependency
    """

    LABEL = "BaseLineUctMcCd"
    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as BaseLineMcUct

    def __init__(self, dataset: Dataset):
        super().__init__(dataset)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())
