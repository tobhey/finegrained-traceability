from abc import ABC
import logging

from precalculating.TraceLinkDataStructure import FileLevelTraceLinkDataStructure, \
    ElementLevelTraceLinkDataStructure, TraceLinkDataStructure
from precalculating.TwoDimensionalMatrix import TwoDimensionalMatrix
from traceLinkProcessing.NeighborHandler import NeighborHandler
from traceLinkProcessing.TraceLink import TraceLink
from utility import Util

log = logging.getLogger(__name__)


class TraceLinkCreator(ABC):
    
    def __init__(self, trace_link_data_structure):
        self._trace_link_data_structure = trace_link_data_structure
    
    
class FileLevelTraceLinkCreator(TraceLinkCreator):

    def __init__(self, trace_link_data_structure):
        assert isinstance(trace_link_data_structure, FileLevelTraceLinkDataStructure)
        super().__init__(trace_link_data_structure)

    def process(self) -> [TraceLink]:
        all_trace_links = []
        for req_file_name in self._trace_link_data_structure.all_req_file_names():
            for code_file_name in self._trace_link_data_structure.all_code_file_names():
                similarity = self._trace_link_data_structure.similarity_between(req_file_name, code_file_name)
                all_trace_links.append(TraceLink(req_file_name, code_file_name, similarity))
        return all_trace_links


class MajorityDecisionTraceLinkCreator(TraceLinkCreator):
    
    def __init__(self, trace_link_data_structure, similarity_filter, req_reduce_func, code_reduce_function, callgraph_aggregator=None):
        assert isinstance(trace_link_data_structure, ElementLevelTraceLinkDataStructure)
        super().__init__(trace_link_data_structure)
        self._element_level_trace_link_aggregator = ElementLevelTraceLinkAggregator(req_reduce_func)
        self._callgraph_aggregator = callgraph_aggregator
        self._majority_decision = MajorityDecision(similarity_filter, code_reduce_function)
        
    def process(self, majority_drop_thresh) -> [TraceLink]:
        
        # Step 1: Calculate code element to (whole) req trace links
        trace_link_data_structure = self._element_level_trace_link_aggregator.process(Util.deep_copy(self._trace_link_data_structure))
        
        # Step 2: (optional) Update code element to (whole) req trace links according to call graph neighbors
        if self._callgraph_aggregator:
            trace_link_data_structure = self._callgraph_aggregator.process(trace_link_data_structure)
        # Step 3: Do majority decision to obtain (whole) code file to (whole) req similarities
        
        return self._majority_decision.process(trace_link_data_structure, majority_drop_thresh)

        
class ElementLevelTraceLinkAggregator:

    """
    Result: reqfile x code element matrix
    """

    def __init__(self, req_reduce_func):
        self._req_reduce_func = req_reduce_func

    def process(self, trace_link_data_structure) -> TraceLinkDataStructure:
        self._trace_link_data_structure = trace_link_data_structure
        method_to_req_similarities = TwoDimensionalMatrix.create_empty()
        for code_file_name in self._trace_link_data_structure.all_code_file_names():
            method_to_req_similarities = self._calculate_reduced_similarity_for_each_code_element(method_to_req_similarities, code_file_name)
        
        self._trace_link_data_structure.set_matrix(method_to_req_similarities)
        return self._trace_link_data_structure

    def _calculate_reduced_similarity_for_each_code_element(self, method_to_req_similarities, code_file_name):
        for code_element in self._trace_link_data_structure.all_code_elements_of(code_file_name):
            for req_file_name in self._trace_link_data_structure.all_req_file_names():
                code_element_to_req_similarity = self._calculate_reduced_similarity_to_all_reqs(code_element, req_file_name)
                method_to_req_similarities.set_value(req_file_name, code_element, code_element_to_req_similarity)
        return method_to_req_similarities
        
    def _calculate_reduced_similarity_to_all_reqs(self, code_element, req_file_name):
        all_element_level_similarities_to_same_req = []
        for req_element in self._trace_link_data_structure.req_elements_of(req_file_name):
            all_element_level_similarities_to_same_req.append(self._trace_link_data_structure.similarity_between(req_element, code_element))
        
        if not all_element_level_similarities_to_same_req:
            log.info(f"Skip: No similarities for {code_element}")
        return self._req_reduce_func(all_element_level_similarities_to_same_req)


class CallGraphTraceLinkAggregator:
    
    def __init__(self, method_weight, neighbor_strategy, method_call_graph_dict):
        self.method_call_graph_dict = method_call_graph_dict
        self.neighbor_handler = NeighborHandler(neighbor_strategy, method_call_graph_dict)
        self.method_weight = method_weight
            
    def process(self, trace_link_data_structure) -> TraceLinkDataStructure:
        self._trace_link_data_structure = trace_link_data_structure
        method_to_req_similarity_matrix_with_cg = self._trace_link_data_structure.get_copy_of_similarity_matrix()
        for method_key in self._trace_link_data_structure.all_method_keys():
            if not method_key in self.method_call_graph_dict:
                log.debug(f"SKIP: {method_key} is not in the call graph")
            else:
                all_neighbor_keys = self.neighbor_handler.get_neighbor_method_keys_of(method_key)
                for req_file_name in self._trace_link_data_structure.all_req_file_names():
                    weighted_similarity = self._weighted_simialrity_sum_of(method_key, all_neighbor_keys, req_file_name)
                    method_to_req_similarity_matrix_with_cg.set_value(req_file_name, method_key, weighted_similarity)
        self._trace_link_data_structure.set_matrix(method_to_req_similarity_matrix_with_cg)
        return self._trace_link_data_structure
    
    def _weighted_simialrity_sum_of(self, method_key, all_neighbor_keys, req_file_name):
        current_sim = self._trace_link_data_structure.similarity_between(req_file_name, method_key)
        neighbor_sims = []
        for neighbor_key in all_neighbor_keys:
            if not self._trace_link_data_structure.contains_method_key(neighbor_key):
                log.debug(f"The neighbor method {neighbor_key} is not in the precalculated trace link file. (Maybe it's not public)")
            else:
                neighbor_sims.append(self._trace_link_data_structure.similarity_between(req_file_name, neighbor_key))
        if not neighbor_sims:  # Method has no neighbors
            return current_sim

        neighbor_weight = (1 - self.method_weight)
        return self.method_weight * current_sim + neighbor_weight * sum(neighbor_sims) / len(neighbor_sims)


class MajorityDecision:

    def __init__(self, similarity_filter, code_reduce_function):
        self._similarity_filter = similarity_filter
        self._code_reduce_function = code_reduce_function
        
    def process(self, trace_link_data_structure, majority_drop_thresh) -> [TraceLink]:
        self._trace_link_data_structure = trace_link_data_structure
        # One majority decision per code file (assumption: one top level class per code file)
        resulting_trace_links = []
        for code_file_name in self._trace_link_data_structure.all_code_file_names():
            votes, sims_per_req = self._collect_votes_and_similarities(majority_drop_thresh, code_file_name)
            resulting_trace_links += self._do_majority_decision(code_file_name, votes, sims_per_req)
        return resulting_trace_links

    def _collect_votes_and_similarities(self, majority_drop_thresh, code_file_name):
        votes = []
        sims_per_req = MajorityDecision.AppendValueDict()
        for code_elem in self._trace_link_data_structure.all_code_elements_of(code_file_name):
            for req_file_name in self._trace_link_data_structure.all_req_file_names():
                similarity = self._trace_link_data_structure.similarity_between(req_file_name, code_elem)
                if (self._similarity_filter.is_more_similar(similarity, majority_drop_thresh)):
                    votes.append(req_file_name)
                    sims_per_req.append(req_file_name, similarity)
        return votes, sims_per_req
        
    def _do_majority_decision(self, code_file_name, votes, sims_per_req):
        voted_trace_links = []
        if votes:
            majority_ranked_dict, max_vote_count = Util.majority_count(votes)
            for req_file_name in majority_ranked_dict:
                if majority_ranked_dict[req_file_name] == max_vote_count:
                    code_file_to_req_file_similarity = self._code_reduce_function(sims_per_req[req_file_name])
                    voted_trace_links.append(TraceLink(req_file_name, code_file_name, code_file_to_req_file_similarity))
        
        else:
            log.debug(f"No votes for {code_file_name}")
        return voted_trace_links
    
    class AppendValueDict:
        """
        Value is a list of values.
        Append new values to a key with the append method.
        """

        def __init__(self):
            self._internal_dict = {}
    
        def append(self, key, value):
            if key in self._internal_dict:
                self._internal_dict[key].append(value)
            else:
                self._internal_dict[key] = [value]
    
        def __getitem__(self, key):
            return self._internal_dict[key]
        
        def __repr__(self):
            return self._internal_dict.__repr__()

