from utility.Util import calculate_cos_sim
from utility import Util
from _functools import partial

from scipy import spatial

from pyemd import emd
from gensim.corpora import Dictionary

from numpy import zeros,\
    double, sqrt,\
    sum as np_sum

import numpy as np
WMD_VALUE_MAP_FUNCTION = partial(Util.map_value_range, 0, 2)
class SimilarityComparator:

    def __init__(self, compare_two_vectors=True):
        self._dict_artefact_elements = {}
        self._dict_cosine = {}

        if compare_two_vectors:
           self._similarity_func = self._cosine_similarity
        else:
           self._similarity_func = self._wmd


    def calculate_similarity(self, representation_1, representation_2):
        return self._similarity_func(representation_1, representation_2)


    def _cosine_similarity(self, vector_1, vector_2):
        return calculate_cos_sim(vector_1, vector_2)


    def _wmd(self, boe_1, boe_2):
        sum = 0
        for vector_1 in boe_1:
            max = 0
            for vector_2 in boe_2:
                similarity = self._cosine_similarity(vector_1, vector_2)
                if(similarity > max):
                    max = similarity
            sum += max

        map_function = partial(Util.map_value_range, 0, len(boe_1))
        
        return map_function(sum)
