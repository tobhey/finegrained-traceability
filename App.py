import pandas

from Dataset import Etour, Itrust, Smos, EANCI
import PandasUtil
from TraceabilityBuilder import BaseLineRunner, BaseLineCDRunner, \
    BaseLineUCTRunner, BaseLineMCRunner
import Util

F = [0.44]  # Util.get_range_array(0.1, 0.8, 0.1)
M = [0.59]  # Util.get_range_array(0.3, 0.7, 0.1)

b = BaseLineUCTRunner(Etour())
b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)
"""
b = BaseLineUCTRunner(Itrust())
# b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)
"""
b = BaseLineUCTRunner(Smos())
b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)

b = BaseLineUCTRunner(EANCI())
b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)
""""""
