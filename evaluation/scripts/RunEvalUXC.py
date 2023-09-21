
from TraceabilityRunner import FTLRRunner, FTLRCDRunner, \
    FTLRUCTRunner, FTLRMCRunner, \
    FTLRUCTMCRunner, FTLRUCTCDRunner, FTLRUCTMCCDRunner, \
    FTLRMCCDRunner, OUTPUT_DIR, FTLRUCTMCCDCCRunner, ArtifactWMDRunner, ArtifactWMDMCRunner, \
    ArtifactWMDUCTMCRunner, ArtifactWMDUCTRunner, ArtifactAvgCosineRunner, ArtifactAvgCosineMCRunner, ArtifactAvgCosineUCTMCRunner, ElementAvgCosineRunner, ElementAvgCosineCDRunner, \
    ElementAvgCosineUCTRunner, ElementAvgCosineMCRunner, \
    ElementAvgCosineUCTMCRunner, ElementAvgCosineUCTCDRunner, ElementAvgCosineUCTMCCDRunner, \
    ElementAvgCosineMCCDRunner,ArtifactAvgCosineUCTRunner, \
    UniXcoderRunner, UniXcoderWMDRunner, UniXcoderCDRunner, UniXcoderWMDCDRunner
from traceLinkProcessing.ElementFilter import ElementFilter, NFRElementFilter, \
    UserRelatedElementFilter, UserRelatedNFRElementFilter
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans, Libest, Albergate, ItrustFull
from utility import Util
from utility.FileUtil import setup_clear_dir
import logging
from joblib import Parallel, delayed
import os
from multiprocessing import Pool


log = logging.getLogger(__name__)

ENGLISH_FASTTEXT_MODEL_PATH = "../../models/cc.en.300.bin"
ITALIAN_FASTTEXT_MODEL_PATH = "../../models/cc.it.300.bin"
UNIXCODER_MODEL_PATH = "../models/unixcoder-base"
models = {'english': ENGLISH_FASTTEXT_MODEL_PATH, 'italian': ITALIAN_FASTTEXT_MODEL_PATH, 'unixcoder': UNIXCODER_MODEL_PATH}

FINAL_THRESHOLDS = [0.586]
MAJORITY_THRESHOLDS = [0.302]
setup_clear_dir(OUTPUT_DIR)

datasets = [Etour(), Itrust(), SmosTrans(), EanciTrans(), Libest()]
runner_names = ["UniXcoderRunner", "UniXcoderWMDRunner", "UniXcoderCDRunner", "UniXcoderWMDCDRunner"]
element_filter = [None]

# nqk preprocessing
nqk = False

runners = []
no_precalc = []
for dataset in datasets:
    for name in runner_names:
        if not ("UCT" in name and isinstance(dataset, Itrust)) and not ("CD" in name and isinstance(dataset, Libest)):
            for e_filter in element_filter:
                cls = globals()[name]
                # normal preprocessing
                if not isinstance(dataset, Itrust):
                    tt = cls(dataset, use_types=True, classname_as_optional_voter=True, element_filter = e_filter, nqk = nqk)
                    tt.configurate_word_choosers(use_types=True, uct=True, mc=True, mb=False)
                    runners.append(tt)
                    tt = cls(dataset, use_types=True, classname_as_optional_voter=True, element_filter = e_filter, nqk = nqk)
                    tt.configurate_word_choosers(use_types=True, uct=True, mc=False, mb=False)
                    runners.append(tt)
                else:
                    tt = cls(dataset, use_types=True, classname_as_optional_voter=True, element_filter = e_filter, nqk = nqk)
                    tt.configurate_word_choosers(use_types=True, uct=False, mc=True, mb=False)
                    runners.append(tt)
                    tt = cls(dataset, use_types=True, classname_as_optional_voter=True, element_filter = e_filter, nqk = nqk)
                    tt.configurate_word_choosers(use_types=True, uct=False, mc=False, mb=False)
                    runners.append(tt)
                
                    


#FINAL_THRESHOLDS = Util.get_range_array(0.4, 0.60, 0.001)  # [0.4, 0.41, ..., 0.5]
#MAJORITY_THRESHOLDS = Util.get_range_array(0.53, 0.70, 0.001)  # [0.53, 0.54, ..., 0.63]

FINAL_THRESHOLDS_eTour = Util.get_range_array(0.52, 0.62, 0.001)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS_eTour = Util.get_range_array(0.33, 0.48, 0.001)  # [0.53, 0.54, ..., 0.63]

FINAL_THRESHOLDS_iTrust = Util.get_range_array(0.47, 0.65, 0.001)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS_iTrust = Util.get_range_array(0.22, 0.40, 0.001)  # [0.53, 0.54, ..., 0.63]

FINAL_THRESHOLDS_Smos = Util.get_range_array(0.38, 0.54, 0.001)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS_Smos = Util.get_range_array(0.00, 0.21, 0.001)  # [0.53, 0.54, ..., 0.63]

FINAL_THRESHOLDS_eAnci = Util.get_range_array(0.43, 0.61, 0.001)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS_eAnci = Util.get_range_array(0.24, 0.35, 0.001)  # [0.53, 0.54, ..., 0.63]

FINAL_THRESHOLDS_Libest = Util.get_range_array(0.37, 0.46, 0.001)  # [0.4, 0.41, ..., 0.5]
MAJORITY_THRESHOLDS_Libest = Util.get_range_array(0.00, 0.18, 0.001)  # [0.53, 0.54, ..., 0.63]

#FINAL_THRESHOLDS = Util.get_range_array(0.40, 0.60, 0.001)  # [0.4, 0.41, ..., 0.5]
#MAJORITY_THRESHOLDS = Util.get_range_array(0.15, 0.32, 0.001)  # [0.53, 0.54, ..., 0.63]

#FINAL_THRESHOLDS = Util.get_range_array(0.4, 1.0, 0.001)  # [0.4, 0.41, ..., 0.5]
#MAJORITY_THRESHOLDS = Util.get_range_array(0.4, 0.9, 0.001)  # [0.53, 0.54, ..., 0.63]

def run_calculate(runner):
    print(type(runner).__name__)
    print(vars(runner))
    runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS, default_final=0.586, default_maj=0.302, also_print_eval=False)
    if isinstance(runner.dataset, Etour):
        runner.calculate_f1_and_map(FINAL_THRESHOLDS_eTour, MAJORITY_THRESHOLDS_eTour, default_final=0.475, default_maj=0.58, also_print_eval=False)
    elif isinstance(runner.dataset, Itrust):
        runner.calculate_f1_and_map(FINAL_THRESHOLDS_iTrust, MAJORITY_THRESHOLDS_iTrust, default_final=0.475, default_maj=0.58, also_print_eval=False)
    elif isinstance(runner.dataset, SmosTrans):
        runner.calculate_f1_and_map(FINAL_THRESHOLDS_Smos, MAJORITY_THRESHOLDS_Smos, default_final=0.475, default_maj=0.58, also_print_eval=False)
    elif isinstance(runner.dataset, EanciTrans):
        runner.calculate_f1_and_map(FINAL_THRESHOLDS_eAnci, MAJORITY_THRESHOLDS_eAnci, default_final=0.475, default_maj=0.58, also_print_eval=False)
    elif isinstance(runner.dataset, Libest):
        runner.calculate_f1_and_map(FINAL_THRESHOLDS_Libest, MAJORITY_THRESHOLDS_Libest, default_final=0.475, default_maj=0.58, also_print_eval=False)
    
def run_precalculate(runner):
    print(type(runner).__name__)
    print(vars(runner))
    runner.precalculate(models, matrix_file_path=None, artifact_map_file_path=None)
    
    
tasksPre = (delayed(run_precalculate)(runner) for runner in runners)
tasks = (delayed(run_calculate)(runner) for runner in runners)
#executor = Parallel(n_jobs=2, backend='multiprocessing')
#executor(tasksPre)
executor = Parallel(n_jobs=50, backend='multiprocessing')
executor(tasks)



