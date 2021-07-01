from autograd.builtins import isinstance

from Dataset import   Smos, Itrust, Etour
from Evaluator import F1Evaluator, F1ResultObject, EvalResultObject, \
    MAPEvaluator
import Evaluator
import FileUtil, logging
from Paths import csv_recall_map_filename
import Paths
from SolutionComparator import SolutionComparator
from TraceLink import TraceLink
import Util

logging.basicConfig(level=logging.INFO,
                    handlers=[logging.FileHandler("log_output.log", mode='w'), logging.StreamHandler()])

log = logging.getLogger(__name__)


def eval_f1_moran_data(file_path, dataset, drop_threshs):
    trace_links = _extract_moran_trace_links(file_path)
    eval_result_list = _eval_moran_data_multiple_thresh(trace_links, dataset, drop_threshs)
    best = None
    best_thresh = None
    for eval_result_object, thresh in eval_result_list:
        log.info(f"\n drop_thresh={thresh}:\n" + eval_result_object.get_print_str() + "\n")
        if not best or eval_result_object.is_greater_than(best):
            best = eval_result_object
            best_thresh = thresh
    log.info(f"\nBest f1 at {best_thresh}: \n{best.get_defining_value()}")


def convert_moran_to_recall_prec_csv(file_path, dataset, drop_threshs, output_file_name):
    trace_links = _extract_moran_trace_links(file_path)
    eval_result_list = _eval_moran_data_multiple_thresh(trace_links, dataset, drop_threshs)
    recall_prec_dict = {}
    for eval_result_object, _ in eval_result_list:
        if  isinstance(eval_result_object, F1ResultObject):
            recall_prec_dict[eval_result_object.recall] = eval_result_object.precision
    
    FileUtil.write_recall_precision_csv(recall_prec_dict, output_file_name)

    
def calculate_moran_mean_avg_prec(file_path, dataset, k):
    """
    MAP@k
    """
    trace_links = _extract_moran_trace_links(file_path)
    map_result_object = MAPEvaluator(SolutionComparator(dataset.solution_matrix()), dataset.all_original_req_file_names(),
                                            dataset.all_original_code_file_names(), False, True, k).evaluate(trace_links)
    log.info(f"{map_result_object.get_print_str()}")
    
    
def _eval_moran_data_multiple_thresh(trace_links, dataset, drop_threshs) -> [(EvalResultObject, float)]:
    return [(_eval_moran_data_at_thresh(trace_links, dataset, thresh), thresh)  for thresh in drop_threshs]

        
def _eval_moran_data_at_thresh(trace_links, dataset, drop_thresh) -> EvalResultObject:
    trace_links = [link for link in trace_links if link.similarity > drop_thresh]
    return F1Evaluator(SolutionComparator(dataset.solution_matrix())).evaluate(trace_links)


def _extract_moran_trace_links(file_path):
    lines = FileUtil.read_textfile_into_lines_list(file_path)
    lines = lines[6:]  # first 6 lines contain no similarity data
    trace_links = []
    for line in lines:
        req, code, sim = line.split(" ")
        code = _remove_package(code)
        if code.endswith(".jsp") or  code.endswith(".txt"):
            continue
        sim = float(sim)
        trace_links.append(TraceLink(req, code, sim))
    return trace_links

            
def _remove_package(name):
    if name.count(".") > 1:
        parts = name.split(".")
        name = parts[-2] + "." + parts[-1]  # Last dot is for file extension
    return name

# calculate_moran_mean_avg_prec(Paths.ROOT / "Moran/Data/results_RQ1_eTour_MAP.tm", Etour(True), None) 
# calculate_moran_mean_avg_prec(Paths.ROOT / "Moran/Data/results_RQ1_SMOS_MAP.tm", Smos(True), None) 
# calculate_moran_mean_avg_prec(Paths.ROOT / "Moran/Data/results_RQ1_iTrust_MAP.tm", Itrust(), None)

# eval_f1_moran_data(Paths.ROOT / "Moran/Data/results_RQ1_iTrust_MAP.tm", Itrust(), Util.get_range_array(0.9, 1, 0.01))
