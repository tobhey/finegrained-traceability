import os
import sys
from _functools import partial
import logging

from pathlib import Path
import gensim

from datasets.Dataset import Dataset, Libest
from embeddingCreator.CodeEmbeddingCreator import CodeBOEEmbeddingCreator, CodeVectorEmbeddingCreator, \
    MockCodeEmbeddingCreator, CodeEmbeddingCreator
from embeddingCreator.RequirementEmbeddingCreator import MockUCEmbeddingCreator, RequirementBOEEmbeddingCreator, \
    RequirementVectorEmbeddingCreator, UCEmbeddingCreator
from embeddingCreator.SimilarityComparator import SimilarityComparator
from embeddingCreator.UniXcoderEmbeddingCreator import UniXcoderEmbeddingCreator
from embeddingCreator.WordChooser import MethodBodyCommentSignatureChooser, MethodBodySignatureChooser, \
    MethodSignatureChooser, SentenceChooser, ClassnameWordChooser, MethodCommentSignatureChooser, \
    UCNameDescFlowWordChooser, ClassnameCommentWordChooser, UCAllWordChooser
from embeddingCreator.WordEmbeddingCreator import FastTextEmbeddingCreator, RandomWordEmbeddingCreator, FastTextAlignedEmbeddingCreator
from evaluation.OutputService import F1ExcelOutputService, MAPOutputService, MAPExcelOutputService, \
    CombinedExcelOutputService, LagOutputService, PrecisionRecallPairOutputService, TracelinkOutputService
from precalculating.TraceLinkDataStructure import ElementLevelTraceLinkDataStructure, FileLevelTraceLinkDataStructure
from precalculating.TraceLinkDataStructureFactory import ElementLevelTraceLinkDataStructureFactory, \
    FileLevelTraceLinkDataStructureFactory
from preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer, CCodeASTTokenizer
from preprocessing.Preprocessor import CamelCaseSplitter, LowerCaseTransformer, \
    NonLetterFilter, UrlRemover, Separator, JavaCodeStopWordRemover, \
    StopWordRemover, Lemmatizer, WordLengthFilter, Preprocessor, POSFilter
from preprocessing.Tokenizer import JavaDocDescriptionOnlyTokenizer, \
    WordAndSentenceTokenizer, UCTokenizer, WordTokenizer, NameAndDescriptionTokenizer
from traceLinkProcessing.ElementFilter import ElementFilter
from traceLinkProcessing.NeighborHandler import NeighborStrategy
from traceLinkProcessing.SimilarityFilter import SimilarityFilter
from traceLinkProcessing.TraceLinkCreator import CallGraphTraceLinkAggregator
from traceLinkProcessing.TraceLinkProcessor import MajProcessor, FileLevelProcessor
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
    LEMMA_IT = Lemmatizer(Lemmatizer.LemmatizerType.italian_spacy_non_pre)
    LEMMA = Lemmatizer(Lemmatizer.LemmatizerType.english_spacy_non_pre)
    W_LENGTH = WordLengthFilter(2)
    POS = POSFilter([POSFilter.POSTag.NN.value, POSFilter.POSTag.NNP.value, POSFilter.POSTag.NNS.value, POSFilter.POSTag.NNPS.value, 'JJ', 'JJR', 'JJS', 'VB', 'VBD', 'VBG', 'VBN', 'VBP', 'VBZ', 'RB', 'RBR', 'RBS'])

    REQ_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, STOP, W_LENGTH])
    CODE_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, JAVASTOP, STOP, W_LENGTH])
    REQ_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA_IT, STOP_IT, W_LENGTH])
    CODE_PREPROCESSOR_IT = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA_IT, JAVASTOP_IT, STOP_IT, W_LENGTH])
    REQ_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])
    CODE_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])

    def __init__(self, dataset, nqk=False):
        self.dataset = dataset
        self.req_preprocessor = self.REQ_PREPROCESSOR if dataset.is_english() else self.REQ_PREPROCESSOR_IT
        self.code_preprocessor = self.CODE_PREPROCESSOR if dataset.is_english() else self.CODE_PREPROCESSOR_IT
        if nqk:
            self.req_preprocessor = self.REQ_PREPROCESSOR_NQK
            self.code_preprocessor = self.CODE_PREPROCESSOR_NQK
        self.code_tokenizer = dataset.code_tokenizer()


    def get_model_for_language(self, models):
        if self.dataset.is_english():
            return models['english']
        else:
            return models['italian']


class AvgCosineRunner(TraceabilityRunner):
    DEFAULT_MATRIX_FILE_PATH = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_matrix.csv"

    def __init__(self, dataset: Dataset):
        super().__init__(dataset)

        self.req_reduce_func = max
        self.code_reduce_function = max
        self.similarity_filter = SimilarityFilter(True)

    def default_matrix_path(self):
        return self.DEFAULT_MATRIX_FILE_PATH.format(dataset_folder=self.dataset.folder(),
                                                    folder=self.DEFAULT_DATASOURCE_SUFFIX,
                                                    dataset_name=self.dataset.name(),
                                                    name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)

class ArtifactAvgCosineRunner(AvgCosineRunner):
    LABEL = "ArtifactAvgCosine"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactAvgCosine"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset)
        if not use_types:
            self.LABEL = self.LABEL + "NoTypes"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NoTypes"
        if isinstance(element_filter, ElementFilter):
            self.LABEL = self.LABEL + type(element_filter).__name__
        if not classname_as_optional_voter:
            self.LABEL = self.LABEL + "ClassVoter"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "ClassVoter"
        self.classname_as_optional_voter = classname_as_optional_voter
        self.req_tokenizer = WordAndSentenceTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = SentenceChooser()
        if dataset.has_UCT():
            if isinstance(dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCAllWordChooser()
        excel_output_folder = dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"
        self.method_word_chooser = MethodSignatureChooser(use_types)
        self.classname_word_chooser = ClassnameWordChooser()
        self.callgraph_aggregator = None
        self.element_filter = element_filter

    def calculate_f1(self, final_thresholds, majority_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        output_service = F1ExcelOutputService(self.dataset, self.excel_output_file_path)
        output_service.process_trace_link_dict(
            self._run(final_thresholds, matrix_file_path))

    def calculate_map(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = MAPOutputService(self.dataset, True, True, None)
        output_service.process_trace_link_dict(self._run([1], matrix_file_path))
        
    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None, default_final=0.44, default_maj=0.59, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final, default_maj, True, True, None, also_print_eval)
        if not 0 in final_thresholds:
            final_thresholds.append(0)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        output_service.process_trace_link_dict(
            self._run(final_thresholds, matrix_file_path))

    def output_trace_links(self, final_thresholds, maj_thresholds, matrix_file_path=None,
                           artifact_map_file_path=None, final=0.44, maj=0.59):
        csv_path = self.dataset.folder() / "output" / f"{self.dataset.name()}_{self.LABEL}_tracelinks.csv"
        output_service = TracelinkOutputService(csv_path)
        output_service.process_trace_link_dict(self._run(final_thresholds, matrix_file_path), final)

    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = FastTextEmbeddingCreator(self.get_model_for_language(models))
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()

        req_embedding_containers = UCEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor,
                                                          word_emb_creator,
                                                          self.req_tokenizer).create_all_embeddings(
            self.dataset.req_folder())
        code_embedding_containers = CodeEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                             self.code_preprocessor, word_emb_creator,
                                                             self.code_tokenizer,
                                                             classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
            self.dataset.code_folder())

        data_structure_factory = FileLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                           code_embedding_containers,
                                                                           Util.calculate_cos_sim)
        trace_link_data_structure = data_structure_factory.create()
        trace_link_data_structure.write_data(matrix_file_path)

    def _run(self, final_thresholds, matrix_file_path=None):
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not FileUtil.file_exists(matrix_file_path):
            log.error(f"File does not exists: {matrix_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
            sys.exit(1)

        trace_link_data_structure = FileLevelTraceLinkDataStructure.load_data_from(matrix_file_path)
        trace_link_processor = FileLevelProcessor(trace_link_data_structure, self.similarity_filter, final_thresholds)
        return trace_link_processor.run()
		
class ArtifactAvgCosineMCRunner(ArtifactAvgCosineRunner):
    """
    ArtifactAvgCosineRunner + Method Comments
    """

    LABEL = "ArtifactAvgCosineMc"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactAvgCosineMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)

class ArtifactAvgCosineUCTRunner(ArtifactAvgCosineRunner):
    """
    ArtifactAvgCosineRunner + Use Case Templates
    """
    LABEL = "ArtifactAvgCosineUct"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactAvgCosineUct"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = UCNameDescFlowWordChooser()

class ArtifactAvgCosineUCTMCRunner(ArtifactAvgCosineUCTRunner):
    """
    ArtifactAvgCosineRunner + Method Comments + Use Case Templates
    """

    LABEL = "ArtifactAvgCosineUctMc"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactAvgCosineUctMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)



class WMDRunner(TraceabilityRunner):
    DEFAULT_MATRIX_FILE_PATH = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_matrix.csv"

    def __init__(self, dataset: Dataset, nqk = False):
        super().__init__(dataset, nqk)

        self.req_reduce_func = min
        self.code_reduce_function = min
        self.similarity_filter = SimilarityFilter(False)

    def default_matrix_path(self):
        return self.DEFAULT_MATRIX_FILE_PATH.format(dataset_folder=self.dataset.folder(),
                                                    folder=self.DEFAULT_DATASOURCE_SUFFIX,
                                                    dataset_name=self.dataset.name(),
                                                    name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)

class ArtifactWMDRunner(WMDRunner):
    LABEL = "ArtifactWMD"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactWMD"
    WMD_VALUE_MAP_FUNCTION = partial(Util.map_value_range, 0, 2)  # Map the wmd distances from [0,2] to [0,1]

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset)
        if not use_types:
            self.LABEL = self.LABEL + "NoTypes"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NoTypes"
        if isinstance(element_filter, ElementFilter):
            self.LABEL = self.LABEL + type(element_filter).__name__
        if not classname_as_optional_voter:
            self.LABEL = self.LABEL + "ClassVoter"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "ClassVoter"
        self.classname_as_optional_voter = classname_as_optional_voter
        self.req_tokenizer = WordAndSentenceTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = SentenceChooser()
        if dataset.has_UCT():
            if isinstance(dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCAllWordChooser()
        excel_output_folder = dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"
        self.method_word_chooser = MethodSignatureChooser(use_types)
        self.classname_word_chooser = ClassnameWordChooser()
        self.callgraph_aggregator = None
        self.element_filter = element_filter

    def calculate_f1(self, final_thresholds, majority_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        output_service = F1ExcelOutputService(self.dataset, self.excel_output_file_path)
        output_service.process_trace_link_dict(
            self._run(final_thresholds, matrix_file_path))

    def calculate_map(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = MAPOutputService(self.dataset, True, False, None)
        output_service.process_trace_link_dict(self._run([1], matrix_file_path))
        
    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None, default_final=0.44, default_maj=0.59, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final, default_maj, True, False, None, also_print_eval)
        if not 1 in final_thresholds:
            final_thresholds.append(1)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        output_service.process_trace_link_dict(
            self._run(final_thresholds, matrix_file_path))

    def output_trace_links(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                           final=0.44, maj=0.59):
        csv_path = self.dataset.folder() / "output" / f"{self.dataset.name()}_{self.LABEL}_tracelinks.csv"
        output_service = TracelinkOutputService(csv_path)
        output_service.process_trace_link_dict(self._run(final_thresholds, matrix_file_path), final)
    
    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = FastTextEmbeddingCreator(self.get_model_for_language(models))
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()

        req_embedding_containers = MockUCEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor,
                                                          word_emb_creator,
                                                          self.req_tokenizer).create_all_embeddings(
            self.dataset.req_folder())
        code_embedding_containers = MockCodeEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                             self.code_preprocessor, word_emb_creator,
                                                             self.code_tokenizer,
                                                             classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
            self.dataset.code_folder())

        data_structure_factory = FileLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                           code_embedding_containers,
                                                                           word_emb_creator.word_movers_distance,
                                                                           self.WMD_VALUE_MAP_FUNCTION)
        trace_link_data_structure = data_structure_factory.create()
        trace_link_data_structure.write_data(matrix_file_path)

    def _run(self, final_thresholds, matrix_file_path=None):
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not FileUtil.file_exists(matrix_file_path):
            log.error(f"File does not exists: {matrix_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
            sys.exit(1)

        trace_link_data_structure = FileLevelTraceLinkDataStructure.load_data_from(matrix_file_path)
        trace_link_processor = FileLevelProcessor(trace_link_data_structure, self.similarity_filter, final_thresholds)
        return trace_link_processor.run()

class ArtifactWMDMCRunner(ArtifactWMDRunner):
    """
    ArtifactWMDRunner + Method Comments
    """

    LABEL = "ArtifactWMDMc"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactWMDMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)

class ArtifactWMDUCTRunner(ArtifactWMDRunner):
    """
    ArtifactWMDRunner + Use Case Templates
    """
    LABEL = "ArtifactWMDUct"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactWMDUct"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = UCNameDescFlowWordChooser()

class ArtifactWMDUCTMCRunner(ArtifactWMDUCTRunner):
    """
    ArtifactWMDRunner + Method Comments + Use Case Templates
    """

    LABEL = "ArtifactWMDUctMc"
    DEFAULT_DATASOURCE_SUFFIX = "ArtifactWMDUctMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)


class ElementAvgCosineRunner(AvgCosineRunner):
    LABEL = "ElementAvgCosine"
    DEFAULT_DATASOURCE_SUFFIX = "ElementAvgCosine"
    ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_a2eMap.json"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset)
        if not use_types:
            self.LABEL = self.LABEL + "NoTypes"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NoTypes"
        if isinstance(element_filter, ElementFilter):
            self.LABEL = self.LABEL + type(element_filter).__name__
        if not classname_as_optional_voter:
            self.LABEL = self.LABEL + "ClassVoter"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "ClassVoter"
        self.classname_as_optional_voter = classname_as_optional_voter
        self.req_tokenizer = WordAndSentenceTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = SentenceChooser()
        if dataset.has_UCT():
            if isinstance(dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCAllWordChooser()       
        excel_output_folder = dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"
        self.method_word_chooser = MethodSignatureChooser(use_types)
        self.classname_word_chooser = ClassnameWordChooser()
        self.callgraph_aggregator = None
        self.element_filter = element_filter

    def calculate_f1(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        output_service = F1ExcelOutputService(self.dataset, self.excel_output_file_path)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))

    def calculate_map(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = MAPExcelOutputService(self.dataset, True, True, None,  self.excel_output_file_path)
        output_service.process_trace_link_2D_dict(self._run([1], [1], matrix_file_path, artifact_map_file_path))

    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                             default_final=0.44, default_maj=0.59, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final,
                                                    default_maj, True, True, None, also_print_eval)
        if not 0 in final_thresholds:
            final_thresholds.append(0)
        if not 0 in maj_thresholds:
            maj_thresholds.append(0)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        if not default_maj in maj_thresholds:
            maj_thresholds.append(default_maj)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))

    def output_trace_links(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                           final=0.44, maj=0.59):
        csv_path = self.dataset.folder() / "output" / f"{self.dataset.name()}_{self.LABEL}_tracelinks.csv"
        output_service = TracelinkOutputService(csv_path)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path), maj, final)

    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = FastTextEmbeddingCreator(self.get_model_for_language(models))
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()

        req_embedding_containers = UCEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor,
                                                      word_emb_creator,
                                                      self.req_tokenizer).create_all_embeddings(
            self.dataset.req_folder())
        code_embedding_containers = CodeEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                         self.code_preprocessor, word_emb_creator,
                                                         self.code_tokenizer,
                                                         classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
            self.dataset.code_folder())

        data_structure_factory = ElementLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                        code_embedding_containers,
                                                                        Util.calculate_cos_sim)
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
            sys.exit(1)
        if not FileUtil.file_exists(artifact_map_file_path):
            log.error(f"File does not exists: {artifact_map_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
            sys.exit(1)

        trace_link_data_structure = ElementLevelTraceLinkDataStructure.load_data_from(matrix_file_path, artifact_map_file_path)
        trace_link_processor = MajProcessor(trace_link_data_structure, self.similarity_filter, self.req_reduce_func, self.code_reduce_function, final_thresholds, maj_thresholds, self.callgraph_aggregator)
        return trace_link_processor.run()

    def _default_a2eMap_path(self):
        return self.ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN.format(dataset_folder=self.dataset.folder(),
                                                                folder=self.DEFAULT_DATASOURCE_SUFFIX,
                                                                dataset_name=self.dataset.name(),
                                                                name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)

class ElementAvgCosineMCRunner(ElementAvgCosineRunner):
    """
    Element level cosine avg + Method Comments
    """

    LABEL = "ElementAvgCosineMc"
    DEFAULT_DATASOURCE_SUFFIX = "ElementAvgCosineMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)


class ElementAvgCosineCDRunner(ElementAvgCosineRunner):
    """
    ElementAvgCosine + Call Graph Dependency
    """
    LABEL = "ElementAvgCosineCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as ElementAvgCosine

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class ElementAvgCosineUCTRunner(ElementAvgCosineRunner):
    """
    ElementAvgCosine + Use Case Templates
    """
    LABEL = "ElementAvgCosineUct"
    DEFAULT_DATASOURCE_SUFFIX = "ElementAvgCosineUct"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        if isinstance(dataset, Libest):
            self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
        else:
            self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = UCNameDescFlowWordChooser()


class ElementAvgCosineUCTCDRunner(ElementAvgCosineUCTRunner):
    """
    ElementAvgCosine + Use Case Templates + Call Graph Dependency
    """
    LABEL = "ElementAvgCosineUctCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as ElementAvgCosineUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class ElementAvgCosineMCCDRunner(ElementAvgCosineMCRunner):
    """
    ElementAvgCosine + Method Comments + Call Graph Dependency
    """
    LABEL = "ElementAvgCosineMcCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as ElementAvgCosineMc

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())

class ElementAvgCosineMCCDCCRunner(ElementAvgCosineMCCDRunner):
    """
    ElementAvgCosine + Method Comments + Call Dependency + Class Comments
    """

    LABEL = "ElementAvgCosineMcCdCC"
    DEFAULT_DATASOURCE_SUFFIX = "ElementAvgCosineMcCC"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as ElementAvgCosineMcUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.classname_word_chooser = ClassnameCommentWordChooser()


class ElementAvgCosineUCTMCRunner(ElementAvgCosineUCTRunner):
    """
    ElementAvgCosine + Method Comments + Use Case Templates
    """

    LABEL = "ElementAvgCosineUctMc"
    DEFAULT_DATASOURCE_SUFFIX = "ElementAvgCosineUctMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)


class ElementAvgCosineUCTMCCDRunner(ElementAvgCosineUCTMCRunner):
    """
    ElementAvgCosine + Method Comments + Use Case Templates + Call Dependency
    """

    LABEL = "ElementAvgCosineUctMcCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as ElementAvgCosineMcUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())



class FTLRRunner(WMDRunner):
    ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN = "{dataset_folder}/{folder}/{dataset_name}_{name_suffix}_a2eMap.json"
    LABEL = "FTLR"
    DEFAULT_DATASOURCE_SUFFIX = "FTLR"
    WMD_VALUE_MAP_FUNCTION = partial(Util.map_value_range, 0, 2)  # Map the wmd distances from [0,2] to [0,1]

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, nqk)
        if nqk:
            self.LABEL = self.LABEL + "NQK"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NQK"
        if not use_types:
            self.LABEL = self.LABEL + "NoTypes"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NoTypes"
        if isinstance(element_filter, ElementFilter):
            self.LABEL = self.LABEL + type(element_filter).__name__
        if not classname_as_optional_voter:
            self.LABEL = self.LABEL + "ClassVoter"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "ClassVoter"
        self.classname_as_optional_voter = classname_as_optional_voter
        self.req_tokenizer = WordAndSentenceTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = SentenceChooser()
        if dataset.has_UCT():
            if isinstance(dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCAllWordChooser()
        excel_output_folder = dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"
        self.method_word_chooser = MethodSignatureChooser(use_types)
        self.classname_word_chooser = ClassnameWordChooser()
        self.callgraph_aggregator = None
        self.element_filter = element_filter

    def calculate_f1(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None):
        output_service = F1ExcelOutputService(self.dataset, self.excel_output_file_path)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))

    def calculate_map(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = MAPExcelOutputService(self.dataset, True, False, None, self.excel_output_file_path)
        output_service.process_trace_link_2D_dict(self._run([1], [1], matrix_file_path, artifact_map_file_path))

    def calculate_lag(self, matrix_file_path=None, artifact_map_file_path=None):
        output_service = LagOutputService(self.dataset, True, False)
        output_service.process_trace_link_2D_dict(self._run([1], [1], matrix_file_path, artifact_map_file_path))
        
    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None, default_final=0.44, default_maj=0.59, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final, default_maj, True, False, None, also_print_eval)
        if not 1 in final_thresholds:
            final_thresholds.append(1)
        if not 1 in maj_thresholds:
            maj_thresholds.append(1)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        if not default_maj in maj_thresholds:
            maj_thresholds.append(default_maj)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))

    def output_trace_links(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                           final=0.44, maj=0.59):
        csv_path = self.dataset.folder() / "output" / f"{self.dataset.name()}_{self.LABEL}_tracelinks.csv"
        output_service = TracelinkOutputService(csv_path)
        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path), maj, final)

    def calculate_precision_recall_curve_csv(self, matrix_file_path=None, artifact_map_file_path=None):
        csv_path = self.dataset.folder() / "output" / f"{self.dataset.name()}_{self.LABEL}_curve.csv"
        output_service = PrecisionRecallPairOutputService(self.dataset, csv_path, also_print_eval=True)
        output_service.process_trace_link_2D_dict(self._run(Util.get_range_array(0.0, 1.0, 0.001), Util.get_range_array(0.0,1.0,0.001), matrix_file_path, artifact_map_file_path))

    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = FastTextEmbeddingCreator(self.get_model_for_language(models))
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()

        req_embedding_containers = MockUCEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor,
                                                          word_emb_creator, self.req_tokenizer).create_all_embeddings(
            self.dataset.req_folder())
        code_embedding_containers = MockCodeEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                             self.code_preprocessor, word_emb_creator,
                                                             self.code_tokenizer,
                                                             classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
            self.dataset.code_folder())

        data_structure_factory = ElementLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                           code_embedding_containers,
                                                                           word_emb_creator.word_movers_distance,
                                                                           self.WMD_VALUE_MAP_FUNCTION)
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
            sys.exit(1)
        if not FileUtil.file_exists(artifact_map_file_path):
            log.error(f"File does not exists: {artifact_map_file_path}\n"
                      f"Please pass a valid file path or call {self.__class__.__name__}().precalculate() first")
            sys.exit(1)

        trace_link_data_structure = ElementLevelTraceLinkDataStructure.load_data_from(matrix_file_path,
                                                                                      artifact_map_file_path)
        if isinstance(self.element_filter, ElementFilter):
            trace_link_data_structure = self.element_filter.filter(trace_link_data_structure, self.dataset)
        trace_link_processor = MajProcessor(trace_link_data_structure, self.similarity_filter, self.req_reduce_func,
                                            self.code_reduce_function, final_thresholds, maj_thresholds,
                                            self.callgraph_aggregator)
        return trace_link_processor.run()

    def _default_a2eMap_path(self):
        return self.ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN.format(dataset_folder=self.dataset.folder(),
                                                                folder=self.DEFAULT_DATASOURCE_SUFFIX,
                                                                dataset_name=self.dataset.name(),
                                                                name_suffix=self.DEFAULT_DATASOURCE_SUFFIX)


class FTLRMCRunner(FTLRRunner):
    """
    FTLR + Method Comments
    """

    LABEL = "FTLRMc"
    DEFAULT_DATASOURCE_SUFFIX = "FTLRMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)


class FTLRCDRunner(FTLRRunner):
    """
    FTLR + Call Graph Dependency
    """
    LABEL = "FTLRCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as FTLR

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class FTLRUCTRunner(FTLRRunner):
    """
    FTLR + Use Case Templates
    """
    LABEL = "FTLRUct"
    DEFAULT_DATASOURCE_SUFFIX = "FTLRUct"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        if isinstance(dataset, Libest):
            self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
        else:
            self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
        self.requirements_word_chooser = UCNameDescFlowWordChooser()


class FTLRUCTCDRunner(FTLRUCTRunner):
    """
    FTLR + Use Case Templates + Call Graph Dependency
    """
    LABEL = "FTLRUctCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as FTLRUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class FTLRMCCDRunner(FTLRMCRunner):
    """
    FTLR + Method Comments + Call Graph Dependency
    """
    LABEL = "FTLRMcCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as FTLRMc

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())

class FTLRMCCDCCRunner(FTLRMCCDRunner):
    """
    FTLR + Method Comments + Use Case Templates + Call Dependency
    """

    LABEL = "FTLRMcCdCC"
    DEFAULT_DATASOURCE_SUFFIX = "FTLRMcCC"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as FTLRMcUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.classname_word_chooser = ClassnameCommentWordChooser()


class FTLRUCTMCRunner(FTLRUCTRunner):
    """
    FTLR + Method Comments + Use Case Templates
    """

    LABEL = "FTLRUctMc"
    DEFAULT_DATASOURCE_SUFFIX = "FTLRUctMc"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.method_word_chooser = MethodCommentSignatureChooser(use_types)


class FTLRUCTMCCDRunner(FTLRUCTMCRunner):
    """
    FTLR + Method Comments + Use Case Templates + Call Dependency
    """

    LABEL = "FTLRUctMcCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as FTLRMcUct

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class FTLRUCTMCCDCCRunner(FTLRUCTMCCDRunner):
    """
    FTLR + Method Comments + Use Case Templates + Call Dependency
    """

    LABEL = "FTLRUctMcCdCC"
    DEFAULT_DATASOURCE_SUFFIX = "FTLRUctMcCC"

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=False):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.classname_word_chooser = ClassnameCommentWordChooser()


class UniXcoderRunner(FTLRRunner):
    """
    Language Model: UniXcoder
    """

    LABEL = "UniXcoder"
    DEFAULT_DATASOURCE_SUFFIX = "UniXcoder"
    
    LOWER = LowerCaseTransformer()
    LETTER = NonLetterFilter()
    URL = UrlRemover()
    SEP = Separator()

    REQ_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])
    CODE_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])


    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        if nqk:
            self.req_preprocessor = self.REQ_PREPROCESSOR_NQK
            self.code_preprocessor = self.CODE_PREPROCESSOR_NQK
            self.LABEL = self.LABEL + "NQK"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NQK"
        
        self.bigger_is_more_similar = True 
        self.req_reduce_func = max
        self.code_reduce_function = max
        self.similarity_filter = SimilarityFilter(True)


    def configurate_word_choosers(self, use_types=True, uct=False, mc=False, mb=False):
        if uct:
            if isinstance(self.dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCNameDescFlowWordChooser()
            self.LABEL = self.LABEL + "Uct"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Uct"

        if not mc and not mb:
            self.method_word_chooser = MethodSignatureChooser(use_types)
        elif mc and not mb:
            self.method_word_chooser = MethodCommentSignatureChooser(use_types)
            self.LABEL = self.LABEL + "Mc"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Mc"
        elif not mc and mb:
            self.method_word_chooser = MethodBodySignatureChooser(use_types)
            self.LABEL = self.LABEL + "Mb"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Mb"
        elif mc and mb:
            self.method_word_chooser = MethodBodyCommentSignatureChooser(use_types)
            self.LABEL = self.LABEL + "McMb"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "McMb"
        
        excel_output_folder = self.dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"


    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = UniXcoderEmbeddingCreator(model_path=models['unixcoder'])
        similarity_comparator = SimilarityComparator(True)
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()

        req_embedding_containers = None
        code_embedding_containers = None

        
        req_embedding_containers = RequirementVectorEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor, word_emb_creator, self.req_tokenizer).create_all_embeddings(self.dataset.req_folder())
        code_embedding_containers = CodeVectorEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                             self.code_preprocessor, word_emb_creator,
                                                             self.code_tokenizer,                                                             classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
                self.dataset.code_folder())


        data_structure_factory = ElementLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                           code_embedding_containers,
                                                                           similarity_comparator.calculate_similarity,
                                                                           None)
        trace_link_data_structure = data_structure_factory.create()
        trace_link_data_structure.write_data(matrix_file_path, artifact_map_file_path)


    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                             default_final=0.54, default_maj=0.39, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final,
                                                    default_maj, True, True, None, also_print_eval)

        if not 0 in final_thresholds:
            final_thresholds.append(0)
        if not 0 in maj_thresholds:
                maj_thresholds.append(0)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        if not default_maj in maj_thresholds:
            maj_thresholds.append(default_maj)

        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))


class UniXcoderCDRunner(UniXcoderRunner):
    """
        UniXcoderWMD + Call Graph Dependency
        """
    LABEL = "UniXcoderCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as UniXcoder

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True,
                 nqk=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())


class UniXcoderWMDRunner(FTLRRunner):
    """
    Language Model: UniXcoder
    """

    LABEL = "UniXcoderWMD"
    DEFAULT_DATASOURCE_SUFFIX = "UniXcoderWMD"
    
    LOWER = LowerCaseTransformer()
    LETTER = NonLetterFilter()
    URL = UrlRemover()
    SEP = Separator()

    REQ_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])
    CODE_PREPROCESSOR_NQK = Preprocessor([URL, SEP, LETTER, LOWER])


    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter)
        if nqk:
            self.req_preprocessor = self.REQ_PREPROCESSOR_NQK
            self.code_preprocessor = self.CODE_PREPROCESSOR_NQK
            self.LABEL = self.LABEL + "NQK"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "NQK"
        
        self.bigger_is_more_similar = True 
        self.req_reduce_func = max
        self.code_reduce_function = max
        self.similarity_filter = SimilarityFilter(True)


    def configurate_word_choosers(self, use_types, uct, mc, mb):
        if uct:
            if isinstance(self.dataset, Libest):
                self.req_tokenizer = NameAndDescriptionTokenizer(self.dataset, not self.dataset.is_english())
            else:
                self.req_tokenizer = UCTokenizer(self.dataset, not self.dataset.is_english())
            self.requirements_word_chooser = UCNameDescFlowWordChooser()
            self.LABEL = self.LABEL + "Uct"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Uct"

        if not mc and not mb:
            self.method_word_chooser = MethodSignatureChooser(use_types)
        elif mc and not mb:
            self.method_word_chooser = MethodCommentSignatureChooser(use_types)
            self.LABEL = self.LABEL + "Mc"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Mc"
        elif not mc and mb:
            self.method_word_chooser = MethodBodySignatureChooser(use_types)
            self.LABEL = self.LABEL + "Mb"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "Mb"
        elif mc and mb:
            self.method_word_chooser = MethodBodyCommentSignatureChooser(use_types)
            self.LABEL = self.LABEL + "McMb"
            self.DEFAULT_DATASOURCE_SUFFIX = self.DEFAULT_DATASOURCE_SUFFIX + "McMb"
        
        excel_output_folder = self.dataset.folder() / "output"
        if not os.path.exists(excel_output_folder):
            os.mkdir(excel_output_folder)
        self.excel_output_file_path = excel_output_folder / f"{self.dataset.name()}_{self.LABEL}_eval_result.xlsx"


    def precalculate(self, models, matrix_file_path=None, artifact_map_file_path=None):
        word_emb_creator = UniXcoderEmbeddingCreator(model_path=models['unixcoder'])
        similarity_comparator = SimilarityComparator(False)
        if not matrix_file_path:
            matrix_file_path = self.default_matrix_path()
        if not artifact_map_file_path:
            artifact_map_file_path = self._default_a2eMap_path()

        req_embedding_containers = None
        code_embedding_containers = None

        req_embedding_containers = RequirementBOEEmbeddingCreator(self.requirements_word_chooser, self.req_preprocessor,
                                                          word_emb_creator, self.req_tokenizer).create_all_embeddings(
                self.dataset.req_folder())
        code_embedding_containers = CodeBOEEmbeddingCreator(self.method_word_chooser, self.classname_word_chooser,
                                                             self.code_preprocessor, word_emb_creator,
                                                             self.code_tokenizer,                                                     classname_as_optional_voter=self.classname_as_optional_voter).create_all_embeddings(
                self.dataset.code_folder())


        data_structure_factory = ElementLevelTraceLinkDataStructureFactory(req_embedding_containers,
                                                                           code_embedding_containers,
                                                                           similarity_comparator.calculate_similarity,
                                                                           None)
        trace_link_data_structure = data_structure_factory.create()
        trace_link_data_structure.write_data(matrix_file_path, artifact_map_file_path)


    def calculate_f1_and_map(self, final_thresholds, maj_thresholds, matrix_file_path=None, artifact_map_file_path=None,
                             default_final=0.54, default_maj=0.39, also_print_eval=True):
        output_service = CombinedExcelOutputService(self.dataset, self.excel_output_file_path, default_final,
                                                    default_maj, True, True, None, also_print_eval)

        if not 0 in final_thresholds:
            final_thresholds.append(0)
        if not 0 in maj_thresholds:
                maj_thresholds.append(0)
        if not default_final in final_thresholds:
            final_thresholds.append(default_final)
        if not default_maj in maj_thresholds:
            maj_thresholds.append(default_maj)

        output_service.process_trace_link_2D_dict(
            self._run(final_thresholds, maj_thresholds, matrix_file_path, artifact_map_file_path))

class UniXcoderWMDCDRunner(UniXcoderWMDRunner):
    """
        UniXcoderWMD + Call Graph Dependency
        """
    LABEL = "UniXcoderWMDCd"

    # Don't override the DEFAULT_DATASOURCE_SUFFIX because this runner uses the same default precalculated file-(name) as UniXcoderWMD

    def __init__(self, dataset: Dataset, use_types=True, element_filter=None, classname_as_optional_voter=True, nqk=True):
        super().__init__(dataset, use_types, element_filter, classname_as_optional_voter, nqk)
        self.callgraph_aggregator = CallGraphTraceLinkAggregator(0.9, NeighborStrategy.both, dataset.method_callgraph())
