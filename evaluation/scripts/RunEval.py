
from TraceabilityRunner import FTLRRunner, FTLRCDRunner, \
    FTLRUCTRunner, FTLRMCRunner, \
    FTLRUCTMCRunner, FTLRUCTCDRunner, FTLRUCTMCCDRunner, \
    FTLRMCCDRunner, OUTPUT_DIR, FTLRUCTMCCDCCRunner, ArtifactWMDRunner, ArtifactWMDMCRunner, \
    ArtifactWMDUCTMCRunner, ArtifactWMDUCTRunner, ArtifactAvgCosineRunner, ArtifactAvgCosineMCRunner, ArtifactAvgCosineUCTMCRunner, ElementAvgCosineRunner, ElementAvgCosineCDRunner, \
    ElementAvgCosineUCTRunner, ElementAvgCosineMCRunner, \
    ElementAvgCosineUCTMCRunner, ElementAvgCosineUCTCDRunner, ElementAvgCosineUCTMCCDRunner, \
    ElementAvgCosineMCCDRunner,ArtifactAvgCosineUCTRunner
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
models = {'english': ENGLISH_FASTTEXT_MODEL_PATH, 'italian': ITALIAN_FASTTEXT_MODEL_PATH}

FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]
setup_clear_dir(OUTPUT_DIR)

datasets = [Etour(), Itrust(), ItrustFull(), Smos(), Eanci(), Libest()]
#datasets = [Etour(classification_file=Etour.ETOUR_GOLD_STANDARD_CLASSIFICATION_FILE), Itrust(classification_file=Itrust.ITRUST_GOLD_STANDARD_CLASSIFICATION_FILE), ItrustFull(classification_file=ItrustFull.ITRUST_GOLD_STANDARD_CLASSIFICATION_FILE), Smos(classification_file=Smos.SMOS_GOLD_STANDARD_CLASSIFICATION_FILE), Eanci(classification_file=Eanci.EANCI_GOLD_STANDARD_CLASSIFICATION_FILE), Libest(classification_file=Libest.LIBEST_GOLD_STANDARD_CLASSIFICATION_FILE)]
runner_names = ["FTLRRunner", "FTLRUCTRunner", "FTLRMCRunner","FTLRCDRunner", "FTLRUCTMCRunner", "FTLRUCTCDRunner", "FTLRUCTMCCDRunner", "FTLRMCCDRunner"]
#runner_names = ["ArtifactAvgCosineRunner", "ArtifactAvgCosineUCTRunner", "ArtifactAvgCosineMCRunner", "ArtifactAvgCosineUCTMCRunner", "ArtifactWMDRunner", "ArtifactWMDUCTRunner", "ArtifactWMDMCRunner", "ArtifactWMDUCTMCRunner"]
#runner_names = ["ElementAvgCosineRunner", "ElementAvgCosineUCTRunner", "ElementAvgCosineMCRunner", "ElementAvgCosineCDRunner", "ElementAvgCosineUCTMCRunner", "ElementAvgCosineUCTCDRunner", "ElementAvgCosineMCCDRunner", "ElementAvgCosineUCTMCCDRunner"]
#element_filter = [None]
element_filter = [NFRElementFilter(), UserRelatedNFRElementFilter(), UserRelatedElementFilter()]

runners = []
no_precalc = []
for dataset in datasets:
    for name in runner_names:
        if not ("UCT" in name and isinstance(dataset, Itrust)) and not ("CD" in name and isinstance(dataset, Libest)):
            for e_filter in element_filter:
                cls = globals()[name]
                tt = cls(dataset, use_types=True, classname_as_optional_voter=True, element_filter = e_filter)
                runners.append(tt)
                if "CD" in name:
                    replaced = name.replace("CD", "")
                    if (replaced in runner_names) and (runner_names.index(name) > runner_names.index(replaced)):
                        no_precalc.append(tt)
                    


FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]

#OPT
#FINAL_THRESHOLDS = Util.get_range_array(0.0, 1.0, 0.001)  # [0.4, 0.401, ..., 1.0]
#MAJORITY_THRESHOLDS = Util.get_range_array(0.0, 0.9, 0.001)  # [0.4, 0.401, ..., 0.9]


def run_calculate(runner):
    print(type(runner).__name__)
    print(vars(runner))
    #ORG
    #runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS, default_final=0.44, default_maj=0.59, also_print_eval=False)

    #MED
    runner.calculate_f1_and_map(FINAL_THRESHOLDS, MAJORITY_THRESHOLDS, default_final=0.475, default_maj=0.58, also_print_eval=False)
    
def run_precalculate(runner):
    print(type(runner).__name__)
    print(vars(runner))
    runner.precalculate(models, matrix_file_path=None, artifact_map_file_path=None)
    
tasksPre = (delayed(run_precalculate)(runner) for runner in runners)
tasks = (delayed(run_calculate)(runner) for runner in runners)
executor = Parallel(n_jobs=10, backend='multiprocessing')
executor(tasksPre)
executor = Parallel(n_jobs=50, backend='multiprocessing')
executor(tasks)
