from Dataset import Etour
from TraceabilityBuilder import BaseLineRunner

b = BaseLineRunner(Etour())
b.precalculate()
