import FileUtil


class ArtifactToElementMap:
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
        instance = cls()
        instance._req_file_to_req_element_id_map = loaded_json[cls.REQ_FILE_TO_REQ_ELEMENT_ID_MAP]
        instance._code_file_to_method_map = loaded_json[cls.CODE_FILE_TO_METHOD_MAP]
        instance._code_file_to_non_cg_element_map = loaded_json[cls.CODE_FILE_TO_NON_CG_ELEMENT_MAP]
        return instance
    
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

    def all_req_file_names(self):
        return self._req_file_to_req_element_id_map.keys()
    
    def all_code_file_names(self):
        return self._code_file_to_method_map.keys()
    
    def all_method_keys(self):
        return self._code_file_to_method_map.keys()
    
    def all_non_cg_element_keys(self):
        return self._code_file_to_non_cg_element_map.keys()
