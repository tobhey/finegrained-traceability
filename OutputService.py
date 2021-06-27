from abc import ABC , abstractmethod
import logging
from typing import Dict, List
import FileUtil
from TraceLink import TraceLink

log = logging.getLogger(__name__)


class OutputService(ABC):
    
    def __init__(self, evaluator):
        self._evaluator = evaluator
        
    def _process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        """
        trace_link_dict: Dictionary with trace link lists as value
        """
        best_eval_result = None
        best_thresh = None
        
        print_str_dict = {}
        for file_level_thresh in trace_link_dict:
            # header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(file_level_thresh))
            eval_result_object = self._evaluator.evaluate(trace_link_dict[file_level_thresh])
            print_str_dict[file_level_thresh] = eval_result_object.get_print_str()
            
            if not best_eval_result or eval_result_object.is_greater_than(best_eval_result):
                best_eval_result = eval_result_object
                best_thresh = file_level_thresh
            
        return print_str_dict, best_eval_result, best_thresh
    
    def _process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        """
        trace_link_dict: Dictionary with dictionaries as values that have trace link lists as values
        """
        best_eval_result = None
        best_maj_thresh, best_file_level_thresh = None, None
        
        print_str_dict = {}
        for maj_thresh in trace_link_2D_dict:
            
            local_print_str_dict, best_local_eval_result, best_local_thresh = self._process_trace_link_dict(trace_link_2D_dict[maj_thresh])
            print_str_dict[maj_thresh] = local_print_str_dict
            
            if not best_eval_result or best_local_eval_result.is_greater_than(best_eval_result):
                best_eval_result = best_local_eval_result
                best_maj_thresh = maj_thresh
                best_file_level_thresh = best_local_thresh
                
        return print_str_dict, best_eval_result, best_file_level_thresh, best_maj_thresh

    
class F1ExcelOutputService(OutputService):
    FILE_LEVEL_DROP_THRESH_PATTERN = "file_level_drop_thresh: %d"
    MAJ_DROP_THRESH_PATTERN = "majority_drop_thresh: %d"
    BEST_F1_MESSAGE_PATTERN = "Best f1: %d at f%d"
    BEST_F1_2D_MESSAGE_PATTERN = "Best f1: %d at m%d f%d"
    NO_BEST_F1_MESSAGE = "No best F1"
    
    def __init__(self, evaluator, excel_output_file_path, also_print_log=True):
        super().__init__(evaluator)
        self._excel_output_file_path = excel_output_file_path
        self._also_print_log = also_print_log
        
    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        print_str_dict, best_eval_result, best_thresh = self._process_trace_link_dict(trace_link_dict)
        header_row = []  # Contains thresholds
        value_row = []  # Contains evaluated f1 metrics
        for file_level_thresh in sorted(print_str_dict.keys()):
            header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(file_level_thresh))
            value_row.append(print_str_dict[file_level_thresh])
            
            if self._also_print_log:
                log.info(f"f{file_level_thresh}\n"
                         f"{value_row[-1]}\n")
        
        excel_array = [header_row] + [value_row]
        excel_array.append([""])  # Add empty row as divider
        if best_eval_result.is_greater_than(0):
            excel_array = self._add_best_f1_excel_rows(excel_array, print_str_dict, best_eval_result, best_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])

        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
            
    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        print_str_dict, best_eval_result, best_file_level_thresh, best_maj_thresh = self._process_trace_link_2D_dict(trace_link_2D_dict)
        
        header_row = [""]  # First header cell is empty -> needed for header column
        header_row += [self.FILE_LEVEL_DROP_THRESH_PATTERN.format(file_level_thresh) for file_level_thresh in print_str_dict[best_maj_thresh].keys()]
        
        excel_array = [header_row]
        for maj_thresh in sorted(print_str_dict):
            next_row = [self.MAJ_DROP_THRESH_PATTERN.format(maj_thresh)]  # First cell is the maj thresh, followed by the evaluated f1 metrics for this maj thresh
            
            for file_level_thresh in sorted(print_str_dict[maj_thresh]):
                next_row.append([print_str_dict[maj_thresh][file_level_thresh]])
                
                if self._also_print_log:
                    log.info(f"m{maj_thresh} f{file_level_thresh}\n"
                             f"{next_row[-1]}\n")
                    
            excel_array.append(next_row)
            
        excel_array.append([""])  # Add empty row as divider
        if best_eval_result.is_greater_than(0):
            excel_array = self._add_best_f1_2D_excel_rows(excel_array, print_str_dict, best_eval_result, best_file_level_thresh, best_maj_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])
            
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
        
    def _add_best_f1_excel_rows(self, excel_array, print_str_dict, best_eval_result, best_thresh):
        best_f1_message = self.BEST_F1_MESSAGE_PATTERN.format(best_eval_result.get_defining_value(), best_thresh)
        excel_array.append([best_f1_message])
        if self._also_print_log:
            log.info(best_f1_message)
            
        # Create an excel row that additionally contains the right and left neighbor threshold of the best f1 threshold as context
        context_threshs = self._get_context_thresholds(print_str_dict.keys(), best_thresh)
        excel_array.append([self.FILE_LEVEL_DROP_THRESH_PATTERN.format(thresh) for thresh in context_threshs])
        excel_array.append([print_str_dict[thresh] for thresh in context_threshs])
        
        return excel_array
    
    def _add_best_f1_2D_excel_rows(self, excel_array, print_str_dict, best_eval_result, best_file_level_thresh, best_maj_thresh):
        best_f1_message = self.BEST_F1_2D_MESSAGE_PATTERN.format(best_eval_result.get_defining_value(), best_maj_thresh, best_file_level_thresh)
        excel_array.append([best_f1_message])
        if self._also_print_log:
            log.info(best_f1_message)
            
        # Create an 3x3 excel matrix that additionally contains the right and left neighbor threshold of the best f1 threshold as context
        file_level_context_threshs = self._get_context_thresholds(print_str_dict.keys(), best_file_level_thresh)
        excel_array.append([""] + [self.FILE_LEVEL_DROP_THRESH_PATTERN.format(thresh) for thresh in file_level_context_threshs])
        
        maj_context_threshs = self._get_context_thresholds(print_str_dict.keys(), best_maj_thresh)
        for maj_thresh in maj_context_threshs:
            next_row = [self.MAJ_DROP_THRESH_PATTERN.format(maj_thresh)]
            next_row += [print_str_dict[maj_thresh][thresh] for thresh in file_level_context_threshs]
            excel_array.append(next_row)
            
        return excel_array
        
    def _get_context_thresholds(self, all_threshs, best_thresh):
        
        if len(all_threshs) > 1:
            context_threshs = []
            all_threshs = sorted(all_threshs)
            best_idx = all_threshs.index(best_thresh)
            if best_idx - 1 >= 0:
                context_threshs.append(all_threshs[best_idx - 1])
            context_threshs.append(best_thresh)
            if best_idx + 1 < len(all_threshs):
                context_threshs.append(all_threshs[best_idx + 1])
            return context_threshs
        return [best_thresh]
        
