import argparse
import os.path
import sys

from TraceabilityRunner import FTLRRunner, FTLRCDRunner, \
    FTLRUCTRunner, FTLRMCRunner, \
    FTLRUCTMCRunner, FTLRUCTCDRunner, FTLRUCTMCCDRunner, \
    FTLRMCCDRunner, OUTPUT_DIR, FTLRUCTMCCDCCRunner, ArtifactWMDRunner, ArtifactWMDUCTRunner, ArtifactWMDMCRunner, \
    ArtifactWMDUCTMCRunner, ArtifactAvgCosineRunner, ArtifactAvgCosineUCTRunner, ArtifactAvgCosineMCRunner, \
    ArtifactAvgCosineUCTMCRunner, \
    ElementAvgCosineRunner, ElementAvgCosineCDRunner, ElementAvgCosineMCRunner, ElementAvgCosineUCTRunner, \
    ElementAvgCosineUCTMCRunner, ElementAvgCosineMCCDRunner, ElementAvgCosineUCTCDRunner, ElementAvgCosineUCTMCCDRunner, \
    UniXcoderRunner
from traceLinkProcessing.ElementFilter import ElementFilter, NFRElementFilter, UserRelatedElementFilter, \
    UserRelatedNFRElementFilter
from datasets.Dataset import Etour, Itrust, Smos, Eanci, SmosTrans, EanciTrans, Libest, Albergate, ItrustFull
from utility import Util
from utility.FileUtil import setup_clear_dir

FINAL_THRESHOLDS = [0.44]
MAJORITY_THRESHOLDS = [0.59]

setup_clear_dir(OUTPUT_DIR)


def handle_dataset(dataset, gold_standard):
    resulting_dataset = Etour()
    if dataset.lower() == "itrust":
        if gold_standard:
            resulting_dataset = Itrust(classification_file=Itrust.ITRUST_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = Itrust()
    elif dataset.lower() == "itrustjsp":
        if gold_standard:
            resulting_dataset = ItrustFull(classification_file=ItrustFull.ITRUST_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = ItrustFull()
    elif dataset.lower() == "smos":
        if gold_standard:
            resulting_dataset = Smos(classification_file=Smos.SMOS_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = Smos()
    elif dataset.lower() == "eanci":
        if gold_standard:
            resulting_dataset = Eanci(classification_file=Eanci.EANCI_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = Eanci()
    elif dataset.lower() == "smostrans":
        if gold_standard:
            resulting_dataset = SmosTrans(classification_file=SmosTrans.SMOS_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = SmosTrans()
    elif dataset.lower() == "eancitrans":
        if gold_standard:
            resulting_dataset = EanciTrans(classification_file=EanciTrans.EANCI_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = EanciTrans()
    elif dataset.lower() == "libest":
        if gold_standard:
            resulting_dataset = Libest(classification_file=Libest.LIBEST_GOLD_STANDARD_CLASSIFICATION_FILE)
        else:
            resulting_dataset = Libest()
    elif dataset.lower() == "albergate":
        if gold_standard:
            print("Albergate has no requirements classification gold standard")
        resulting_dataset = Albergate()
    return resulting_dataset


def handle_filter(filter):
    resulting_filter = None
    if not filter:
        return resulting_filter
    elif filter.lower() == "nf":
        resulting_filter = NFRElementFilter()
    elif filter.lower() == "nb":
        resulting_filter = UserRelatedElementFilter()
    elif filter.lower() == "both":
        resulting_filter = UserRelatedNFRElementFilter()
    return resulting_filter


def build_runner_name(variant, use_case_templates, method_comments, call_dependencies):
    runner_name = ""
    if variant.lower() == "ecoss":
        runner_name += "ElementAvgCosine"
    elif variant.lower() == "acoss":
        runner_name += "ArtifactAvgCosine"
    elif variant.lower() == "awmd":
        runner_name += "ArtifactWMD"
    elif variant.lower() == "uxccos":
        runner_name += "UniXcoder"
    elif variant.lower() == "uxcwmd":
        runner_name += "UniXcoderWMD"
    else:
        runner_name += "FTLR"

    if not runner_name.startswith("UniXcoder"):
        if use_case_templates:
            runner_name += "UCT"
        if method_comments:
            runner_name += "MC"
    if call_dependencies:
        runner_name += "CD"

    runner_name += "Runner"
    return runner_name


def handle_variant(variant, dataset, filter, use_case_templates, method_comments, call_dependencies, nqk):
    runner_name = build_runner_name(variant, use_case_templates, method_comments, call_dependencies)
    cls = globals()[runner_name]
    runner = cls(dataset=dataset, element_filter=filter, nqk=nqk)
    if isinstance(runner, UniXcoderRunner):
        uct = mc = False
        if use_case_templates:
            uct = True
        if method_comments:
            mc = True
        runner.configurate_word_choosers(uct=uct, mc=mc)
        return runner
    return runner


def handle_models(english_model, italian_model, unixcoder_model, dataset, runner):
    models = {}
    if dataset.is_english():
        if isinstance(runner, UniXcoderRunner):
            if os.path.exists(unixcoder_model):
                models['unixcoder'] = unixcoder_model
            else:
                print("Unixcoder model file does not exist:")
                print(unixcoder_model)
        else:
            if os.path.exists(english_model):
                models['english'] = english_model
            else:
                print("English model file does not exist:")
                print(english_model)
    else:
        if not isinstance(runner, UniXcoderRunner):
            if os.path.exists(italian_model):
                models['italian'] = italian_model
            else:
                print("Italian model file does not exist:")
                print(italian_model)
        else:
            print("Unixcoder can only be applied to english datasets!")
            sys.exit(1)

    return models


def precalculate(runner, models):
    runner.precalculate(models)


def run(runner, args):
    if args.metric.lower() == "f1":
        final_thresholds = [args.final_threshold]
        majority_thresholds = [args.majority_threshold]
        if args.optimize_thresholds:
            final_thresholds = Util.get_range_array(0.01, 0.99, 0.01)  # [0.4, 0.41, ..., 0.5]
            majority_thresholds = Util.get_range_array(0.01, 0.99, 0.01)  # [0.53, 0.54, ..., 0.63]
        else:
            runner.output_trace_links(final_thresholds, majority_thresholds, final=args.final_threshold,
                                      maj=args.majority_threshold)
        runner.calculate_f1(final_thresholds, majority_thresholds)
    elif args.metric.lower() == "map":
        runner.calculate_map()
    elif args.metric.lower() == "both":
        final_thresholds = [args.final_threshold]
        majority_thresholds = [args.majority_threshold]
        if args.optimize_thresholds:
            final_thresholds = Util.get_range_array(0.01, 0.99, 0.01)  # [0.4, 0.41, ..., 0.5]
            majority_thresholds = Util.get_range_array(0.01, 0.99, 0.01)  # [0.53, 0.54, ..., 0.63]
        else:
            runner.output_trace_links(final_thresholds, majority_thresholds, final=args.final_threshold,
                                      maj=args.majority_threshold)
        runner.calculate_f1_and_map(final_thresholds, majority_thresholds)
    else:
        if not args.optimize_thresholds:
            runner.output_trace_links([args.final_threshold], [args.majority_threshold], final=args.final_threshold,
                                      maj=args.majority_threshold)


def main(args):
    dataset = handle_dataset(args.dataset, args.gold_standard)
    filter = handle_filter(args.filter)
    runner = handle_variant(args.variant, dataset, filter, args.use_case_templates, args.method_comments,
                            args.call_dependencies, args.nqk)
    models = handle_models(args.english_model, args.italian_model, args.unixcoder_model, dataset, runner)
    # Your application logic here
    if args.processing_step.lower() == "precalculate":
        precalculate(runner, models)
    elif args.processing_step.lower() == "run":
        run(runner, args)
    else:
        precalculate(runner, models)
        run(runner, args)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(prog="FTLR",
                                     description="Performs Evaluation of FTLR-Variants on Different Datasets.")
    parser.add_argument("--dataset", "-d", type=str, required=True,
                        choices=["etour", "itrust", "itrustjsp", "smos", "eanci", "smostrans", "eancitrans", "libest",
                                 "albergate"], help="Choose dataset to run on")
    parser.add_argument("--variant", "-v", type=str, choices=["ftlr", "ecoss", "acoss", "awmd", "uxccos", "uxcwmd"],
                        help="Choose tlr variant (default: %(default)s)", default="ftlr")
    parser.add_argument("--use_case_templates", "-uct", help="Use use case template filter", action='store_true')
    parser.add_argument("--method_comments", "-mc", help="Use method comments in representation", action='store_true')
    parser.add_argument("--call_dependencies", "-cd", help="Use call dependencies", action='store_true')
    parser.add_argument("--nqk", "-nqk",
                        help="Use reduced preprocessing (only remove links, numbers and special characters)",
                        action='store_true')
    parser.add_argument("--metric", "-m", type=str, choices=["f1", "map", "both", "None"],
                        help="Choose metric to calculate (default: %(default)s)", default="None")
    parser.add_argument("--processing_step", "-p", type=str, choices=["precalculate", "run", "both"],
                        help="Either precalculate, run or both (default: %(default)s)", default="both")
    parser.add_argument("--filter", "-f", type=str, choices=["NF", "NB", "both", "None"],
                        help="Either NF, NB, both or None (default: %(default)s)", default=None)
    parser.add_argument("--gold_standard", "-g", help="Use gold standard requirements classification results",
                        action='store_true')
    parser.add_argument("--majority_threshold", "-mt", type=float, default=0.59,
                        help="Define the majority threshold (default: %(default)s)")
    parser.add_argument("--final_threshold", "-ft", type=float, default=0.44,
                        help="Define the final threshold (default: %(default)s)")
    parser.add_argument("--english_model", "-em", type=str,
                        help="Path to english fasttext model file (default: %(default)s)",
                        default="../models/cc.en.300.bin")
    parser.add_argument("--italian_model", "-im", type=str,
                        help="Path to italian fasttext model file (default: %(default)s)",
                        default="../models/cc.it.300.bin")
    parser.add_argument("--unixcoder_model", "-uxcm", type=str,
                        help="Path to unixcoder model files (default: %(default)s)",
                        default="../models/unixcoder-base")
    parser.add_argument("--optimize_thresholds", "-OPT",
                        help="Optimizes thresholds by varying in 0.01 steps from 0 to 1 (does not output trace links)",
                        action='store_true')

    args = parser.parse_args()
    print("Configuration:")
    print(args)
    main(args)
