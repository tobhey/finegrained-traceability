from abc import ABC
from typing import Dict, List
import logging

from autograd.builtins import isinstance

from datasets.Dataset import Dataset
from evaluation.Evaluator import F1Evaluator, F1ResultObject, MAPResultObject, MAPEvaluator, LagEvaluator, \
    LagResultObject
from evaluation.SolutionComparator import SolutionComparator
from traceLinkProcessing.TraceLink import TraceLink
from utility import FileUtil

log = logging.getLogger(__name__)


class OutputService(ABC):

    def __init__(self, evaluator):
        self._evaluator = evaluator

    
class F1ExcelOutputService(OutputService):
    FILE_LEVEL_DROP_THRESH_PATTERN = "final_threshold: {}"
    MAJ_DROP_THRESH_PATTERN = "majority_threshold: {}"
    BEST_F1_MESSAGE_PATTERN = "\nBest f1 at f{}:\n{}"
    BEST_F1_2D_MESSAGE_PATTERN = "\nBest f1 at m{} f{}:\n{}"
    NO_BEST_F1_MESSAGE = "No best F1"
    
    def __init__(self, dataset, excel_output_file_path, also_print_eval=True):
        super().__init__(F1Evaluator(SolutionComparator(dataset.solution_matrix())))
        self._excel_output_file_path = excel_output_file_path
        self._also_print_eval = also_print_eval
        
    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        print_str_dict, best_eval_result, best_thresh = self._process_trace_link_dict(trace_link_dict)
        header_row = []  # Contains thresholds
        value_row = []  # Contains evaluated f1 metrics
        for final_threshold in sorted(print_str_dict.keys()):
            header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold))
            value_row.append(print_str_dict[final_threshold])
            
            if self._also_print_eval:
                log.info(f"\nf{final_threshold}\n"
                         f"{value_row[-1]}")
        
        excel_array = [header_row] + [value_row]
        excel_array.append([""])  # Add empty row as divider
        if isinstance(best_eval_result, F1ResultObject):
            excel_array = self._add_best_f1_excel_rows(excel_array, print_str_dict, best_eval_result, best_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])

        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
            
    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh = self._process_trace_link_2D_dict(trace_link_2D_dict)
        
        header_row = [""]  # First header cell is empty -> needed for header column
        header_row += [self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold) for final_threshold in sorted(print_str_dict[best_maj_thresh].keys())]
        
        excel_array = [header_row]
        for maj_thresh in sorted(print_str_dict):
            next_row = [self.MAJ_DROP_THRESH_PATTERN.format(maj_thresh)]  # First cell is the maj thresh, followed by the evaluated f1 metrics for this maj thresh
            
            for final_threshold in sorted(print_str_dict[maj_thresh]):
                next_row.append(print_str_dict[maj_thresh][final_threshold])
                
                if self._also_print_eval:
                    log.info(f"\nm{maj_thresh} f{final_threshold}\n"
                             f"{next_row[-1]}")
                    
            excel_array.append(next_row)
            
        excel_array.append([""])  # Add empty row as divider
        if isinstance(best_eval_result, F1ResultObject):
            excel_array = self._add_best_f1_2D_excel_rows(excel_array, print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])
            
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
    
    def _process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        """
        trace_link_dict: Dictionary with trace link lists as value
        """
        best_eval_result = None
        best_thresh = None
        
        print_str_dict = {}
        for final_threshold in trace_link_dict:
            # header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold))
            eval_result_object = self._evaluator.evaluate(trace_link_dict[final_threshold])
            print_str_dict[final_threshold] = eval_result_object.get_print_str()
            
            if not best_eval_result or eval_result_object.is_greater_than(best_eval_result):
                best_eval_result = eval_result_object
                best_thresh = final_threshold
            
        return print_str_dict, best_eval_result, best_thresh
    
    def _process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        """
        trace_link_dict: Dictionary with dictionaries as values that have trace link lists as values
        """
        best_eval_result = None
        best_maj_thresh, best_final_threshold = None, None
        
        print_str_dict = {}
        for maj_thresh in trace_link_2D_dict:
            
            local_print_str_dict, best_local_eval_result, best_local_thresh = self._process_trace_link_dict(trace_link_2D_dict[maj_thresh])
            print_str_dict[maj_thresh] = local_print_str_dict
            
            if not best_eval_result or best_local_eval_result.is_greater_than(best_eval_result):
                best_eval_result = best_local_eval_result
                best_maj_thresh = maj_thresh
                best_final_threshold = best_local_thresh
                
        return print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh
    
    def _add_best_f1_excel_rows(self, excel_array, print_str_dict, best_eval_result, best_thresh):
        best_f1_message = self.BEST_F1_MESSAGE_PATTERN.format(best_thresh, best_eval_result.get_print_str())
        excel_array.append([best_f1_message])
        log.info(best_f1_message + "\n")
            
        # Create an excel row that additionally contains the right and left neighbor threshold of the best f1 threshold as context
        context_threshs = self._get_context_thresholds(print_str_dict.keys(), best_thresh)
        excel_array.append([self.FILE_LEVEL_DROP_THRESH_PATTERN.format(thresh) for thresh in context_threshs])
        excel_array.append([print_str_dict[thresh] for thresh in context_threshs])
        
        return excel_array
    
    def _add_best_f1_2D_excel_rows(self, excel_array, print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh):
        best_f1_message = self.BEST_F1_2D_MESSAGE_PATTERN.format(best_maj_thresh, best_final_threshold, best_eval_result.get_print_str())
        excel_array.append([best_f1_message])
        log.info(best_f1_message + "\n")
            
        # Create an 3x3 excel matrix that additionally contains the right and left neighbor threshold of the best f1 threshold as context
        file_level_context_threshs = self._get_context_thresholds(list(print_str_dict.values())[0].keys(), best_final_threshold)
        excel_array.append([""] + [self.FILE_LEVEL_DROP_THRESH_PATTERN.format(thresh) for thresh in file_level_context_threshs])
        
        maj_context_threshs = self._get_context_thresholds(print_str_dict.keys(), best_maj_thresh)
        for maj_thresh in maj_context_threshs:
            next_row = [self.MAJ_DROP_THRESH_PATTERN.format(maj_thresh)]
            next_row += [print_str_dict[maj_thresh][thresh] for thresh in file_level_context_threshs]
            excel_array.append(next_row)
            
        return excel_array
        
    @staticmethod
    def _get_context_thresholds(all_threshs, best_thresh):
        
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
        

class MAPOutputService(OutputService):
    
    def __init__(self, dataset: Dataset, fully_connected, bigger_is_more_similar, k):
        super().__init__(MAPEvaluator(SolutionComparator(dataset.solution_matrix()), dataset.all_original_req_file_names(),
                                      dataset.all_original_code_file_names(), fully_connected, bigger_is_more_similar, k))

    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        for thresh in trace_link_dict:
            map_result_object = self._evaluator.evaluate(trace_link_dict[thresh])
            log.info(f"\nfinal_threshold={thresh}: {map_result_object.get_print_str()}")
            
    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        for maj_thresh in trace_link_2D_dict:
            for file_thresh in trace_link_2D_dict[maj_thresh]:
                map_result_object = self._evaluator.evaluate(trace_link_2D_dict[maj_thresh][file_thresh])
                log.info(f"\nfinal_threshold={file_thresh}, maj_thresh={maj_thresh}: {map_result_object.get_print_str()}")


class LagOutputService(OutputService):

    def __init__(self, dataset: Dataset, fully_connected, bigger_is_more_similar):
        super().__init__(
            LagEvaluator(SolutionComparator(dataset.solution_matrix()), dataset.all_original_req_file_names(),
                         dataset.all_original_code_file_names(), fully_connected, bigger_is_more_similar))

    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        for thresh in trace_link_dict:
            lag_result_object = self._evaluator.evaluate(trace_link_dict[thresh])
            log.info(f"\nfinal_threshold={thresh}: {lag_result_object.get_print_str()}")

    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        for maj_thresh in trace_link_2D_dict:
            for file_thresh in trace_link_2D_dict[maj_thresh]:
                lag_result_object = self._evaluator.evaluate(trace_link_2D_dict[maj_thresh][file_thresh])
                log.info(
                    f"\nfinal_threshold={file_thresh}, maj_thresh={maj_thresh}: {lag_result_object.get_print_str()}")
                
class MAPExcelOutputService(MAPOutputService):
    
    def __init__(self, dataset: Dataset, fully_connected, bigger_is_more_similar, k, excel_output_file_path):
        super().__init__(dataset, fully_connected, bigger_is_more_similar, k)
        self._excel_output_file_path = excel_output_file_path

    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        excel_array = [[]]
        for thresh in trace_link_dict:
            map_result_object = self._evaluator.evaluate(trace_link_dict[thresh])
            log.info(f"\nfinal_threshold={thresh}: {map_result_object.get_print_str()}")  
            excel_array.append(["final_threshold={}:".format(thresh), map_result_object.get_print_str()])
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
            
    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        excel_array = [[]]
        for maj_thresh in trace_link_2D_dict:
            for file_thresh in trace_link_2D_dict[maj_thresh]:
                map_result_object = self._evaluator.evaluate(trace_link_2D_dict[maj_thresh][file_thresh])
                log.info(f"\nfinal_threshold={file_thresh}, maj_thresh={maj_thresh}: {map_result_object.get_print_str()}")
                excel_array.append(["final_threshold={}, maj_thresh={}".format(file_thresh, maj_thresh), map_result_object.get_print_str()])
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
        
class CombinedExcelOutputService(F1ExcelOutputService):
    DEFAULT_F1_MESSAGE_PATTERN = "\nDefault f1 at f{}:\n{}"
    DEFAULT_F1_2D_MESSAGE_PATTERN = "\nDefault f1 at m{}, f{}:\n{}"
    MAP_MESSAGE_PATTERN = "\nMAP at final_threshold={}:\n{}"
    MAP_2D_MESSAGE_PATTERN = "\nMAP at final_threshold={}, majority_threshold={}:\n{}"
    LAG_MESSAGE_PATTERN = "\nLAG at final_threshold={}:\n{}"
    LAG_2D_MESSAGE_PATTERN = "\nLAG at final_threshold={}, majority_threshold={}:\n{}"
    
    def __init__(self, dataset, excel_output_file_path, default_final, default_maj, fully_connected, bigger_is_more_similar, k, also_print_eval=True):
        super().__init__(dataset, excel_output_file_path, also_print_eval)

        self._excel_output_file_path = excel_output_file_path
        self._also_print_eval = also_print_eval
        self._default_final_threshold = default_final
        self._default_majority_threshold = default_maj
        self._bigger_is_more_similar = bigger_is_more_similar
        self._map_evaluator = MAPEvaluator(SolutionComparator(dataset.solution_matrix()), dataset.all_original_req_file_names(),
                                      dataset.all_original_code_file_names(), fully_connected, bigger_is_more_similar, k)
        self._lag_evaluator = LagEvaluator(SolutionComparator(dataset.solution_matrix()), dataset.all_original_req_file_names(),
                                      dataset.all_original_code_file_names(), fully_connected, bigger_is_more_similar)
        
    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]]):
        print_str_dict, best_eval_result, best_thresh = self._process_trace_link_dict(trace_link_dict)
        header_row = []  # Contains thresholds
        value_row = []  # Contains evaluated f1 metrics
        for final_threshold in sorted(print_str_dict.keys()):
            header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold))
            value_row.append(print_str_dict[final_threshold])
            
            if self._also_print_eval:
                log.info(f"\nf{final_threshold}\n"
                         f"{value_row[-1]}")
        
        excel_array = [header_row] + [value_row]
        excel_array.append([""])  # Add empty row as divider
        if isinstance(best_eval_result, F1ResultObject):
            excel_array = self._add_best_f1_excel_rows(excel_array, print_str_dict, best_eval_result, best_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])
            
        excel_array.append([""])  # Add empty row as divider
        default_eval_result = self._evaluator.evaluate(trace_link_dict[self._default_final_threshold])
        default_f1_message = self.DEFAULT_F1_MESSAGE_PATTERN.format(self._default_final_threshold, print_str_dict[self._default_final_threshold])
        if self._also_print_eval:
            log.info(default_f1_message + "\n")
        excel_array.append([default_f1_message])
        
        excel_array.append([""])  # Add empty row as divider
        thresh = 1
        if self._bigger_is_more_similar:
            thresh = 0
        map_result_object = self._map_evaluator.evaluate(trace_link_dict[thresh])
        map_message = self.MAP_MESSAGE_PATTERN.format(thresh, map_result_object.get_print_str())
        log.info(map_message + "\n")  
        excel_array.append([map_message])

        excel_array.append([""])  # Add empty row as divider
        thresh = 1
        if self._bigger_is_more_similar:
            thresh = 0
        lag_result_object = self._lag_evaluator.evaluate(trace_link_dict[thresh])
        lag_message = self.LAG_MESSAGE_PATTERN.format(thresh, lag_result_object.get_print_str())
        log.info(lag_message + "\n")
        excel_array.append([lag_message])
        
        excel_array.append([""])  # Add empty row as divider
        csv_array = [["Best:", "", "", "", "", "Default:", "", "", "MAP", "LAG"],
                     ["Maj", "Final", "Precision", "Recall", "F1", "Precision", "Recall", "F1", "MAP", "LAG"]]
        if isinstance(best_eval_result, F1ResultObject):
            default_prec = 0
            default_rec = 0
            default_f1 = 0
            map_value = 0
            lag_value = 0
            if isinstance(default_eval_result, F1ResultObject):
                default_prec = default_eval_result.get_precision()
                default_rec = default_eval_result.get_recall()
                default_f1 = default_eval_result.get_defining_value()
            if isinstance(map_result_object, MAPResultObject):
                map_value = map_result_object.get_defining_value()
            if isinstance(lag_result_object, LagResultObject):
                lag_value = lag_result_object.get_defining_value()
            csv_array.append([-1, best_thresh, best_eval_result.get_precision(), best_eval_result.get_recall(), best_eval_result.get_defining_value(), default_prec, default_rec, default_f1, map_value, lag_value])
        excel_array.extend(csv_array)
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
        csv_path = self._excel_output_file_path.with_suffix('.csv')
        FileUtil.write_eval_to_csv(csv_array, csv_path)
            
    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh = self._process_trace_link_2D_dict(trace_link_2D_dict)
        
        header_row = [""]  # First header cell is empty -> needed for header column
        header_row += [self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold) for final_threshold in print_str_dict[best_maj_thresh].keys()]
        
        excel_array = [header_row]
        for maj_thresh in sorted(print_str_dict):
            next_row = [self.MAJ_DROP_THRESH_PATTERN.format(maj_thresh)]  # First cell is the maj thresh, followed by the evaluated f1 metrics for this maj thresh
            
            for final_threshold in sorted(print_str_dict[maj_thresh]):
                next_row.append(print_str_dict[maj_thresh][final_threshold])
                
                if self._also_print_eval:
                    log.info(f"\nm{maj_thresh} f{final_threshold}\n"
                             f"{next_row[-1]}")
                    
            excel_array.append(next_row)
            
        excel_array.append([""])  # Add empty row as divider
        if isinstance(best_eval_result, F1ResultObject):
            excel_array = self._add_best_f1_2D_excel_rows(excel_array, print_str_dict, best_eval_result, best_final_threshold, best_maj_thresh)
        else:
            excel_array.append([self.NO_BEST_F1_MESSAGE])
            
        excel_array.append([""])  # Add empty row as divider
        default_eval_result = self._evaluator.evaluate(trace_link_2D_dict[self._default_majority_threshold][self._default_final_threshold])
        default_f1_message = self.DEFAULT_F1_2D_MESSAGE_PATTERN.format(self._default_majority_threshold, self._default_final_threshold, print_str_dict[self._default_majority_threshold][self._default_final_threshold])
        log.info(default_f1_message + "\n")
        excel_array.append([default_f1_message])
        
        excel_array.append([""])  # Add empty row as divider
        thresh = 1
        if self._bigger_is_more_similar:
            thresh = 0
        map_result_object = self._map_evaluator.evaluate(trace_link_2D_dict[thresh][thresh])
        map_message = self.MAP_2D_MESSAGE_PATTERN.format(thresh,thresh, map_result_object.get_print_str())
        log.info(map_message + "\n")
        excel_array.append([map_message])

        excel_array.append([""])  # Add empty row as divider
        thresh = 1
        if self._bigger_is_more_similar:
            thresh = 0
        lag_result_object = self._lag_evaluator.evaluate(trace_link_2D_dict[thresh][thresh])
        lag_message = self.LAG_2D_MESSAGE_PATTERN.format(thresh, thresh, lag_result_object.get_print_str())
        log.info(lag_message + "\n")
        excel_array.append([lag_message])

        excel_array.append([""])  # Add empty row as divider
        csv_array = [["Best:", "", "", "", "", "Default:", "", "", "MAP", "LAG"],
                     ["Maj", "Final", "Precision", "Recall", "F1", "Precision", "Recall", "F1", "MAP", "LAG"]]
        if isinstance(best_eval_result, F1ResultObject):
            default_prec = 0
            default_rec = 0
            default_f1 = 0
            map_value = 0
            lag_value = 0
            if isinstance(default_eval_result, F1ResultObject):
                default_prec = default_eval_result.get_precision()
                default_rec = default_eval_result.get_recall()
                default_f1 = default_eval_result.get_defining_value()
            if isinstance(map_result_object, MAPResultObject):
                map_value = map_result_object.get_defining_value()
            if isinstance(lag_result_object, LagResultObject):
                lag_value = lag_result_object.get_defining_value()
            csv_array.append([best_maj_thresh, best_final_threshold, best_eval_result.get_precision(), best_eval_result.get_recall(), best_eval_result.get_defining_value(), default_prec, default_rec, default_f1, map_value, lag_value])
        excel_array.extend(csv_array)
        FileUtil.write_eval_to_excel(excel_array, self._excel_output_file_path)
        csv_path = self._excel_output_file_path.with_suffix('.csv')
        FileUtil.write_eval_to_csv(csv_array, csv_path)
            

class PrecisionRecallPairOutputService(F1ExcelOutputService):


    def __init__(self, dataset, excel_output_file_path, also_print_eval=True):
        super().__init__(dataset, excel_output_file_path, also_print_eval)

        self._excel_output_file_path = excel_output_file_path
        self._also_print_eval = also_print_eval

    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]]):
        results = {}
        for maj_thresh in trace_link_2D_dict:
            for final_threshold in trace_link_2D_dict[maj_thresh]:
                # header_row.append(self.FILE_LEVEL_DROP_THRESH_PATTERN.format(final_threshold))
                eval_result_object = self._evaluator.evaluate(trace_link_2D_dict[maj_thresh][final_threshold])
                if isinstance(eval_result_object, F1ResultObject):
                    recall = eval_result_object.get_recall()
                    prec = eval_result_object.get_precision()
                    if recall == 0 and prec == 0:
                        continue
                    if recall in results:
                        if results[recall] > prec:
                            continue
                    results[recall] = prec

        FileUtil.write_recall_precision_csv(results, self._excel_output_file_path)


class TracelinkOutputService(ABC):

    def __init__(self, output_file_path):
        self._output_file_path = output_file_path

    def process_trace_link_dict(self, trace_link_dict: Dict[float, List[TraceLink]], threshold):
        rows = [["sourceID", "targetID"]]
        if threshold in trace_link_dict.keys():
            for tracelink in trace_link_dict[threshold]:
                if not isinstance(tracelink, str):
                    rows.append([tracelink.req_key, tracelink.code_key])
        else:
            log.error("specified threshold not in results!")

        FileUtil.write_rows_to_csv_file(self._output_file_path, rows)

    def process_trace_link_2D_dict(self, trace_link_2D_dict: Dict[float, Dict[float, List[TraceLink]]], maj_threshold, final_threshold):
        rows = [["sourceID", "targetID", "maj_threshold={} final_threshold={}".format(maj_threshold,final_threshold)]]
        if maj_threshold in trace_link_2D_dict.keys():
            trace_link_dict = trace_link_2D_dict[maj_threshold]
            if final_threshold in trace_link_dict.keys():
                for tracelink in trace_link_dict[final_threshold]:
                    if not isinstance(tracelink,str):
                        rows.append([tracelink.req_key, tracelink.code_key])
            else:
                log.error("specified final threshold not in results!")
        else:
            log.error("specified majority threshold not in results!")

        FileUtil.write_rows_to_csv_file(self._output_file_path, rows)
