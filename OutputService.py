from abc import ABC , abstractmethod
from TraceLink import TraceLink

class OutputService(ABC):
    
    def __init__(self, evaluator):
        self._evaluator = evaluator
        
    @abstractmethod
    def process_trace_link_list(self, trace_links: [TraceLink]):
        pass
    @abstractmethod
    def process_trace_link_dict(self, trace_link_dict: dict[float, [TraceLink]]):
        """
        trace_link_dict: Dictionary with trace link lists as value
        """
        pass
    @abstractmethod
    def process_trace_link_2D_dict(self, trace_links: dict[dict[float, [TraceLink]]]):
        """
        trace_link_dict: Dictionary with dictionaries as values that have trace link lists as values
        """
        pass
    
    
class ExcelOutputService:
    def process_trace_link_list(self, trace_links: [TraceLink]):
        return self._evaluator.evaluate(trace_links)
    
    def process_trace_link_dict(self, trace_link_dict: dict):
        best_f1 = 0
        best_thresh = None
        for thresh in trace_link_dict:
            excel_row = self.process_trace_link_list(trace_link_dict[thresh])
    
    excel_array = []
        best_f1 = 0
        best_elem_thresh = None
        best_maj_thresh = None
        best_file_thresh = None