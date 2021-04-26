import FileUtil, logging
from Preprocessing.CodeFileRepresentation import CodeFileRepresentation
import Paths
from Dataset import Etour308
from Preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer

log = logging.getLogger(__name__)


def generate_classifer_to_file_map(dataset, tokenizer, output_file=None):
    """
    Precalculates a classifier -> file map and saves it to a json.
    
    Load the json with FileUtil.read_dict_from_json()
    """
    if not output_file:
        output_file = Paths.classifier_to_file_map_filename(dataset)
        
    classifier_to_file_map = {}
    for file in FileUtil.get_files_in_directory(dataset.code_folder()):
        code_file_representation = tokenizer.tokenize(file)
        assert isinstance(code_file_representation, CodeFileRepresentation), "use an appopiate tokenizer to generate a CodeFileRepresentation"
        file_name = FileUtil.get_filename_from_path(file) # with extension
        for classifier in code_file_representation.classifiers:
            if classifier.get_original_name() in classifier_to_file_map:
                log.info(f"Duplicate classifier name: {classifier.name} -> {file_name} overwrites {classifier.name} -> {classifier_to_file_map[classifier.name]}")
            classifier_to_file_map[classifier.get_original_name()] = file_name
    FileUtil.write_to_json(output_file, classifier_to_file_map)
    
generate_classifer_to_file_map(Etour308(), JavaCodeASTTokenizer())
    