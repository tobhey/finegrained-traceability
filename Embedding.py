import abc, logging
import pathlib
from _operator import sub
import FileUtil
import Util

log = logging.getLogger(__name__)

class Embedding(abc.ABC):
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
        
    

class RequirementEmbedding(Embedding):
    REQUIREMENT_ELEMENT_VECTORS = "requirement_element_vectors"
    """
    Represents a requirement file
    """
    def __init__(self, file_path, file_vector, requirement_element_vectors):
        super(RequirementEmbedding, self).__init__(file_path, file_vector)
        self.requirement_element_vectors = requirement_element_vectors

    def to_json(self):
        emb_dict = super(RequirementEmbedding, self).to_json(self)
        emb_dict[self.REQUIREMENT_ELEMENT_VECTORS] = [vec.tolist() for vec in self.requirement_element_vectors]
        return emb_dict
    
    @classmethod
    def from_json(cls, emb_dict):
        vector = Util.numpy_array(emb_dict[cls.VECTOR]) if emb_dict[cls.VECTOR] else None
        requirement_element_vectors = [Util.numpy_array(v) for v in emb_dict[cls.REQUIREMENT_ELEMENT_VECTORS]]
        return cls(pathlib.Path(emb_dict[cls.FILE_PATH]), vector, requirement_element_vectors)

class MockEmbedding(Embedding):
    """
    Contains no embedding vector, but encapsulates a file_representation
    """
    def __init__(self, file_path, file_representation):
        super(MockEmbedding, self).__init__(file_path, None)
        self.file_representation = file_representation
        
    def to_json(self):
        """
        No point to precalculate and save MockEmbeddings since it does not contain actual embedding vectors
        """
        log.error(f"MockEmbeddings can't be persisted to json")
    
    @classmethod
    def from_json(cls, emb_dict):
        log.error(f"Can't load MockEmbeddings from json")
    
class ClassEmbedding(Embedding):
    
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
        super(ClassEmbedding, self).__init__(file_path, file_vector)
        self.class_name = class_name
        self.methods_dict # (vector, [] ) embedding vector and list of tuples (similarity, requirement_name) per method
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
            if method_dict_key in self.methods_sims_dict:
                self.methods_sims_dict[method_dict_key].append((similarity, req_name))
            else:
                self.methods_sims_dict[method_dict_key] = [(similarity, req_name)]
        else:
            log.info("{} is not in {} methods_dict".format(method_dict_key, self.class_name))
    
    def add_method_vector_and_sim(self, method_dict_key, vector, similarity, req_name):
        self.methods_dict[method_dict_key] = vector
        self.add_method_sim(method_dict_key, similarity, req_name)
        
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
            
    def add_non_cg_vector_and_sim(self, dict_key, vector, similarity, req_name):
        self.non_cg_dict[dict_key] = vector
        self.add_non_cg_sim(dict_key, similarity, req_name)
        
    def to_json(self):
        embedding_dict = super(ClassEmbedding, self).to_json()
        embedding_dict[self.CLASSNAME] = self.class_name
        json_conform_sims_dict = {}
        for method_key in self.methods_sims_dict:
            json_conform_sims_dict[method_key] = [list(tup) for tup in self.methods_sims_dict[method_key]]
        embedding_dict[self.METHOD_SIMS_DICT] = json_conform_sims_dict
        
        json_conform_methods_dict = {}
        for method_key in self.methods_dict: # change ndarrays to list
            json_conform_methods_dict[method_key] = self.methods_dict[method_key].tolist()
        embedding_dict[self.METHOD_DICT] = json_conform_methods_dict
        
        json_conform_non_method_sims_dict = {}
        for key in self.non_cg_sims_dict:
            json_conform_non_method_sims_dict[key] = [list(tup) for tup in self.non_cg_sims_dict[key]]
        embedding_dict[self.NON_CG_SIMS_DICT] = json_conform_non_method_sims_dict
        
        json_conform_non_cg_dict = {}
        for key in self.non_cg_dict: # change ndarrays to list
            json_conform_non_cg_dict[key] = self.non_cg_dict[key].tolist()
        embedding_dict[self.NON_CG_DICT] = json_conform_non_cg_dict
        
        return embedding_dict
        
    @classmethod
    def from_json(cls, embedding_dict):
        method_dict = {}
        json_method_dict = embedding_dict[cls.METHOD_DICT]
        for method_key in json_method_dict:
            method_dict[method_key] = Util.numpy_array(json_method_dict[method_key])
        instance = cls(pathlib.Path(embedding_dict[cls.FILE_PATH]), embedding_dict[cls.FILE_VECTOR], embedding_dict[cls.CLASSNAME], method_dict)
        json_sims_dict = embedding_dict[cls.METHOD_SIMS_DICT]
        for method_key in json_sims_dict:
            for tup in json_sims_dict[method_key]:
                instance.add_method_sim(method_key, tup[0], tup[1])
        
        json_non_method_dict = {}        
        if cls.NON_CG_DICT in embedding_dict:
            json_non_method_dict = embedding_dict[cls.NON_CG_DICT]
            
        non_cg_dict = {}
        for key in json_non_method_dict:
            non_cg_dict[key] = Util.numpy_array(json_non_method_dict[key])
        instance.non_cg_dict = non_cg_dict
        if non_cg_dict:
            json_non_method_sims_dict = embedding_dict[cls.NON_CG_SIMS_DICT]
            for key in json_non_method_sims_dict:
                for tup in json_non_method_sims_dict[key]:
                    instance.add_non_cg_sim(key, tup[0], tup[1])
        return instance
