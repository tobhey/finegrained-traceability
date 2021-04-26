import Paths, FileUtil, logging
from Preprocessing.CodeFileRepresentation import CodeFileRepresentation
from Dataset import Etour308
from Preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer

log = logging.getLogger(__name__)
    
def generate_inheritance_graph(dataset, tokenizer, output_file=None):
    """
    considers extend-relations
    Nodes = Code Files
    dict[file] = ([super classes], [sub classes])
    
    """
    if not output_file:
        output_file = Paths.inheritance_graph_filename(dataset)
    
    # Maps classifier name to its containing code file
    classifier_to_file_map = FileUtil.read_from_json(Paths.classifier_to_file_map_filename(dataset))
    inheritance_graph = {}
    for file in FileUtil.get_files_in_directory(dataset.code_folder()):
        code_file_representation = tokenizer.tokenize(file)
        assert isinstance(code_file_representation, CodeFileRepresentation), "use an appropiate tokenizer to generate a CodeFileRepresentation"
        super_classes = set()
        for classifier in code_file_representation.classifiers:
            for extended_classifier in classifier.get_extended_classifiers_plain_list():
                if not extended_classifier in classifier_to_file_map:
                    log.info(f"SKIP: Unknown super classifier (probably not part of {dataset.name()}): {extended_classifier}")
                    continue
                file_of_super_class = classifier_to_file_map[extended_classifier]
                super_classes.add(file_of_super_class)
                # Add sub class relation from super class' perspective
                if file_of_super_class in inheritance_graph:
                    inheritance_graph[file_of_super_class][1].add(code_file_representation.file_name)
                else: 
                    inheritance_graph[file_of_super_class] = (set(), {code_file_representation.file_name})
    
        if code_file_representation.file_name in inheritance_graph:
            inheritance_graph[code_file_representation.file_name][0].update(super_classes)
        else: 
            inheritance_graph[code_file_representation.file_name] = (super_classes, set())
    
    FileUtil.write_to_json(output_file, inheritance_graph)
    
def generate_implements_graph(dataset, tokenizer, output_file=None):
    """
    considers implements-relations
    Nodes = Code Files
    dict[file] = ([super classifier], [sub classifier])
    
    """
    if not output_file:
        output_file = Paths.implements_graph_filename(dataset)
    
    # Maps classifier name to its containing code file
    classifier_to_file_map = FileUtil.read_from_json(Paths.classifier_to_file_map_filename(dataset))
    implements_graph = {}
    for file in FileUtil.get_files_in_directory(dataset.code_folder()):
        code_file_representation = tokenizer.tokenize(file)
        assert isinstance(code_file_representation, CodeFileRepresentation), "use an appropiate tokenizer to generate a CodeFileRepresentation"
        super_classes = set()
        for classifier in code_file_representation.classifiers:
            for implemented_classifier in classifier.get_implemented_classifiers_plain_list():
                if not implemented_classifier in classifier_to_file_map:
                    log.info(f"SKIP: Unknown super classifier (probably not part of {dataset.name()}): {implemented_classifier}")
                    continue
                file_of_super_classifier = classifier_to_file_map[implemented_classifier]
                super_classes.add(file_of_super_classifier)
                # Add sub class relation from super class' perspective
                if file_of_super_classifier in implements_graph:
                    implements_graph[file_of_super_classifier][1].add(code_file_representation.file_name)
                else: 
                    implements_graph[file_of_super_classifier] = (set(), {code_file_representation.file_name})
    
        if code_file_representation.file_name in implements_graph:
            implements_graph[code_file_representation.file_name][0].update(super_classes)
        else: 
            implements_graph[code_file_representation.file_name] = (super_classes, set())
    
    FileUtil.write_to_json(output_file, implements_graph)
    
#generate_inheritance_graph(Etour308(), JavaCodeASTTokenizer())
generate_implements_graph(Etour308(), JavaCodeASTTokenizer())