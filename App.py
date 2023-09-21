
from TraceabilityRunner import FTLRRunner, FTLRCDRunner, \
    FTLRUCTRunner, FTLRMCRunner, \
    FTLRUCTMCRunner, FTLRUCTCDRunner, FTLRUCTMCCDRunner, \
    FTLRMCCDRunner, OUTPUT_DIR, FTLRUCTMCCDCCRunner, ArtifactWMDRunner,ArtifactWMDUCTRunner, ArtifactWMDMCRunner, \
    ArtifactWMDUCTMCRunner, ArtifactAvgCosineRunner, ArtifactAvgCosineUCTRunner, ArtifactAvgCosineMCRunner, ArtifactAvgCosineUCTMCRunner, \
    ElementAvgCosineRunner, ElementAvgCosineCDRunner, ElementAvgCosineMCRunner, ElementAvgCosineUCTRunner, \
    ElementAvgCosineUCTMCRunner, ElementAvgCosineMCCDRunner, ElementAvgCosineUCTCDRunner, ElementAvgCosineUCTMCCDRunner
from traceLinkProcessing.ElementFilter import ElementFilter, NFRElementFilter, \
    UserRelatedElementFilter, UserRelatedNFRElementFilter
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans, Libest, Albergate, ItrustFull
from utility import Util
from utility.FileUtil import setup_clear_dir

ENGLISH_FASTTEXT_MODEL_PATH = "../models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "../models/cc.it.300.bin"
UNIXCODER_MODEL_PATH = "../models/unixcoder-base"
models = {'english': ENGLISH_FASTTEXT_MODEL_PATH, 'italian': ITALIAN_FASTTEXT_MODEL_PATH, 'unixcoder': UNIXCODER_MODEL_PATH}

setup_clear_dir(OUTPUT_DIR)

"""
PRECALCULATION EXAMPLE

1. Modify ENGLISH_FASTTEXT_MODEL_PATH and ITALIAN_FASTTEXT_MODEL_PATH to point to your fastText model location
2. Run the code below
3. Caution: With matrix_file_path=None, artifact_map_file_path=None the output precalculated files will be written
            to their default location which will overwrite the existing files. Change the location to avoid this.

runner = FTLRUCTMCCDRunner(Etour())
runner.precalculate(models, matrix_file_path=None, artifact_map_file_path=None)

"""

# Runs the FTLR +uct + mc +cd config on eTour.
runner = FTLRUCTMCCDRunner(Etour(),
                           #define requirements element filters to use
                           #element_filter=NFRElementFilter())
                           #element_filter=UserRelatedElementFilter())
                           #element_filter=UserRelatedNFRElementFilter()
                           element_filter=None)
runner.precalculate(models, matrix_file_path=None, artifact_map_file_path=None)


# Calculate f1 and MAP for the FTLR +uct + mc +cd config on eTour.
# The f1 and MAP output is printed to console and to an excel file (in datasets/*PROJECT*/output/)
# Note: The FTLRUCT*Runners won't work with iTrust since iTrust doesn't have use case templates

FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]

runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)

# Output trace links for the given threshold combination
runner.output_trace_links(FINAL_THRESHOLDS,MAJORITY_THRESHOLDS, final=0.44, maj=0.59)

"""
DETERMINE THRESHOLD WITH BEST F1

Set FINAL_THRESHOLDS and MAJORITY_THRESHOLDS to an array of floats between 0 and 1.
This will calculate and print all f1 values of the given threshold combinations.
At the end of the console output / excel file, the highest f1 of all considered threshold combinations is printed.

Note 1: Don't create too large threshold arrays, this could get too time-consuming (especially on itrust)

FINAL_THRESHOLDS = Util.get_range_array(0.01, 0.99, 0.01)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS = Util.get_range_array(0.01, 0.99, 0.01)  # [0.53, 0.54, ..., 0.63]

runner = FTLRUCTMCCDRunner(Etour())
runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""

FINAL_THRESHOLDS = Util.get_range_array(0.01, 0.99, 0.01)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS = Util.get_range_array(0.01, 0.99, 0.01)  # [0.53, 0.54, ..., 0.63]

runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)

"""
To present the results in a precision recall curve use the following method
"""
runner.calculate_precision_recall_curve_csv()


