import pandas

from Dataset import Etour, Itrust, Smos, EANCI
import PandasUtil
from TraceabilityBuilder import BaseLineRunner, BaseLineCDRunner, \
    BaseLineUCTRunner, BaseLineMCRunner, \
    BaseLineMCUCTRunner, BaseLineUCTCDRunner, BaseLineMCUCTCDRunner
import Util

F = [0.44]  # Util.get_range_array(0.1, 0.8, 0.1)
M = [0.59]  # Util.get_range_array(0.3, 0.7, 0.1)

b = BaseLineMCUCTRunner(Etour())
# b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)
""""""
b = BaseLineMCUCTRunner(Itrust())
# b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)

b = BaseLineMCUCTRunner(Smos())
# b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)

b = BaseLineMCUCTRunner(EANCI())
# b.precalculate()
b.calculate_map()
b.calculate_f1(F, M)
""""""
