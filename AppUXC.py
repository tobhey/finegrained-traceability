
from TraceabilityRunner import FTLRRunner, FTLRCDRunner, \
    FTLRUCTRunner, FTLRMCRunner, \
    FTLRUCTMCRunner, FTLRUCTCDRunner, FTLRUCTMCCDRunner, \
    FTLRMCCDRunner, OUTPUT_DIR, FTLRUCTMCCDCCRunner, ArtifactWMDRunner,ArtifactWMDUCTRunner, ArtifactWMDMCRunner, \
    ArtifactWMDUCTMCRunner, ArtifactAvgCosineRunner, ArtifactAvgCosineUCTRunner, ArtifactAvgCosineMCRunner, ArtifactAvgCosineUCTMCRunner, \
    ElementAvgCosineRunner, ElementAvgCosineCDRunner, ElementAvgCosineMCRunner, ElementAvgCosineUCTRunner, \
    ElementAvgCosineUCTMCRunner, ElementAvgCosineMCCDRunner, ElementAvgCosineUCTCDRunner, ElementAvgCosineUCTMCCDRunner, \
    UniXcoderRunner
from traceLinkProcessing.ElementFilter import ElementFilter, NFRElementFilter, \
    UserRelatedElementFilter, UserRelatedNFRElementFilter
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans, Libest, Albergate, ItrustFull
from utility import Util
from utility.FileUtil import setup_clear_dir

ENGLISH_FASTTEXT_MODEL_PATH = "../models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "../models/cc.it.300.bin"
UNIXCODER_MODEL_PATH = "../models/unixcoder-base"
models = {'english': ENGLISH_FASTTEXT_MODEL_PATH, 'italian': ITALIAN_FASTTEXT_MODEL_PATH, 'unixcoder': UNIXCODER_MODEL_PATH}

FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]

setup_clear_dir(OUTPUT_DIR)

"""
PRECALCULATION EXAMPLE

"""

# Runs FTLR with UniXcoder cosinus distance and reduced nqk preprocessing on eTour.
b = UniXcoderRunner(Etour(), element_filter=None, nqk=True)

# select whether to use use case templates (UCT), method comments (MC) or the method body (MB)
b.configurate_word_choosers(uct=True, mc=False, mb=False)

b.precalculate(models, matrix_file_path=None, artifact_map_file_path=None)


"""
DETERMINE MAP and THRESHOLD WITH BEST F1 EXAMPLE

"""

FINAL_THRESHOLDS = Util.get_range_array(0.0, 1.0, 0.001)
MAJORITY_THRESHOLDS = Util.get_range_array(0.0, 1.0, 0.001)


b.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)



