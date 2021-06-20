from SolutionTraceMatrix import SolutionTraceMatrix
import Util, logging

log = logging.getLogger(__name__)


class SolutionComparator:
    
    def __init__(self, solution_trace_matrix: SolutionTraceMatrix, print_false_positives=False, print_false_negatives=False):
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
        false_positives_matrix = SolutionTraceMatrix()
        for trace_link in trace_link_candidates:
            if sol_matrix_copy.contains_req_code_pair(trace_link.get_req_key(),
                                                              trace_link.get_code_key()):
                # Remove correct trace links on copy to avoid duplicate true positive count
                sol_matrix_copy.remove_trace_pair(trace_link.get_req_key(), trace_link.get_code_key())
                valid_trace_links.append(trace_link)
            elif self._print_false_positives:
                false_positives_matrix.add_trace_pair(trace_link.get_req_key(), trace_link.get_code_key())
        if self._print_false_negatives:
            self._print_false_negatives(sol_matrix_copy)
        if self._print_false_positives:
            log.info("\n\nFalse Positives: {} Links, {} unique Reqs, {} unique Code".format(false_positives_matrix._number_of_trace_links,
                                                                        false_positives_matrix.num_unique_reqs(), false_positives_matrix.num_unique_code()))
            log.info("\n" + false_positives_matrix.print_str())
            
        return valid_trace_links
    
    def _print_false_negatives(self, sol_matrix_with_false_negatives):
        log.info(f"\nFalse Negatives: {sol_matrix_with_false_negatives._number_of_trace_links} Links, {sol_matrix_with_false_negatives.num_unique_reqs()} unique Reqs, {sol_matrix_with_false_negatives.num_unique_code()} unique Code")
        log.info("\n" + sol_matrix_with_false_negatives.print_str())
