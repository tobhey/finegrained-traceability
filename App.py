import pandas

from Dataset import Etour, Itrust, Smos, EANCI
import PandasUtil
from TraceabilityBuilder import BaseLineRunner, BaseLineCDRunner, \
    BaseLineUCTRunner, BaseLineMCRunner, \
    BaseLineUCTMCRunner, BaseLineUCTCDRunner, BaseLineUCTMCCDRunner, \
    BaseLineMCCDRunner
import Util

ENGLISH_FASTTEXT_MODEL_PATH = "/content/drive/My Drive/models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "/content/drive/My Drive/models/cc.it.300.bin"

FINAL_THRESHOLDS = [0.44]  # Util.get_range_array(0.1, 0.8, 0.1)
MAJORITY_THRESHOLDS = [0.59]  # Util.get_range_array(0.3, 0.7, 0.1)

b = BaseLineRunner(Etour())
# b.precalculate(ENGLISH_FASTTEXT_MODEL_PATH)
b.calculate_map()
# b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""
b = BaseLineMCCDRunner(Itrust())
# b.precalculate(ENGLISH_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
""""""
b = BaseLineMCCDRunner(Smos())
# b.precalculate(ITALIAN_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)

b = BaseLineMCCDRunner(EANCI())
# b.precalculate(ITALIAN_FASTTEXT_MODEL_PATH)
b.calculate_map()
b.calculate_f1(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS)
"""
