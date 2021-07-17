
from TraceabilityRunner import BaseLineRunner, BaseLineCDRunner, \
    BaseLineUCTRunner, BaseLineMCRunner, \
    BaseLineUCTMCRunner, BaseLineUCTCDRunner, BaseLineUCTMCCDRunner, \
    BaseLineMCCDRunner, OUTPUT_DIR
from datasets.Dataset import Etour, Itrust, Smos, Eanci
from utility import Util
from utility.FileUtil import setup_clear_dir

ENGLISH_FASTTEXT_MODEL_PATH = "/content/models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "/content/models/cc.it.300.bin"

FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]

setup_clear_dir(OUTPUT_DIR)

# Runs the FTLR +uct + mc +cd config on eTour. (see table II in the paper for the expected result)
# The f1 output is printed to console and to an excel file (in output/)
# The MAP is only printed to the console
# Note: The BaselineUCT*Runners wont work with iTrust since iTrust doesn't have use case templates

b = BaseLineUCTMCCDRunner(Etour())
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
b.calculate_map()

"""
DETERMINE THRESHOLD WITH BEST F1 EXAMPLE

For the data of table III of the paper, set FINAL_THRESHOLDS and MAJORITY_THRESHOLDS to an array of trace links.
This will calculate and print all f1 values of the given threshold combinations.
At the end of the console output / excel file, the highest f1 of all considered threshold combinations is printed.

Note 1: In the paper, we used interval deltas of 0.01
Note 2: Don't create too large threshold arrays, this could get too time-consuming (especially on itrust)

FINAL_THRESHOLDS = Util.get_range_array(0.4, 0.5, 0.01)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS = Util.get_range_array(0.53, 0.63, 0.01)  # [0.53, 0.54, ..., 0.63]

b = BaseLineMCCDRunner(Etour())
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""

"""
PRECALCULATION EXAMPLE

1. Modify ENGLISH_FASTTEXT_MODEL_PATH and ITALIAN_FASTTEXT_MODEL_PATH to point to your fastText model location
2. Run the code below
3. Caution: With matrix_file_path=None, artifact_map_file_path=None the output precalculated files will be written
            to their default location which will overwrite the existing files. Change the location to avoid this.

b = BaseLineRunner(Etour())
b.precalculate(ENGLISH_FASTTEXT_MODEL_PATH, matrix_file_path=None, artifact_map_file_path=None)

"""
