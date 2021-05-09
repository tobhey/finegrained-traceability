import abc, logging
import pathlib
from _operator import sub
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
    
    def __init__(self, file_path, file_vector= None):
        self.file_path = file_path
        self.file_vector = file_vector
        self.file_name = FileUtil.get_filename_from_path(self.file_path)
    
    def __repr__(self):
        return str(self.file_name) + " " + str(self.file_vector)
    
    def to_json(self):
        emb_dict = {}
        emb_dict[self.FILE_PATH] = str(self.file_path)
        emb_dict[self.FILE_VECTOR] = self.file_vector.tolist() if self.file_vector is not None else None
        return emb_dict
        
    @classmethod
    def from_json(cls, emb_dict):
        file_vector = Util.numpy_array(emb_dict[cls.FILE_VECTOR]) if emb_dict[cls.FILE_VECTOR] else None
        return cls(pathlib.Path(emb_dict[cls.FILE_PATH]), file_vector)
        
    

class RequirementEmbeddingContainer(EmbeddingContainer):
    REQUIREMENT_ELEMENT_VECTORS = "requirement_element_vectors"
    """
    Represents a requirement file
    """
    def __init__(self, file_path, file_vector, requirement_element_vectors):
        super(RequirementEmbeddingContainer, self).__init__(file_path, file_vector)
        self.requirement_element_vectors = requirement_element_vectors

    def to_json(self):
        emb_dict = super(RequirementEmbeddingContainer, self).to_json(self)
        emb_dict[self.REQUIREMENT_ELEMENT_VECTORS] = [vec.tolist() for vec in self.requirement_element_vectors]
        return emb_dict
    
    @classmethod
    def from_json(cls, emb_dict):
        vector = Util.numpy_array(emb_dict[cls.VECTOR]) if emb_dict[cls.VECTOR] else None
        requirement_element_vectors = [Util.numpy_array(v) for v in emb_dict[cls.REQUIREMENT_ELEMENT_VECTORS]]
        return cls(pathlib.Path(emb_dict[cls.FILE_PATH]), vector, requirement_element_vectors)

class MockEmbeddingContainer(EmbeddingContainer):
    """
    Contains no embedding vector, but encapsulates a file_representation
    """
    def __init__(self, file_path, file_representation):
        super(MockEmbeddingContainer, self).__init__(file_path, None)
        self.file_representation = file_representation
        
    def to_json(self):
        """
        No point to precalculate and save MockEmbeddings since it does not contain actual embedding vectors
        """
        log.error(f"MockEmbeddings can't be persisted to json")
    
    @classmethod
    def from_json(cls, emb_dict):
        log.error(f"Can't load MockEmbeddings from json")
    
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
        super(ClassEmbeddingContainer, self).__init__(file_path, file_vector)
        self.class_name = class_name
        self.methods_dict = {} # self.methods_dict[method_key] = vector
        #self.methods_dict = {} # (vector, [] ) embedding vector and list of tuples (similarity, requirement_name) per method
        self.non_cg_dict = {} # like methods_dict, but for class elements without call graph property that participate in majority decision
        
    def check_class_name(self, class_name):
        return self.class_name == class_name
    
    def get_method_vector(self, method_dict_key):
        if method_dict_key in self.methods_dict:
            return self.methods_dict[method_dict_key]
        log.debug("{} is not in {} methods_dict".format(method_dict_key, self.class_name))
        return None
    
    def get_method_sims(self, method_dict_key):
        if method_dict_key in self.methods_sims_dict:
            assert method_dict_key in self.methods_dict
            return self.methods_sims_dict[method_dict_key]
        log.debug("{} of is not in {} methods_sims_dict".format(method_dict_key, self.class_name))
        return None
    
    def add_method_sim(self, method_dict_key, similarity, req_name):
        assert isinstance(similarity, list)
        if method_dict_key in self.methods_dict:
            self.methods_dict[method_dict_key][1].append((similarity, req_name))
        else:
            self.methods_dict[method_dict_key] = (None, [(similarity, req_name)])
    
    def add_method_vector_and_sim(self, method_dict_key, vector, similarity, req_name):
        if vector:
            self.set_method_vector(method_dict_key, vector)
        if similarity and req_name:
            self.add_method_sim(method_dict_key, similarity, req_name)
        
    def set_method_vector(self, method_dict_key, vector):
        if method_dict_key in self.methods_dict:
            if self.methods_dict[method_dict_key][0]:
                log.info(f"Caution: Overwriting existing method vector of {method_dict_key} in {self.class_name}")
            self.methods_dict[method_dict_key][0] = vector
        else:
            self.methods_dict[method_dict_key] = (vector, [])
            
    def get_non_cg_vector(self, dict_key):
        if dict_key in self.non_cg_dict:
            return self.non_cg_dict[dict_key]
        log.info(f"No entry in non method dict with the key {dict_key}")
        
    def get_non_cg_sim(self, dict_key):
        if dict_key in self.non_cg_sims_dict:
            return self.non_cg_sims_dict[dict_key]
        log.info(f"No entry in non method sims dict with the key {dict_key}")
        return None
        
    def add_non_cg_sim(self, dict_key, similarity, req_name):
        assert isinstance(similarity, list)
        if dict_key in self.non_cg_sims_dict:
            self.non_cg_sims_dict[dict_key].append((similarity, req_name))
        else:
            self.non_cg_sims_dict[dict_key] = [(similarity, req_name)]
            
    def set_non_cg_vector(self, key, vector):
        if key in self.non_cg_dict:
            if self.non_cg_dict[key]:
                log.info(f"Caution: Overwriting existing element vector of {key} in {self.class_name}")
            self.non_cg_dict[key][0] = vector
        else:
            self.non_cg_dict[key] = (vector, [])
            
    def add_non_cg_vector_and_sim(self, key, vector, similarity, req_name):
        if vector:
            self.set_non_cg_vector(key, vector)
        if similarity and req_name:
            self.add_non_cg_sim(key, similarity, req_name)
        
    def has_method_entries(self):
        return bool(self.methods_dict)
        
    def to_json(self):
        embedding_dict = super(ClassEmbeddingContainer, self).to_json()
        embedding_dict[self.CLASSNAME] = self.class_name
        
        json_conform_methods_dict = {}
        for method_key in self.methods_dict: # change ndarrays and tuples to list
            json_conform_methods_dict[method_key] = [self.methods_dict[method_key][0].tolist()]
            json_conform_methods_dict[method_key].append([list(sim_tuple) for sim_tuple in self.methods_dict[method_key][1]])
        embedding_dict[self.METHOD_DICT] = json_conform_methods_dict
        
        json_conform_non_cg_dict = {}
        for key in self.non_cg_dict: # change ndarrays and tuples to list
            json_conform_non_cg_dict[key] = [self.non_cg_dict[key][0].tolist()]
            json_conform_non_cg_dict[key].append([list(sim_tuple) for sim_tuple in self.non_cg_dict[key][1]]) 
        embedding_dict[self.NON_CG_DICT] = json_conform_non_cg_dict
        
        return embedding_dict
        
    @classmethod
    def from_json(cls, embedding_dict):
        instance = cls(pathlib.Path(embedding_dict[cls.FILE_PATH]), embedding_dict[cls.FILE_VECTOR], embedding_dict[cls.CLASSNAME])
        
        for method_key, value in embedding_dict[cls.METHOD_DICT].items():
            instance.set_method_vector(method_key, Util.numpy_array(value[0]))
            for sim_eintry in value[0]:
                instance.add_method_sim(sim_eintry[0], sim_eintry[1])
        
        for key, value in embedding_dict[cls.NON_CG_DICT].items():
            instance.set_non_cg_vector(key, Util.numpy_array(value[0]))
            for sim_eintry in value[0]:
                instance.add_non_cg_sim(sim_eintry[0], sim_eintry[1])
        return instance
