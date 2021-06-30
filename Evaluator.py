from abc import ABC, abstractmethod

from TraceLink import TraceLink
import Util


class Evaluator(ABC):
    NO_TRACE_LINKS_MESSAGE = "No trace links found"
    NO_TRUE_POSITIVES_MESSAGE = "No true positives found"
    
    def __init__(self, solution_comparator):
        self._solution_comparator = solution_comparator
       
    @abstractmethod
    def evaluate(self, trace_links: [TraceLink]) -> "EvalResultObject":
        pass 
    
    
class F1Evaluator(Evaluator):
    
    def evaluate(self, trace_links: [TraceLink]) -> "EvalResultObject":
        if isinstance(trace_links, str):
            return EmptyResultObject(trace_links)
        
        total_num_found_links = len(trace_links)
        if total_num_found_links <= 0:
            return EmptyResultObject(self.NO_TRACE_LINKS_MESSAGE)
        
        num_true_positives = len(self._solution_comparator.get_true_positives(trace_links))
        if num_true_positives <= 0:
            return EmptyResultObject(self.NO_TRUE_POSITIVES_MESSAGE)
        
        precision, recall, f1 = Util.calc_prec_recall_f1(num_true_positives, total_num_found_links, self._solution_comparator.solution_size())
        return F1ResultObject(f1, precision, recall, total_num_found_links, num_true_positives)

    
class MAPEvaluator(Evaluator):
    """
    Calculate MAP@k. 
    1. Input: List of trace links
    2. For each requirement, build a code files list sorted by their similarity to this requirement
    3. Take the first k code files and throw the remainder away
    4. Calculate Average Precision @ k
    5. Sum all Average Precisions @ k of each requirement and normalize it by dividing through len(original_req_file_names)
    
    If k == None, it calculates the MAP over all retrieved code files (== leave out step 3.)
    
    If fully_connected = True: For comparability with related papers
        The list in step 2. contains all possible code files in related papers. This is not always the case in our approach, because
        we can't process broken code files.
        In the map calculation, the missing code files are replaced with placeholder with the lowest similarity to ensure comparability.
        For this, we need the original list of code files names.
    
    Note: We did not leave out any requirement files, so this is identical with the actual number of requirements.
    """

    def __init__(self, solution_comparator, original_req_file_names, original_code_file_names, fully_connected=True, bigger_is_more_similar=True, k=None):
        super().__init__(solution_comparator)
        self._original_req_file_names = original_req_file_names
        self._original_code_file_names = original_code_file_names
        self._k = k
        self._fully_connected = fully_connected
        self._bigger_is_more_similar = bigger_is_more_similar
    
    def evaluate(self, trace_links: [TraceLink]) -> "EvalResultObject":
        if isinstance(trace_links, str):
            return EmptyResultObject(trace_links)
        
        total_num_found_links = len(trace_links)
        if total_num_found_links <= 0:
            return EmptyResultObject(self.NO_TRACE_LINKS_MESSAGE)
    
        trace_links = list(dict.fromkeys(trace_links))  # Deterministic duplicate removal (set is not stable)
        
        if self._fully_connected and len(trace_links) < len(self._original_req_file_names) * len(self._original_code_file_names) :
            trace_links = self._fill_up_with_dummy_trace_links(trace_links)
            
        req_dict = self._solution_comparator.get_similarity_relevance_dict(trace_links)
        
        return self._calculate_mean_average_precision(req_dict)

    def _fill_up_with_dummy_trace_links(self, trace_links):
        # Add missing links as dummy links with lowest similarity 
        for req_name in self._original_req_file_names:
            for code_name in self._original_code_file_names:
                lowest_similarity = 0 if self._bigger_is_more_similar else 1
                dummy_trace_link = TraceLink(req_name, code_name, lowest_similarity)
                if not dummy_trace_link in trace_links:
                    trace_links.append(dummy_trace_link)
        return trace_links
        
    def _calculate_mean_average_precision(self, req_dict):
        """
        req_dict["req_name"] = [(sim_to_code_1: float, relevant: bool), (sim_to_code_2, relevant), ...]
        """
        precision_sum = 0
        for req in sorted(req_dict.keys()):
            first_k_links = sorted(list(req_dict[req]), key=lambda sim_rel_tuple: sim_rel_tuple[0], reverse=self._bigger_is_more_similar)  # most similar first
            if self._k is not None:
                first_k_links = first_k_links[:self._k]
            precision_sum += self._calculate_average_precision(first_k_links)
            
        return  precision_sum / len(self._original_req_file_names)

    def _calculate_average_precision(self, similarity_relevance_list):
        """
        Input: similarity_relevance_list = [(similarity, relevant)]
        reverse_compare = True if smaller sim == better
        
        """
        
        similarity_relevance_list.sort(key=lambda sim_rel_tuple: sim_rel_tuple[0], reverse=self._bigger_is_more_similar)  # most similar first
        prec_at_k = []
        relevant_links_at_k = 0
        for index, (sim, relevant) in enumerate(similarity_relevance_list):
            relevant_links_at_k += relevant
            prec_at_k.append(((relevant_links_at_k / (index + 1)), relevant, relevant_links_at_k))  # index + 1 == #retrieved links
            
        ap = 0
        prec_k_print_str = ""
        for index, (precK, relevant, rel_linksK) in  enumerate(prec_at_k):
            ap += precK * relevant
            prec_k_print_str += f"\nprec@{index+1}: {precK} with {rel_linksK} correct links"
        if relevant_links_at_k == 0:
            ap = 0
        else:
            ap /= relevant_links_at_k
            
        # print(prec_k_print_str)
        return ap


class EvalResultObject(ABC):
    
    @abstractmethod
    def get_print_str(self):
        pass
    
    @abstractmethod
    def is_greater_than(self, other):
        pass

    @abstractmethod
    def get_defining_value(self):
        pass


class EmptyResultObject():

    def __init__(self, message: str):
        self._message = message
        
    def get_print_str(self):
        return self._message
    
    def is_greater_than(self, other):
        return False
    
    def get_defining_value(self):
        return self._message


class F1ResultObject(EvalResultObject):
    
    def __init__(self, f1, precision, recall, num_found_links, num_true_positives):
        self.f1 = f1
        self.precision = precision
        self.recall = recall
        self.num_found_links = num_found_links
        self.num_true_positives = num_true_positives
        
    def get_print_str(self):
        return Util.build_prec_recall_f1_print_str(self.precision, self.recall, self.f1, self.num_true_positives, self.num_found_links)
    
    def is_greater_than(self, other):
        if isinstance(other, EmptyResultObject):
            return True
        return self.f1 > other.f1
    
    def get_defining_value(self):
        return self.f1


class FileLevelEvaluator(Evaluator):
    """
    Loeschen
    """

    def evaluate_file_level(self, file_level_trace_links: dict):
        file_level_metric_dict = {}
        for file_level_thresh in file_level_trace_links:
            file_level_metric_dict[file_level_thresh] = self.evaluate(file_level_trace_links[file_level_thresh])
        return file_level_metric_dict
        # print_str = Util.build_prec_recall_f1_print_str(precision, recall, f_1, true_positives, total_num_found_links, self._sol_matrix_size)


class MajorityEvaluator(Evaluator):

    def evaluate_maj_level(self, file_level_trace_links: dict):
        file_level_metric_dict = {}
        for file_level_thresh in file_level_trace_links:
            file_level_metric_dict[file_level_thresh] = self.evaluate(file_level_trace_links[file_level_thresh])
        return file_level_metric_dict
