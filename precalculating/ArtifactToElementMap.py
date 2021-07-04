from utility import FileUtil


class ArtifactToElementMap:
    """
    This is part of an ElementLevelTracelinkDataStructure and contains the mappings from code files to its containing code elements
    and from requirement files to its containing requirement elements (e. g. sentences, represented by an id)
    You DON'T need to create it by yourself, the ElementLevelTracelinkDataStructureBuilder will do it automatically.
    
    ArtifactToElementMap contains 3 dictionaries:
    - REQ_FILE_TO_REQ_ELEMENT_ID_MAP: key = Requirement file name; value = list of Requirement element ids (custom ids for each requirement sentence)
    - CODE_FILE_TO_METHOD_MAP: key = Code file name; value = list of method keys. For the method key shape see CallgraphUtil.build_class_method_param_dict_key
    - CODE_FILE_TO_NON_CG_ELEMENT_MAP: key = Code file name; value = list of non call graph element keys. The key is defined by the used CodeEmbeddingCreator.
    
    """
    REQ_FILE_TO_REQ_ELEMENT_ID_MAP = "req_file_to_req_element_id_map"
    CODE_FILE_TO_METHOD_MAP = "code_file_to_method_map"
    CODE_FILE_TO_NON_CG_ELEMENT_MAP = "code_file_to_non_cg_element_map"
    
    def __init__(self, req_file_to_req_element_id_map, code_file_to_method_map, code_file_to_non_cg_element_map):
        self._req_file_to_req_element_id_map = req_file_to_req_element_id_map
        self._code_file_to_method_map = code_file_to_method_map
        self._code_file_to_non_cg_element_map = code_file_to_non_cg_element_map

        assert len(code_file_to_method_map) == len(code_file_to_non_cg_element_map)
        
    @classmethod
    def load_from(cls, file_path):
        loaded_json = FileUtil.read_from_json(file_path)
        return cls(loaded_json[cls.REQ_FILE_TO_REQ_ELEMENT_ID_MAP], loaded_json[cls.CODE_FILE_TO_METHOD_MAP], loaded_json[cls.CODE_FILE_TO_NON_CG_ELEMENT_MAP])
    
    def write_to(self, file_path):
        json_to_write = {self.REQ_FILE_TO_REQ_ELEMENT_ID_MAP : self._req_file_to_req_element_id_map,
                         self.CODE_FILE_TO_METHOD_MAP : self._code_file_to_method_map,
                         self.CODE_FILE_TO_NON_CG_ELEMENT_MAP : self._code_file_to_non_cg_element_map}
        FileUtil.write_to_json(file_path, json_to_write)
        
    def req_element_ids_of(self, req_file_name):
        return self._req_file_to_req_element_id_map[req_file_name]
    
    def method_keys_of(self, code_file_name):
        return self._code_file_to_method_map[code_file_name]
    
    def non_cg_keys_of(self, code_file_name):
        return self._code_file_to_non_cg_element_map[code_file_name]

    def all_code_elements_of(self, code_file_name):
        return self.method_keys_of(code_file_name) + self.non_cg_keys_of(code_file_name)

    def all_req_file_names(self):
        return self._req_file_to_req_element_id_map.keys()
    
    def all_code_file_names(self):
        return self._code_file_to_method_map.keys()
    
    def all_method_keys(self):
        return [method_key for method_keys_of_class in self._code_file_to_method_map.values() for method_key in method_keys_of_class]
    
    def all_non_cg_element_keys(self):
        return [non_cg_element_key for non_cg_element_keys_in_class in self._code_file_to_non_cg_element_map.values() for non_cg_element_key in non_cg_element_keys_in_class]

    def contains_method_key(self, method_key):
        return method_key in self.all_method_keys()
