import logging

from datasets.SolutionMatrix import SolutionMatrix
from utility import Util

log = logging.getLogger(__name__)


class SolutionComparator:
    
    def __init__(self, solution_trace_matrix: SolutionMatrix, print_false_positives=False, print_false_negatives=False):
        self._solution_trace_matrix = solution_trace_matrix
        self._print_false_positives = print_false_positives
        self._print_false_negatives = print_false_negatives
        
    def solution_size(self):
        return self._solution_trace_matrix.number_of_trace_links()

    def get_true_positives(self, trace_link_candidates):
        if not trace_link_candidates:
            log.debug("No Trace Link candidates!")
            return []
        valid_trace_links = []
        sol_matrix_copy = Util.deep_copy(self._solution_trace_matrix)
        false_positives_matrix = SolutionMatrix()
        for trace_link in trace_link_candidates:
            if sol_matrix_copy.contains_req_code_pair(trace_link.req_key,
                                                              trace_link.code_key):
                # Remove correct trace links on copy to avoid duplicate true positive count
                sol_matrix_copy.remove_trace_pair(trace_link.req_key, trace_link.code_key)
                valid_trace_links.append(trace_link)
            elif self._print_false_positives:
                false_positives_matrix.add_trace_pair(trace_link.req_key, trace_link.code_key)
        if self._print_false_negatives:
            self.print_false_negatives(sol_matrix_copy)
        if self._print_false_positives:
            log.info("\n\nFalse Positives: {} Links, {} unique Reqs, {} unique Code".format(false_positives_matrix._number_of_trace_links,
                                                                        false_positives_matrix.num_unique_reqs(), false_positives_matrix.num_unique_code()))
            log.info("\n" + false_positives_matrix.print_str())
            
        return valid_trace_links
    
    def get_similarity_relevance_dict(self, trace_links):
        """
        Returns a dict with the shape: req_dict["req_name"] = [(sim_to_code_1: float, relevant: bool), (sim_to_code_2, relevant), ...]
        This is used for the average precision calculation
        """
        req_dict = {}
        sol_matrix_copy = Util.deep_copy(self._solution_trace_matrix)  # Use copy to track false negatives and avoid duplicate trace links
        for trace_link in trace_links:
            req_name = trace_link.req_key
            code_name = trace_link.code_key
            sim_rel_tuple_to_add = (trace_link.similarity, False)
            if sol_matrix_copy.contains_req_code_pair(req_name, code_name):
                sim_rel_tuple_to_add = (trace_link.similarity, True)
                sol_matrix_copy.remove_trace_pair(req_name, code_name)
            if req_name in req_dict:
                req_dict[req_name].append(sim_rel_tuple_to_add)
            else:
                req_dict[req_name] = [sim_rel_tuple_to_add]
                
        if self._print_false_negatives:
            self.print_false_negatives(sol_matrix_copy)
            
        return req_dict

    @staticmethod
    def print_false_negatives(sol_matrix_with_false_negatives):
        log.info(f"\nFalse Negatives: {sol_matrix_with_false_negatives._number_of_trace_links} Links, {sol_matrix_with_false_negatives.num_unique_reqs()} unique Reqs, {sol_matrix_with_false_negatives.num_unique_code()} unique Code")
        log.info("\n" + sol_matrix_with_false_negatives.print_str())
