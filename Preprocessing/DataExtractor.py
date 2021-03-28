import logging, os, json, abc, FileUtil
from SolutionTraceMatrix import SolutionTraceMatrix
from Preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer
from Preprocessing.Tokenizer import SentenceTokenizer
from Preprocessing.Preprocessor import Preprocessor, CamelCaseSplitter,\
    NonLetterFilter, Separator, JavaDocFilter,\
    DuplicateWhiteSpaceFilter, AddFullStop, RemoveLastPunctuatioMark
from Paths import *
import Util
from Dataset import Dronology, DronologyDD, Dataset

"""
Contains various functions to extract dronology data from the original dronology solution matrix (that contains the requirements)
and functions to generated labeled data for BERT classification training
"""

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)

      
def extract_dronology_design_definitions(path=DronologyDD.DRONOLOGY_JSON_FILE, only_existing_code_classes=True):
    _extract_dronology_data(path, "DD", DronologyDD.DRONOLOGY_DD_REQ_DIR, DronologyDD.DRONOLOGY_DD_SOLUTION_MATRIX_PATH, only_existing_code_classes)
    
def extract_dronology_reqs(path=Dronology.DRONOLOGY_JSON_FILE, only_existing_code_classes=True):
    _extract_dronology_data(path, "RE", Dronology.DRONOLOGY_REQ_DIR, Dronology.DRONOLOGY_SOLUTION_MATRIX_PATH, only_existing_code_classes)
 
def _extract_dronology_data(path, issueid_prefix, req_folder, trace_matrix_path, only_existing_code_classes=True):
    """
    Extracts Requirements or Design definitions from the dronology json file and constructs the trace matrix between
    the extracted elements and code classes.
    issueid_prefix = "RE" for Requirements
    issueid_prefix = "DD" for Design Definitions (Refinement of Requirements)
    """
    entries = json.loads(FileUtil.read_textfile_into_string(path))["entries"]
    entries_dict = dict()
    requirement_indices = []

    for index, entry in enumerate(entries):
        issue = entry["issueid"]
        entries_dict[issue] = entries[index]
        if issue[:2] == issueid_prefix:
            requirement_indices.append(index)
    existing_classes = set()
    for filename in os.listdir(Dronology.DRONOLOGY_CODE_DIR):
        existing_classes.add(FileUtil.get_filename_without_extension__from_path(filename))
    requirement_dict = dict()
    links_dict = dict()
    for req_index in sorted(requirement_indices, reverse=True) :
        req_entry = entries[req_index]
        req_name = req_entry["issueid"]
        req_text, req_code_links = _traverse_dronology_node(req_entry, entries_dict)
        requirement_dict[req_name] = "\n".join(req_text)
        if only_existing_code_classes:
            ex_class_links = [cls for cls in req_code_links if cls in existing_classes]
            if ex_class_links:
                links_dict[req_name] =  ex_class_links
            else:
                continue
        else:
            links_dict[req_name] = req_code_links
    _write_dronology_reqs(requirement_dict, req_folder)
    _write_dronology_trace_matrix(links_dict, trace_matrix_path)
        
def _write_dronology_reqs(requirement_dict, folder):
    for req_name in requirement_dict:
        FileUtil.write_file(folder / (req_name + ".txt"), requirement_dict[req_name])
        
def _write_dronology_trace_matrix(requirement_dict, file):
    all_trace_links_string = []
    for req_name in requirement_dict:
        if requirement_dict[req_name]:
            all_trace_links_string.append(req_name + ":" + " ".join([class_name for class_name in requirement_dict[req_name]]))
    FileUtil.write_file(file, "\n".join(all_trace_links_string))
        
def _traverse_dronology_node(node: dict, entries_dict):
    text = []
    code_links = set()
    if node["attributes"]["summary"]:
        text.append(node["attributes"]["summary"])
    if node["attributes"]["description"]:
        text.append(node["attributes"]["description"])
    for code_link in node["code"]:
        file_name = _extract_java_filename(code_link["filename"])
        if file_name:
            code_links.add(file_name)
    for value_list in node["children"].values():
        for issue_id in value_list:
            child_text, child_code_links = _traverse_dronology_node(entries_dict[issue_id], entries_dict)
            if child_text:
                text += child_text
            if child_code_links:
                code_links.update(child_code_links)
    return text, code_links

def _extract_java_filename(path):
    extension = str(os.path.splitext(path)[1])
    if extension == ".java":
        return FileUtil.get_filename_without_extension__from_path(path)
    return ""
  
def count_dronology_data():
    """
    Counts the trace links and code classes mentioned in the solution json file
    and compare it with the existing code files
    """
    total_classes = set()
    for filename in os.listdir(Dronology().code_folder()):
        total_classes.add(FileUtil.get_filename_without_extension__from_path(filename))
    print("Existing code classes: " + str(len(total_classes)))
    s = FileUtil.read_textfile_into_string(Dronology().folder() / 'dronologydataset01.json')
    data = json.loads(s)
    found_classes = set()
    entries = data["entries"]
    for entry in entries:
        for link in entry["code"]:
            file = link["filename"]
            found_classes.add(file)
    print("Classes in solution matrix: " + str(len(found_classes)))
    existing_java = non_existing_java =  python = 0
    for f in found_classes:
        ex = str(os.path.splitext(f)[1])
        if ex == ".java":
            name = FileUtil.get_filename_without_extension__from_path(f)
            if name in total_classes:
                existing_java += 1
            else:
                non_existing_java +=  1
        elif ex == ".py":
            python += 1
        else:
            print("unknow: " + os.path.splitext(f)[1])
            
    print("Existing java classes in solution: {}, java classes not in solution: {}, python classes in solution: {}".format(
        existing_java, non_existing_java, python))
    links = noL = dd_l = st_l = cc_l = 0
    dd_l_ex = st_l_ex = cc_l_ex = links_ex = 0
    req_l_ex = 0
    req_l =0
    for entry in entries:
        for link in entry["code"]:
            file = link["filename"]
            ext = str(os.path.splitext(file)[1])
            fn = FileUtil.get_filename_without_extension__from_path(file)
            if ext == ".java":
                links += 1
                if entry["issueid"][:2] == "RE":
                    req_l += 1
                if entry["issueid"][:2] == "DD":
                    dd_l += 1
                if entry["issueid"][:2] == "ST":
                    st_l += 1
                if entry["issueid"][:2] == "CO":
                    cc_l  += 1
                if fn in total_classes:
                    links_ex += 1
                    if entry["issueid"][:2] == "RE":
                        req_l_ex += 1
                    if entry["issueid"][:2] == "DD":
                        dd_l_ex += 1
                    if entry["issueid"][:2] == "ST":
                        st_l_ex += 1
                    if entry["issueid"][:2] == "CO":
                        cc_l_ex  += 1
            else:
                noL += 1
    print("\nExistingLinks/TotalLinks to Code:\n {}/{} REQ; {}/{} DD; {}/{} Subtask; {}/{} Component; {}/{} javaLinks; {} NonJava".format(req_l_ex, req_l,
            dd_l_ex, dd_l, st_l_ex, st_l, cc_l_ex, cc_l, links_ex, links, noL))


class LabeledDataCreator(abc.ABC):
    """
    Super class for extraction of labeled trace link data out of requirement and code files that can be used
    to train BERT classifier
    """
    
    def __init__(self, split_percent, dataset: Dataset, seed):
        self._req_tokenizer = SentenceTokenizer()
        self._req_preprocessor = Preprocessor([Separator(True), CamelCaseSplitter(True), NonLetterFilter(), DuplicateWhiteSpaceFilter(), AddFullStop()])
        self._code_tokenizer = JavaCodeASTTokenizer(SentenceTokenizer())
        self._code_preprocessor = Preprocessor([JavaDocFilter(), Separator(True), CamelCaseSplitter(True), NonLetterFilter(), DuplicateWhiteSpaceFilter(), AddFullStop()])
        self._chosen_req_filenames = set() # contains req file names (without path and extension) for the training set
        self._remaining_req_filenames = set() # contains req file names (without path and extension) for the test set
        self._chosen_code_filenames = set() # contains code file names (without path and extension) for the training set
        self._remaining_code_filenames = set() # contains req file names (without path and extension) for the test set
        self._chosen_trace_matrix = SolutionTraceMatrix() # contains valid trace links between chosen code and req files for the training set
        self._remaining_trace_matrix = SolutionTraceMatrix() # contains valid trace links between remaining code and req files for the test set
        self._all_req_files = FileUtil.get_files_in_directory(dataset.req_folder()) # all req files of a project (e.g. etour)
        self._all_code_files = FileUtil.get_files_in_directory(dataset.code_folder()) # all code files of a project
        self._split_percent =  split_percent # percentage of chosen file data
        self._dataset = dataset
        self._seed = seed
        self._solution_matrix = dataset.solution_matrix() # complete solution matrix of a project (e.g. etour)
        self._tracelink_type = None # Set this in non-abstract sub class constructors
    
    @abc.abstractmethod
    def _choose_files(self) -> ([str], [str]):
        """
        Fill self._chosen_req_filenames, self._remaining_req_filenames, self._chosen_code_filenames, self._remaining_code_filenames
        
        chosen = intended for training set; remaining = intended for test set
        """
        pass
    
    @abc.abstractmethod
    def _extract_code_entries(self, file_representation, filename):
        pass
    @abc.abstractmethod
    def _extract_req_entries(self, file_representation, filename):
        pass
    def _write_req_entries(self, chosen_entries, remaining_entries):
        FileUtil.write_rows_to_csv_file(req_csv_filename(self._split_percent, self._dataset, self._tracelink_type), chosen_entries)
        FileUtil.write_rows_to_csv_file(req_csv_filename(str(Util.complement(self._split_percent)), self._dataset, self._tracelink_type), remaining_entries)
    def _write_code_entries(self, chosen_entries, remaining_entries):
        FileUtil.write_rows_to_csv_file(code_csv_filename(self._split_percent, self._dataset, self._tracelink_type), chosen_entries)
        FileUtil.write_rows_to_csv_file(code_csv_filename(Util.complement(self._split_percent), self._dataset, self._tracelink_type), remaining_entries)
    
    def _write_labeled_tracelinks(self, labeled_trace_pairs):
        FileUtil.write_rows_to_csv_file(labeled_tracelink_csv_filename(self._split_percent, self._dataset, self._tracelink_type), labeled_trace_pairs)
    
    def _extract_data(self, all_files, chosen_filenames, remaining_filenames, extract_func, write_func, csv_header, tokenizer, preprocessor):
        """
        Extract and preprocesses the actual code or req files and adds them to the train or 
        test set according to self._chosen_filenames and self._remaining_filenames
        """
        chosen_entries = [csv_header] # files chosen for train set
        remaining_entries = [csv_header] # files chosen for test set
        a = 0
        b = 0
        a_no_entries = []
        b_no_entries = []
        for file_path in all_files:
            filename = FileUtil.get_filename_without_extension__from_path(file_path)
            file_representation = tokenizer.tokenize(file_path)
            file_representation.preprocess(preprocessor)
            entries = extract_func(file_representation, filename)
            
            
            if filename in chosen_filenames:
                if not entries:
                    a += 1
                    a_no_entries.append(filename)
                chosen_entries += entries
            if filename in remaining_filenames:
                if not entries:
                    b += 1
                    b_no_entries.append(filename)
                remaining_entries += entries
        log.info("    {} of {} chosen_filenames have no entries: {}".format(a, len(chosen_filenames), a_no_entries))
        log.info("    {} of {} remaining_filenames have no entries: {}".format(b, len(remaining_filenames), b_no_entries))
        
        write_func(chosen_entries, remaining_entries)
        return chosen_entries, remaining_entries
        
    def extract_labeled_tracelinks(self):
        """
        Creates the labeled csv files that are taken as input for the bert training
        """
        self._choose_files()
        chosen_req_entries, remaining_req_entries = self._extract_data(self._all_req_files, self._chosen_req_filenames,
                                                                        self._remaining_req_filenames, self._extract_req_entries,
                                                                        self._write_req_entries, REQ_FILE_CSV_HEADER,
                                                                        self._req_tokenizer, self._req_preprocessor)
        chosen_code_entries, remaining_code_entries = self._extract_data(self._all_code_files, self._chosen_code_filenames,
                                                                          self._remaining_code_filenames, self._extract_code_entries,
                                                                          self._write_code_entries, CODE_FILE_CSV_HEADER,
                                                                          self._code_tokenizer, self._code_preprocessor)
        
        self.combine_all_entries(chosen_req_entries, chosen_code_entries)
        # dont combine remaining entries -> not necessary for bert training or testing
        self._remaining_trace_matrix.write_trace_matrix(remaining_trace_matrix_filename(Util.complement(self._split_percent), self._dataset, self._tracelink_type))
        
    
    def combine_all_entries(self, req_entries, code_entries): # req_entries and code_entries are lists of csv-rows
        labeled_trace_pairs = [TRACE_LINK_FILE_CSV_HEADER]
        valid_links_count = 0
        invalid_links_count = 0
        for req in req_entries[1:]: # Omit header row
            req_key = req[1] # Req file name
            for code_entry in code_entries[1:]: # Omit header row
                code_key = code_entry[1] # code file name
                if self._chosen_trace_matrix.contains_req_code_pair(req_key, code_key):
                    valid_links_count += 1
                    labeled_trace_pairs.append([req[0], code_entry[0], VALID_TRACE_LINK_LABEL,
                                                 req_key, code_key, self._dataset.name()])
                else:
                    invalid_links_count += 1
                    labeled_trace_pairs.append([req[0], code_entry[0], INVALID_TRACE_LINK_LABEL,
                                                 req_key, code_key, self._dataset.name()])
        self._write_labeled_tracelinks(labeled_trace_pairs)
        log.info(self._dataset.name() + " Valid links: {} ({:.2}%), invalid: {}".format(valid_links_count, 100 * valid_links_count / (valid_links_count + invalid_links_count), invalid_links_count))
            
        
class SplitDataCreator(LabeledDataCreator):
    """
    Superclass for creators who randomly splits the data in train and test set by % valid solution trace links.
    Splits files with no valid trace links in the same proportion and adds them to the train and test sets.
    """
    def _choose_files(self) -> ([str], [str]):        
        all_valid_trace_links = self._solution_matrix.get_all_trace_links()
        chosen_links, remaining_links = Util.sample_random_percent(all_valid_trace_links, self._split_percent, self._seed)
        for c_link in chosen_links:
            self._chosen_trace_matrix.add_trace_pair(c_link[0], c_link[1])
        for r_link in remaining_links:
            self._remaining_trace_matrix.add_trace_pair(r_link[0], r_link[1])
        self._chosen_req_filenames.update([link[0] for link in chosen_links])
        self._chosen_code_filenames.update([link[1] for link in chosen_links])
        self._remaining_req_filenames.update([link[0] for link in remaining_links])
        self._remaining_code_filenames.update([link[1] for link in remaining_links])
        log.info("{}: {} total req files, {} total code_files".format(
            self._dataset.name(), len(self._all_req_files), len(self._all_code_files)))
        log.info("    {}/{} (chosen/remaining) valid trace links".format(
            len(chosen_links), len(remaining_links)))
        # Add req and code files with no valid trace links
        all_req_filenames = list(map(lambda x: FileUtil.get_filename_without_extension__from_path(x), self._all_req_files))
        no_tracelink_req_files = [f_name for f_name in all_req_filenames if f_name not in self._chosen_req_filenames and 
                                  f_name not in self._remaining_req_filenames]
        all_code_filenames = list(map(lambda x: FileUtil.get_filename_without_extension__from_path(x), self._all_code_files))
        no_tracelink_code_files = [f_name for f_name in all_code_filenames if f_name not in self._chosen_code_filenames and 
                                  f_name not in self._remaining_code_filenames]
        
        
        chosen_reqs, remaining_reqs = Util.sample_random_percent(no_tracelink_req_files, self._split_percent, self._seed)
        chosen_code, remaining_code = Util.sample_random_percent(no_tracelink_code_files, self._split_percent, self._seed)
        self._chosen_req_filenames.update(chosen_reqs)
        self._remaining_req_filenames.update(remaining_reqs)
        self._chosen_code_filenames.update(chosen_code)
        self._remaining_code_filenames.update(remaining_code)
        
        log.info("    Additional files without valid trace links:")
        log.info("    {} req_files, {} code_files without trace link".format(len(no_tracelink_req_files),
                                                                              len(no_tracelink_code_files)))
        log.info("    {} to chosen req files, {} to remaining req files".format(len(chosen_reqs), len(remaining_reqs)))
        log.info("    {} to chosen code files, {} to remaining code files".format(len(chosen_code), len(remaining_code)))
        
        log.info("    total files lengths:")
        log.info("    {} chosen req files, {} remaining req files".format(len(self._chosen_req_filenames), len(self._remaining_req_filenames)))
        log.info("    {} chosen code files, {} remaining code files".format(len(self._chosen_code_filenames), len(self._remaining_code_filenames)))
        
        
class TraceLinkCreatorWithReqSentences(SplitDataCreator):
    """
    Super class for creators who uses requirement sentences for the requirement side
    """
    def _extract_req_entries(self, file_representation, filename):
        return [[req, filename, self._dataset.name()] for req in file_representation.token_list]
    
    
class SentenceDataCreator(TraceLinkCreatorWithReqSentences):
    """
    Requirements as sentences, code file as comment sentences, classes with no comments are ignored
    """
    def __init__(self, split_percent, dataset: Dataset, seed):
        super(SentenceDataCreator, self).__init__(split_percent, dataset, seed)
        self._tracelink_type = TraceLinkType.sentence_tracelinks
    
    def _extract_code_entries(self, file_representation, filename):
        all_comments_in_class = file_representation.get_all_comment_tokens_as_list()
        return [[comment, filename, self._dataset.name()] for comment in all_comments_in_class]
    
class IdentifierDataCreator(TraceLinkCreatorWithReqSentences):
    """
    Requirement as sentences, code files as union of method names and class name (no concatenation)
    """
    def __init__(self, split_percent, dataset: Dataset, seed):
        super(IdentifierDataCreator, self).__init__(split_percent, dataset, seed)
        self._code_tokenizer = JavaCodeASTTokenizer()
        self._code_preprocessor = Preprocessor([CamelCaseSplitter(True), NonLetterFilter()])
        self._tracelink_type = TraceLinkType.identifier_tracelinks
        
    def _extract_code_entries(self, file_representation, filename):
        all_identifiers_in_class = []
        for classifier in file_representation.classifiers:
            all_identifiers_in_class.append([" ".join(classifier.get_name_words()), filename, self._dataset.name()])
            for meth in classifier.methods:
                all_identifiers_in_class.append([" ".join(meth.get_name_words()), filename, self._dataset.name()])
        return all_identifiers_in_class
    
        
class ClassNamePrefixIdentifierDataCreator(IdentifierDataCreator):
    def __init__(self, split_percent, dataset: Dataset, seed):
        """
        Requirements as sentences, Code as method names with its class name as concatenated prefix
        """
        super(ClassNamePrefixIdentifierDataCreator, self).__init__(split_percent, dataset, seed)
        self._tracelink_type = TraceLinkType.classname_prefix
        
    def _extract_code_entries(self, file_representation, filename):
        all_identifiers_in_class = []
        for classifier in file_representation.classifiers:
            class_name = classifier.get_name_words()
            if not classifier.methods: # no methods
                all_identifiers_in_class.append([" ".join(class_name), filename, self._dataset.name()])# class name as replacement entry
            else:
                for meth in classifier.methods:
                    prefixed_method_name = class_name + meth.get_name_words()
                    all_identifiers_in_class.append([" ".join(prefixed_method_name), filename, self._dataset.name()])
        return all_identifiers_in_class
        

class FakeSentenceDataCreator(SentenceDataCreator):
    """
    like SentenceDataCreator, but removes punctuation marks at the end of the comment or req sentence
    """
    def __init__(self, all_req_files, all_code_files, split_percent, dataset: Dataset, seed):
        super(FakeSentenceDataCreator, self).__init__(split_percent, dataset, seed)
        self._req_preprocessor = Preprocessor([Separator(True), CamelCaseSplitter(True), NonLetterFilter(), DuplicateWhiteSpaceFilter(), RemoveLastPunctuatioMark()])
        self._code_preprocessor = Preprocessor([JavaDocFilter(), Separator(True), CamelCaseSplitter(True), NonLetterFilter(), DuplicateWhiteSpaceFilter(), RemoveLastPunctuatioMark()])
        self._tracelink_type = TraceLinkType.fake_sentence


class WholeCommentTDataCreator(SplitDataCreator):
    
    def __init__(self, all_req_files, all_code_files, split_percent, dataset: Dataset, seed):
        super(WholeCommentTDataCreator, self).__init__(split_percent, dataset, seed)
        self._tracelink_type = TraceLinkType.whole_comment
    
    def _extract_code_entries(self, file_representation, filename):
        all_comments_in_class = file_representation.get_all_whole_comment_strings_as_list()
        return [[comment, filename, self._dataset.name()] for comment in all_comments_in_class]
    def _extract_req_entries(self, file_representation, filename):
        return [[" ".join(file_representation.token_list), filename, self._dataset.name()]]
    
    
"""
seed = 43

WholeCommentTDataCreator(0.8, Etour(), seed).extract_labeled_tracelinks()
"""


