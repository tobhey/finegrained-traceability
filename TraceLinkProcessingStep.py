from abc import ABC
from enum import Enum
import logging

from NeighborHandler import NeighborHandler
from Preprocessing import CallGraphUtil
from TraceLink import TraceLink
from TwoDimensionalMatrix import TwoDimensionalMatrix
from precalculating.AllTraceLinkCombinations import AllFileLevelTraceLinkCombinations, \
    AllElementLevelTraceLinkCombinations
from precalculating.ArtifactToElementMap import ArtifactToElementMap

log = logging.getLogger(__name__)


class TraceLinkProcessingStep(ABC):
    
    def __init__(self, all_trace_link_combinations):
        self._all_trace_link_combinations = all_trace_link_combinations
    
    
class FileLevelTraceLinkCreator(TraceLinkProcessingStep):

    def __init__(self, all_trace_link_combinations):
        assert isinstance(all_trace_link_combinations, AllFileLevelTraceLinkCombinations)
        super(FileLevelTraceLinkCreator, self).__init__(all_trace_link_combinations)

    def process(self) -> [TraceLink]:
        all_trace_links = []
        for req_file_name in self._all_trace_link_combinations.all_req_file_names:
            for code_file_name in self._all_trace_link_combinations.all_code_file_names:
                similarity = self._all_trace_link_combinations.similarity_between(req_file_name, code_file_name)
                all_trace_links.append(TraceLink(req_file_name, code_file_name, similarity))
        return all_trace_links


class ElementLevelTraceLinkAggregator(TraceLinkProcessingStep):

    def __init__(self, all_trace_link_combinations, req_reduce_func):
        assert isinstance(all_trace_link_combinations, AllElementLevelTraceLinkCombinations)
        super(ElementLevelTraceLinkAggregator, self).__init__(all_trace_link_combinations)
        self._req_reduce_func = req_reduce_func

    def process(self) -> (TwoDimensionalMatrix, ArtifactToElementMap):
        method_to_req_similarities = TwoDimensionalMatrix.create_empty()
        for code_file_name in self._all_trace_link_combinations.all_code_file_names():
            method_to_req_similarities = self._calculate_reduced_similarity_for_code_element(method_to_req_similarities, self._all_trace_link_combinations.all_code_elements_of(code_file_name))
        
        return method_to_req_similarities, self._all_trace_link_combinations._artifact_to_element_map

    def _calculate_reduced_similarity_for_code_element(self, method_to_req_similarities, code_elements):
        for code_element in code_elements:
            for req_file_name in self._all_trace_link_combinations.all_req_file_names():
                code_element_to_req_similarity = self._calculate_reduced_similarity_to_all_reqs(code_element, req_file_name)
                method_to_req_similarities.set_value(req_file_name, code_element, code_element_to_req_similarity)
        return method_to_req_similarities
        
    def _calculate_reduced_similarity_to_all_reqs(self, code_element, req_file_name):
        all_element_level_similarities_to_same_req = []
        for req_element in self._all_trace_link_combinations.req_elements_of(req_file_name):
            all_element_level_similarities_to_same_req.append(self._all_trace_link_combinations.similarity_between(req_element, code_element))
        
        if not all_element_level_similarities_to_same_req:
            log.info(f"Skip: No similarities for {code_element}")
        return self._req_reduce_func(all_element_level_similarities_to_same_req)


class CallGraphTraceLinkAggregator:
    
    def __init__(self, method_weight, neighbor_strategy, method_call_graph_dict, code_elem_to_req_similarity_matrix, artifact_to_element_map):
        self._code_elem_to_req_similarity_matrix = code_elem_to_req_similarity_matrix
        self._artifact_to_element_map = artifact_to_element_map
        self.method_call_graph_dict = method_call_graph_dict
        self.neighbor_handler = NeighborHandler(neighbor_strategy, method_call_graph_dict)
        self.method_weight = method_weight
            
    def process(self) -> (TwoDimensionalMatrix, ArtifactToElementMap):
        method_to_req_similarity_matrix_with_cg = TwoDimensionalMatrix.create_empty(self._code_elem_to_req_similarity_matrix.get_row_names(), self._code_elem_to_req_similarity_matrix.get_column_names())
        for method_key in self._artifact_to_element_map.all_method_keys():
            if not method_key in self.method_call_graph_dict:
                log.info(f"SKIP: {method_key} is not in the call graph")
            else:
                all_neighbor_keys = self.neighbor_handler.get_neighbor_method_keys_of(method_key)
                for req_file_name in self._artifact_to_element_map.all_req_file_names():
                    weighted_similarity = self._weighted_simialrity_sum_of(method_key, all_neighbor_keys, req_file_name)
                    method_to_req_similarity_matrix_with_cg.set_value(req_file_name, method_key, weighted_similarity)
        return method_to_req_similarity_matrix_with_cg, self._artifact_to_element_map
    
    def _weighted_simialrity_sum_of(self, method_key, all_neighbor_keys, req_file_name):
        current_sim = self._code_elem_to_req_similarity_matrix.get_value(req_file_name, method_key)
        neighbor_sims = [self._code_elem_to_req_similarity_matrix.get_value(req_file_name, neighbor_key) for neighbor_key in all_neighbor_keys]
        if not neighbor_sims:  # Method has no neighbors
            return current_sim
        neighbor_weight = (1 - self.method_weight)
        return self.method_weight * current_sim + neighbor_weight * sum(neighbor_sims) / len(neighbor_sims)
