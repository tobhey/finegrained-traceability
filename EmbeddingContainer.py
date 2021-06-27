from _operator import sub
import abc, logging

import pathlib

import FileUtil
import Util

log = logging.getLogger(__name__)


class EmbeddingContainer(abc.ABC):
    """
    An abstract base class to represent embeddings as objects
    
    Attributes
    ----------
    emb_id : int 
        id that identifies the embbedding
        
    file_path : str
        the file_path of the file that is represented with this embedding
        
    file_vector : ndarray
        The embedding vector for the whole file (e.g. requirements or class embedding)
        
    """
    FILE_PATH = "file_path"
    FILE_VECTOR = "file_vector"
    
    def __init__(self, file_path, file_vector=None):
        self.file_path = file_path
        self.file_vector = file_vector
        self.file_name = FileUtil.get_filename_from_path(self.file_path)
    
    def __repr__(self):
        return str(self.file_name) + " " + str(self.file_vector)
    

class RequirementEmbeddingContainer(EmbeddingContainer):
    REQUIREMENT_ELEMENT_VECTORS = "requirement_element_vectors"
    """
    Represents a requirement file
    """

    def __init__(self, file_path, file_vector, requirement_element_vectors):
        super(RequirementEmbeddingContainer, self).__init__(file_path, file_vector)
        self.requirement_element_vectors = requirement_element_vectors


class ClassEmbeddingContainer(EmbeddingContainer):
    
    CLASSNAME = "class_name"
    METHOD_DICT = "method_dict"
    FILE_PATH = "file_path"
    NON_CG_DICT = "non_cg_dict"
    CLASS_NAME_VOTER = "class_name_voter"
    
    def __init__(self, file_path, file_vector, class_name):
        """
        contains  a dictionary that identifies the contained methods by their name and parameters:
        methods_dict["method_name(param_type_list)"] = (method_vector, [([sim1, sim2...], req1), ([sim1, sim2...], req2), ...])]
                                        }
        """
        super().__init__(file_path, file_vector)
        self.class_name = class_name
        self.methods_dict = {}  # self.methods_dict[method_key] = vector
        # self.methods_dict = {} # (vector, [] ) embedding vector and list of tuples (similarity, requirement_name) per method
        self.non_cg_dict = {}  # like methods_dict, but for class elements without call graph property that participate in majority decision
        
    def check_class_name(self, class_name):
        return self.class_name == class_name
    
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
        
