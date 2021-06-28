from abc import ABC, abstractmethod

from TraceLink import TraceLink
import Util


class Evaluator:
    NO_TRACE_LINKS_MESSAGE = "No trace links found"
    NO_TRUE_POSITIVES_MESSAGE = "No true positives found"
    
    def __init__(self, solution_comparator):
        self._solution_comparator = solution_comparator
        
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
