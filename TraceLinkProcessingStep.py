from abc import ABC
from collections import UserDict
from enum import Enum
import logging

from NeighborHandler import NeighborHandler
from Preprocessing import CallGraphUtil
from TraceLink import TraceLink
from TwoDimensionalMatrix import TwoDimensionalMatrix
import Util
from precalculating.AllTraceLinkCombinations import AllFileLevelTraceLinkCombinations, \
    TraceLinkProvider
from precalculating.ArtifactToElementMap import ArtifactToElementMap

log = logging.getLogger(__name__)


class TraceLinkProcessingStep(ABC):
    
    def __init__(self, trace_link_provider):
        self._trace_link_provider = trace_link_provider
    
    
class FileLevelTraceLinkCreator(TraceLinkProcessingStep):

    def __init__(self, all_trace_link_combinations):
        assert isinstance(all_trace_link_combinations, AllFileLevelTraceLinkCombinations)
        super(FileLevelTraceLinkCreator, self).__init__(all_trace_link_combinations)

    def process(self) -> [TraceLink]:
        all_trace_links = []
        for req_file_name in self._trace_link_provider.all_req_file_names:
            for code_file_name in self._trace_link_provider.all_code_file_names:
                similarity = self._trace_link_provider.similarity_between(req_file_name, code_file_name)
                all_trace_links.append(TraceLink(req_file_name, code_file_name, similarity))
        return all_trace_links


class ElementLevelTraceLinkAggregator(TraceLinkProcessingStep):

    def __init__(self, trace_link_provider, req_reduce_func):
        assert isinstance(trace_link_provider, TraceLinkProvider)
        super(ElementLevelTraceLinkAggregator, self).__init__(trace_link_provider)
        self._req_reduce_func = req_reduce_func

    def process(self) -> TraceLinkProvider:
        method_to_req_similarities = TwoDimensionalMatrix.create_empty()
        for code_file_name in self._trace_link_provider.all_code_file_names():
            method_to_req_similarities = self._calculate_reduced_similarity_for_code_element(method_to_req_similarities, self._trace_link_provider.all_code_elements_of(code_file_name))
        
        self._trace_link_provider.set_matrix(method_to_req_similarities)
        return self._trace_link_provider

    def _calculate_reduced_similarity_for_code_element(self, method_to_req_similarities, code_elements):
        for code_element in code_elements:
            for req_file_name in self._trace_link_provider.all_req_file_names():
                code_element_to_req_similarity = self._calculate_reduced_similarity_to_all_reqs(code_element, req_file_name)
                method_to_req_similarities.set_value(req_file_name, code_element, code_element_to_req_similarity)
        return method_to_req_similarities
        
    def _calculate_reduced_similarity_to_all_reqs(self, code_element, req_file_name):
        all_element_level_similarities_to_same_req = []
        for req_element in self._trace_link_provider.req_elements_of(req_file_name):
            all_element_level_similarities_to_same_req.append(self._trace_link_provider.similarity_between(req_element, code_element))
        
        if not all_element_level_similarities_to_same_req:
            log.info(f"Skip: No similarities for {code_element}")
        return self._req_reduce_func(all_element_level_similarities_to_same_req)


class CallGraphTraceLinkAggregator(TraceLinkProcessingStep):
    
    def __init__(self, trace_link_provider, method_weight, neighbor_strategy, method_call_graph_dict):
        super(CallGraphTraceLinkAggregator, self).__init__(trace_link_provider)
        self.method_call_graph_dict = method_call_graph_dict
        self.neighbor_handler = NeighborHandler(neighbor_strategy, method_call_graph_dict)
        self.method_weight = method_weight
            
    def process(self) -> TraceLinkProvider:
        method_to_req_similarity_matrix_with_cg = TwoDimensionalMatrix.create_empty()
        for method_key in self._trace_link_provider.all_method_keys():
            if not method_key in self.method_call_graph_dict:
                log.info(f"SKIP: {method_key} is not in the call graph")
            else:
                all_neighbor_keys = self.neighbor_handler.get_neighbor_method_keys_of(method_key)
                for req_file_name in self._trace_link_provider.all_req_file_names():
                    weighted_similarity = self._weighted_simialrity_sum_of(method_key, all_neighbor_keys, req_file_name)
                    method_to_req_similarity_matrix_with_cg.set_value(req_file_name, method_key, weighted_similarity)
        self._trace_link_provider.set_matrix(method_to_req_similarity_matrix_with_cg)
        return self._trace_link_provider
    
    def _weighted_simialrity_sum_of(self, method_key, all_neighbor_keys, req_file_name):
        current_sim = self._trace_link_provider.similarity_between(req_file_name, method_key)
        neighbor_sims = [self._trace_link_provider.similarity_between(req_file_name, neighbor_key) for neighbor_key in all_neighbor_keys]
        if not neighbor_sims:  # Method has no neighbors
            return current_sim
        neighbor_weight = (1 - self.method_weight)
        return self.method_weight * current_sim + neighbor_weight * sum(neighbor_sims) / len(neighbor_sims)


class MajorityDecision(TraceLinkProcessingStep):

    def __init__(self, trace_link_provider, similarity_filter, code_reduce_function):
        super(MajorityDecision, self).__init__(trace_link_provider)
        self._similarity_filter = similarity_filter
        self._code_reduce_function = code_reduce_function
        
    def process(self, majority_drop_thresh) -> [TraceLink]:
        # One majority decision per code file (assumption: one top level class per code file)
        resulting_trace_links = []
        for code_file_name in self._trace_link_provider.all_code_file_names:
            votes, sims_per_req = self._collect_votes_and_similarities(majority_drop_thresh, code_file_name)
            resulting_trace_links += self._do_majority_decision(code_file_name, votes, sims_per_req)
        return resulting_trace_links

    def _collect_votes_and_similarities(self, majority_drop_thresh, code_file_name):
        votes = []
        sims_per_req = MajorityDecision.AppendValueDict()
        for code_elem in self._trace_link_provider.all_code_elements_of(code_file_name):
            for req_file_name in self._trace_link_provider.all_req_file_names():
                similarity = self._trace_link_provider.similarity_between(req_file_name, code_elem)
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
                    code_file_to_req_file_similarity = self.code_reduce_func(sims_per_req[req_file_name])
                    # #candidate_link.add_req_candidate(sim, link_dict.get_req_emb(req_filename))
                    voted_trace_links.append(TraceLink(req_file_name, code_file_name, code_file_to_req_file_similarity))
        
        else:
            log.info(f"No votes for {code_file_name}")
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
