from pathlib import Path
from enum import Enum
import logging

log = logging.getLogger(__name__)

ROOT = Path(__file__).parent
DATASETS = ROOT / "datasets"
OUTPUT_DIR = ROOT / "output"
LABELED_FOLDER = DATASETS / "labeled"

PRECALCULATED_SPACY_ITALIAN_LEMMA_CSV = ROOT / "Preprocessing/resources/precalculated_spacy_italian_lemmas.csv"
PRECALCULATED_SPACY_ENGLISH_LEMMA_CSV = ROOT / "Preprocessing/resources/precalculated_spacy_english_lemmas.csv"

PREPROCESSED_CODE_OUTPUT_DIR = OUTPUT_DIR / "Preprocessed_Code"
PREPROCESSED_REQ_OUTPUT_DIR = OUTPUT_DIR / "Preprocessed_Req"

TEST_REQ_DIR = ROOT / "RequirementEmbeddingCreator/datasets"
TEST_CODE_DIR = ROOT / "CodeEmbeddingCreator/datasets"

BERT_MODEL_DIR = ROOT / "WordEmbeddingCreator/resources/bertmodels"
BERT_SENTENCE_MODEL = BERT_MODEL_DIR / "sentence_bert_model"
BERT_IDENTIFIER_MODEL = BERT_MODEL_DIR / "identifier_bert_model"
BERT_FAKE_SENTENCE_MODEL = BERT_MODEL_DIR / "fake_sentence_bert_model"
BERT_WHOLE_COMMENT_MODEL = BERT_MODEL_DIR / "whole_comment_bert_model"
BERT_CLASSNAME_PREFIX_IDENTIFIER_MODEL = BERT_MODEL_DIR / "classname_prefix_identifiert_bert_model"

IDENTIFIER_TRACELINK_FOLDER = Path("identifier_tracelinks")
SENTENCE_TRACELINK_FOLDER = Path("sentence_tracelinks")
FAKE_SENTENCE_TRACELINK_FOLDER = Path("fake_sentence_tracelinks")
CLASSNAME_PREFIX_IDENTIFIER_TRACELINK_FOLDER = Path("classname_prefix_identifier_tracelinks")
WHOLE_COMMENT_TRACELINK_FOLDER = Path("whole_comment_tracelinks")

CODE_COLUMN_NAME = "code_entry"
REQ_COLUMN_NAME = "requirement"
REQ_FILE_COLUMN_NAME = "requirement_filename"
CODE_FILE_COLUMN_NAME = "code_filename"
VALID_COLUMN_NAME = "valid"
DATASET_COLUMN_NAME = "dataset"

TRACE_LINK_FILE_CSV_HEADER = [REQ_COLUMN_NAME, CODE_COLUMN_NAME, VALID_COLUMN_NAME, REQ_FILE_COLUMN_NAME, CODE_FILE_COLUMN_NAME, DATASET_COLUMN_NAME]
CODE_FILE_CSV_HEADER = [CODE_COLUMN_NAME, CODE_FILE_COLUMN_NAME, DATASET_COLUMN_NAME]
REQ_FILE_CSV_HEADER = [REQ_COLUMN_NAME, REQ_FILE_COLUMN_NAME, DATASET_COLUMN_NAME]
RECALL_PREC_CSV_HEADER = ["recall", "precision"]

CSV_DELIM = ";"

VALID_TRACE_LINK_LABEL = 1
INVALID_TRACE_LINK_LABEL = 0

CSV_EXT = ".csv"
TXT_EXT = ".txt"
JSON_EXT = ".json"
EXCEL_EXT = ".xlsx"

class ContentType(Enum): 
    """ Type of contents for generated csv files for bert training"""
    comment = 1 # Whole code comment
    comment_sentence = 2 # Code comment sentence
    req = 3 # Whole requirement
    req_sentence = 4 # Requirement sentence
    identifier = 5 
    class_prefix_identifier = 6 # identifier with classname as prefix
    
class TraceLinkType(Enum):
    """ Type of generated trace links for bert training"""
    sentence_tracelinks = 1 # Req sentencen <> comment sentence trace links
    identifier_tracelinks = 2 # Req sentence <> identifier tracelinks
    fake_sentence = 3 # Req sentence <> comment sentence without bert separator
    classname_prefix = 4 # Like identifier_tracelinks, but with classname as prefix for identifier
    whole_comment = 5 # Whole req <> whole comment
    
    
def tracelink_folder(tracelinkType: TraceLinkType):
    if tracelinkType == TraceLinkType.identifier_tracelinks:
        return IDENTIFIER_TRACELINK_FOLDER
    elif tracelinkType == TraceLinkType.sentence_tracelinks:
        return SENTENCE_TRACELINK_FOLDER
    elif tracelinkType == TraceLinkType.fake_sentence:
        return FAKE_SENTENCE_TRACELINK_FOLDER
    elif tracelinkType == TraceLinkType.classname_prefix:
        return CLASSNAME_PREFIX_IDENTIFIER_TRACELINK_FOLDER
    elif tracelinkType == TraceLinkType.whole_comment:
        return WHOLE_COMMENT_TRACELINK_FOLDER
    else:
        log.error(str(tracelinkType) + " is not a TraceLinkType enum constant")
    
def bert_predictor_path(tracelinkType: TraceLinkType):
    if tracelinkType == TraceLinkType.identifier_tracelinks:
        return BERT_IDENTIFIER_MODEL
    elif tracelinkType == TraceLinkType.sentence_tracelinks:
        return BERT_SENTENCE_MODEL
    elif tracelinkType == TraceLinkType.fake_sentence:
        return BERT_FAKE_SENTENCE_MODEL
    elif tracelinkType == TraceLinkType.classname_prefix:
        return BERT_CLASSNAME_PREFIX_IDENTIFIER_MODEL
    elif tracelinkType == TraceLinkType.whole_comment:
        return BERT_WHOLE_COMMENT_MODEL
    else:
        log.error(str(tracelinkType) + " is not a TraceLinkType enum constant")
        
def req_csv_content(tracelinkType: TraceLinkType):
    if tracelinkType == TraceLinkType.identifier_tracelinks:
        return ContentType.req_sentence
    elif tracelinkType == TraceLinkType.sentence_tracelinks:
        return ContentType.req_sentence
    elif tracelinkType == TraceLinkType.fake_sentence:
        return ContentType.req_sentence
    elif tracelinkType == TraceLinkType.classname_prefix:
        return ContentType.req_sentence
    elif tracelinkType == TraceLinkType.whole_comment:
        return ContentType.req
    else:
        log.error(str(tracelinkType) + " is not a TraceLinkType enum constant")
     
def code_csv_content(tracelinkType: TraceLinkType):
    if tracelinkType == TraceLinkType.identifier_tracelinks:
        return ContentType.identifier
    elif tracelinkType == TraceLinkType.sentence_tracelinks:
        return ContentType.comment_sentence
    elif tracelinkType == TraceLinkType.fake_sentence:
        return ContentType.comment_sentence
    elif tracelinkType == TraceLinkType.classname_prefix:
        return ContentType.class_prefix_identifier
    elif tracelinkType == TraceLinkType.whole_comment:
        return ContentType.comment
    else:
        log.error(str(tracelinkType) + " is not a TraceLinkType enum constant")
     

def __get_filename_prefix(percent):
    return str(percent) + "_"

def __build_filename(folder, percent, dataset, link_type, file_ext):
    return folder / (__get_filename_prefix(percent) + dataset.name() + "_" + link_type.name + file_ext)

def req_emb_output_file(dataset, tracelink_creator_classname):
    return OUTPUT_DIR / (str(tracelink_creator_classname) + "_" + dataset.name() + "_req_embeddings" + TXT_EXT)

def code_emb_output_file(dataset, tracelink_creator_classname):
    return OUTPUT_DIR / (str(tracelink_creator_classname) + "_" + dataset.name() + "_code_embeddings" + TXT_EXT)

def bert_trace_pair_json_filename(percent, dataset, tracelinkType: TraceLinkType):
    return dataset.folder() / tracelink_folder(tracelinkType) / (__get_filename_prefix(percent) + dataset.name() + "_" + tracelinkType.name + "_bert_trace_pairs" + JSON_EXT)

def precalculated_json_filename(dataset, folder_name, name_suffix):
    """
    For precalculated req x code similarities
    """
    return dataset.folder() / Path(folder_name) / ("precalculated_" + dataset.name() + "_" + name_suffix + JSON_EXT)

def precalculated_finetuned_json_filename(dataset, name_suffix):
    """
    For precalculated req x code similarities with finetuned fasttext model
    """
    return dataset.folder() / Path(name_suffix) / ("precalculated_" + dataset.name() + "_" + name_suffix + "_finetuned" + JSON_EXT)

def precalculated_vectors_json_filename(dataset, name_suffix):
    """
    For precalculated req XOR code embedding vectors
    """
    return dataset.folder() / Path(name_suffix) / ("precalculated_" + dataset.name() + "_vectors_" + name_suffix + JSON_EXT)

def precalculated_tfidf_weights_filename(dataset, emb_creator_class_name):
    """
    For precalculated tfidf weights of a single (req|code) file
    """
    return dataset.folder() / Path(emb_creator_class_name) / (dataset.name() + "_tfidf_vectors_" + emb_creator_class_name + CSV_EXT)

def precalculated_all_filelevel_sims_csv_filename(dataset, tlp_name):
    """
    For precalculated file level similarity values between all classes and reqs.
    Used for genetic algorithm
    """
    return dataset.folder() / Path(tlp_name) / (dataset.name() + tlp_name +  "_all_filelevel_sims" + CSV_EXT)

def precalculated_jaccard_sims_csv_filename(dataset, req_wordchooser_name, code_wordchooser_name, output_suffix=""):
    """
    For precalculated file level jaccard similarity values between all classes and reqs.
    Used for genetic algorithm
    """
    return dataset.folder() / Path("jaccard") / (dataset.name() + "_" + req_wordchooser_name + "_" + code_wordchooser_name +  "_jaccard_sims" + output_suffix + CSV_EXT)


def precalculated_req_code_tfidf_vectors_filename(dataset, req_wordchooser_name, code_wordchooser_name, output_suffix=""):
    """
    For precalculated tfidf vectors with the union of code and req files as vocabulary
    """
    return dataset.folder() / Path("tfidf_cos_sim") / (dataset.name() + "_" + req_wordchooser_name + "_" + code_wordchooser_name +  "_req_code_tfidf_vectors" + output_suffix + CSV_EXT)

def precalculated_req_code_tfidf_cos_sim_filename(dataset, req_wordchooser_name, code_wordchooser_name, output_suffix=""):
    """
    For precalculated file level cos sim similarity values between all classes and reqs.
    Used for genetic algorithm
    """
    return dataset.folder() / Path("tfidf_cos_sim") / (dataset.name() + "_" + req_wordchooser_name + "_" + code_wordchooser_name +  "_req_code_tfidf_cos_sims" + output_suffix + CSV_EXT)

def resulting_valid_tracelinks_csv_filename(dataset, tlp_name, elem_thresh, maj_thresh, file_thresh, name_suffix=""):
    """
    Path of the valid result trace links of a technique.
    Used for genetic algorithm as initial individual.
    """
    return dataset.folder() / Path(tlp_name) / (dataset.name() + tlp_name + "_e{}m{}f{}".format(elem_thresh, maj_thresh, file_thresh) + "_resulting_valid_tracelinks" + name_suffix + CSV_EXT)

def all_resulting_tracelinks_csv_filename(dataset, tlp_name, elem_thresh, maj_thresh, file_thresh, name_suffix=""):
    """
    Path of the result trace links of a technique.
    Used for genetic algorithm as initial individual.
    """
    return dataset.folder() / Path(tlp_name) / (dataset.name() + tlp_name + "_e{}m{}f{}".format(elem_thresh, maj_thresh, file_thresh) + "_all_resulting_tracelinks" + name_suffix + CSV_EXT)


def labeled_tracelink_csv_filename(percent, dataset, tracelink_type):
    return __build_filename(LABELED_FOLDER, percent, dataset, tracelink_type, CSV_EXT)

def req_csv_filename(percent, dataset, tracelinkType):
    __build_filename(dataset.folder() / tracelink_folder(tracelinkType), percent, dataset, req_csv_content(tracelinkType), CSV_EXT)

def code_csv_filename(percent, dataset, tracelinkType):
    return __build_filename(dataset.folder() / tracelink_folder(tracelinkType), percent, dataset, code_csv_content(tracelinkType), CSV_EXT)

def inheritance_graph_filename(dataset):
    return dataset.folder() / (dataset.name() + "_inheritance_graph" + JSON_EXT)

def implements_graph_filename(dataset):
    return dataset.folder() / (dataset.name() + "_implements_graph" + JSON_EXT)

def classifier_to_file_map_filename(dataset):
    return dataset.folder() / (dataset.name() + "_classifier2file_map" + JSON_EXT)

def remaining_trace_matrix_filename(percent, dataset, tracelinkType):
    return dataset.folder() / tracelink_folder(tracelinkType) / (__get_filename_prefix(percent) + dataset.name() + "_remaining_trace_matrix_" + tracelinkType.name + TXT_EXT)

def excel_eval_filename(dataset, evalname: str):
    return OUTPUT_DIR / ("excel_eval_" + dataset.name() + "_" + evalname + EXCEL_EXT)

def csv_recall_precision_filename(dataset, evalname: str):
    return OUTPUT_DIR / ("recall_prec_" + dataset.name() + "_" + evalname + CSV_EXT)

def csv_recall_map_filename(dataset, evalname: str):
    return OUTPUT_DIR / ("recall_map_" + dataset.name() + "_" + evalname + CSV_EXT)
