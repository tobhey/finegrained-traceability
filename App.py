
from TraceabilityRunner import BaseLineRunner, BaseLineCDRunner, \
    BaseLineUCTRunner, BaseLineMCRunner, \
    BaseLineUCTMCRunner, BaseLineUCTCDRunner, BaseLineUCTMCCDRunner, \
    BaseLineMCCDRunner, OUTPUT_DIR
from datasets.Dataset import Etour, Itrust, Smos, Eanci
from utility.FileUtil import setup_clear_dir

ENGLISH_FASTTEXT_MODEL_PATH = "/content/drive/My Drive/models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "/content/drive/My Drive/models/cc.it.300.bin"

FINAL_THRESHOLDS = [0.44]  # Util.get_range_array(0.1, 0.8, 0.1)
MAJORITY_THRESHOLDS = [0.59]  # Util.get_range_array(0.3, 0.7, 0.1)

# setup_clear_dir(OUTPUT_DIR)

b = BaseLineUCTMCRunner(Etour())
# b.precalculate(ENGLISH_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""
b = BaseLineMCRunner(Itrust())
# b.precalculate(ENGLISH_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""
b = BaseLineUCTMCRunner(Smos())
# b.precalculate(ITALIAN_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)

b = BaseLineUCTMCRunner(Eanci())
# b.precalculate(ITALIAN_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)

