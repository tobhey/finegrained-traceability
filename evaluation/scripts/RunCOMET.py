from datasets.Dataset import Etour, Itrust, ItrustFull, Smos, Libest
import comet.CometDataReader as reader
from utility import Util

#result_file = "results_RQ1_iTrust_MAP.tm"
#result_file = "results_RQ1_SMOS_MAP.tm"
#result_file = "results_RQ1_eTour_MAP.tm"
result_file = "results_RQ1_LibEST_MAP.tm"
#dataset = ItrustFull()
#dataset = Etour(use_italian_solution_matrix=True)
#dataset = Smos(use_english_solution_matrix=True)
dataset = Libest()
#reader.eval_f1_comet_data(reader.COMET_DATA_FOLDER / result_file, dataset, Util.get_range_array(0.001, 0.999, 0.001))
#reader.calculate_comet_mean_avg_prec(reader.COMET_DATA_FOLDER / result_file, dataset, None)
reader.convert_comet_to_recall_prec_csv(reader.COMET_DATA_FOLDER / result_file, dataset, Util.get_range_array(0.0, 1.0, 0.001),"libest_comet_curve.csv")
