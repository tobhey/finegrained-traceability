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

    def __init__(self, compare_two_vectors=True, wikipedia2vec_model=None):
        self.wikipedia2vec_model = wikipedia2vec_model
        self._dict_artefact_elements = {}
        self._dict_cosine = {}

        if compare_two_vectors:
            if wikipedia2vec_model is not None:
                self._similarity_func = self._cos
            else:
                self._similarity_func = self._cosine_similarity
        else:
            if wikipedia2vec_model is not None:
                self._similarity_func = self._word_movers_distance
            else:
                self._similarity_func = self._wmd


    def calculate_similarity(self, representation_1, representation_2, code_element_key="", req_element_key=""):
        if self.wikipedia2vec_model is not None:
            return self._similarity_func(representation_1, representation_2, code_element_key, req_element_key)
        return self._similarity_func(representation_1, representation_2)


    def _cos(self, representation_1, representation_2, code_element_key, req_element_key):
        vector_req = []
        vector_code = []
        if req_element_key in self._dict_artefact_elements:
            vector_req = self._dict_artefact_elements[req_element_key]
        else:
            vector_req = self.wikipedia2vec_model.create_vector_embedding_req(representation_1, req_element_key)
            self._dict_artefact_elements[req_element_key] = vector_req
        
        if code_element_key in self._dict_artefact_elements:
            vector_code = self._dict_artefact_elements[code_element_key]
        else:
            vector_code = self.wikipedia2vec_model.create_vector_embedding_code(representation_2, code_element_key)
            self._dict_artefact_elements[code_element_key] = vector_code
        
        return 1 - spatial.distance.cosine(vector_req, vector_code)


    def _cosine_similarity(self, vector_1, vector_2):
        return calculate_cos_sim(vector_1, vector_2)

    
    def _word_movers_distance(self, str_list_1: [str], str_list_2: [str], code_element_key, req_element_key):
        document1 = []
        if req_element_key in self._dict_artefact_elements:
            document1 = self._dict_artefact_elements[req_element_key]
        else:
            document1 = self.wikipedia2vec_model.embedd_str_list_req(str_list_1, req_element_key)
            self._dict_artefact_elements[req_element_key] = document1

        document2 = []
        if code_element_key in self._dict_artefact_elements:
            document2 = self._dict_artefact_elements[code_element_key]
        else:
            document2 = self.wikipedia2vec_model.embedd_str_list_code(str_list_2, code_element_key)
            self._dict_artefact_elements[code_element_key] = document2

        dictionary = Dictionary(documents=[document1, document2])
        vocab_len = len(dictionary)

        distance_matrix = zeros((vocab_len, vocab_len), dtype=double)
        for i, t1 in dictionary.items():
            for j, t2 in dictionary.items():
                distance_matrix[i, j] = sqrt(np_sum((self.wikipedia2vec_model._dict_sense[t1] - self.wikipedia2vec_model._dict_sense[t2])**2))

        def nbow(document):
            d = zeros(vocab_len, dtype=double)
            nbow = dictionary.doc2bow(document)
            doc_len = len(document)
            for idx, freq in nbow:
                d[idx] = freq / float(doc_len) 
            return d

        d1 = nbow(document1)
        d2 = nbow(document2)

        return WMD_VALUE_MAP_FUNCTION(emd(d1, d2, distance_matrix))


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