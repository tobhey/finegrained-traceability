import abc, logging

from utility import FileUtil

log = logging.getLogger(__name__)


class EmbeddingContainer(abc.ABC):
    """
    An abstract base class to represent embeddings as objects
    
    file_path : str
        the file_path of the file that is represented with this embedding
        
    file_vector : ndarray 
        The embedding vector for the whole file (e.g. requirements or class embedding)
        
    """
    
    def __init__(self, file_path, file_vector=None):
        self.file_path = file_path
        self.file_vector = file_vector
        self.file_name = FileUtil.get_filename_from_path(self.file_path)
    

class RequirementEmbeddingContainer(EmbeddingContainer):
    REQUIREMENT_ELEMENT_VECTORS = "requirement_element_vectors"
    """
    Represents a requirement file
    
    """

    def __init__(self, file_path, file_vector, requirement_element_vectors):
        super(RequirementEmbeddingContainer, self).__init__(file_path, file_vector)
        self.requirement_element_vectors = requirement_element_vectors


class ClassEmbeddingContainer(EmbeddingContainer):
    
    CLASS_NAME_VOTER = "class_name_voter"
    
    def __init__(self, file_path, file_vector, class_name):
        """
        contains two dictionary that contains the elements that should participate in a majority decision:
        - methods
        - code elements with no call graph property (e. g. class name)
        
        methods_dict["method_name(param_type_list)"] = method_vector
        non_cg_dict["non_cg_identifier"] = vector
                                        
        """
        super().__init__(file_path, file_vector)
        self.class_name = class_name
        self.methods_dict = {}  # self.methods_dict[method_key] = vector of method
        self.non_cg_dict = {}  # like methods_dict, but for class elements without call graph property that participate in majority decision
        
    def get_method_vector(self, method_dict_key):
        if method_dict_key in self.methods_dict:
            return self.methods_dict[method_dict_key]
        log.debug(f"{method_dict_key} is not in {self.class_name} methods_dict")
        return None
    
    def set_method_vector(self, method_dict_key, vector):
        if method_dict_key in self.methods_dict:
            log.info(f"Caution: Overwriting existing method vector of {method_dict_key} in {self.class_name}")
            self.methods_dict[method_dict_key] = vector
        else:
            self.methods_dict[method_dict_key] = vector
            
    def get_non_cg_vector(self, dict_key):
        if dict_key in self.non_cg_dict:
            return self.non_cg_dict[dict_key]
        log.info(f"No entry in non method dict with the key {dict_key}")
        
    def set_non_cg_vector(self, key, vector):
        if key in self.non_cg_dict:
            log.info(f"Caution: Overwriting existing element vector of {key} in {self.class_name}")
            self.non_cg_dict[key] = vector
        else:
            self.non_cg_dict[key] = vector
        
    def has_method_entries(self):
        return bool(self.methods_dict)
        
