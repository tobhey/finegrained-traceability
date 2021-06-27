"""
Use load_data_from() to load a existing precalculated file or an AllTraceLinkCombinationsBuilder to create from embeddings
"""

from abc import abstractmethod
import abc

from TwoDimensionalMatrix import TwoDimensionalMatrix
from precalculating.ArtifactToElementMap import ArtifactToElementMap


class TraceLinkDataStructure(abc.ABC):
    
    def __init__(self, similarity_matrix):
        self._similarity_matrix = similarity_matrix
    
    @classmethod
    def load_data_from(cls, file_path):
        return cls(TwoDimensionalMatrix.read_from_csv(file_path))
    
    def write_data(self, file_path):
        self._similarity_matrix.write_to_csv(file_path)
    
    @abstractmethod
    def all_req_file_names(self):
        pass

    @abstractmethod
    def all_code_file_names(self):
        pass
    
    @abstractmethod
    def similarity_between(self, req, code):
        """
        req and code can be files or file elements (depending on sub class)
        """
        pass
    
    
class FileLevelTraceLinkDataStructure(TraceLinkDataStructure):
    """
        Don't use the constructor to instantiate
        
        Rows = req file names; columns = code file names; entry = similarity
    """
    
    def all_req_file_names(self):
        return self._similarity_matrix.get_row_names()

    def all_code_file_names(self):
        return self._similarity_matrix.get_column_names()
    
    def similarity_between(self, req_file_name, code_file_name):
        return self._similarity_matrix.get_value(req_file_name, code_file_name)


class ElementLevelTraceLinkDataStructure(TraceLinkDataStructure):
    """
    Contains an artifact to element map with the mappings between code classes and their elements
    and a TwoDimensionalMatrix containing the actual similarity values
    """
    ARTIFACT_TO_ELEMENT_MAP_FILE_PATTERN = "{folder}/{filename}_a2eMap.json"
    
    def __init__(self, similarity_matrix, artifact_to_element_map):
        super().__init__(similarity_matrix)
        self._artifact_to_element_map = artifact_to_element_map
    
    @classmethod
    def load_data_from(cls, matrix_file_path, artifact_map_file_path):
        similarity_matrix = TwoDimensionalMatrix.read_from_csv(matrix_file_path)
        artifact_to_element_map = ArtifactToElementMap.load_from(artifact_map_file_path)
        return cls(similarity_matrix, artifact_to_element_map)
    
    def write_data(self, matrix_file_path, artifact_map_file_path):
        super().write_data(matrix_file_path)
        self._artifact_to_element_map.write_to(artifact_map_file_path)
    
    def all_req_file_names(self) -> [str]:
        return self._artifact_to_element_map.all_req_file_names()

    def all_code_file_names(self) -> [str]:
        return self._artifact_to_element_map.all_code_file_names()
    
    def methods_of(self, code_file_name) -> [str]:
        return self._artifact_to_element_map.method_keys_of(code_file_name)
    
    def non_cg_elements_of(self, code_file_name) -> [str]:
        return self._artifact_to_element_map.non_cg_keys_of(code_file_name)
    
    def all_code_elements_of(self, code_file_name) -> [str]:
        return self.methods_of(code_file_name) + self.non_cg_elements_of(code_file_name)
    
    def req_elements_of(self, req_file_name) -> [str]:
        return self._artifact_to_element_map.req_element_ids_of(req_file_name)
    
    def all_method_keys(self):
        return self._artifact_to_element_map.all_method_keys()
    
    def all_non_cg_element_keys(self):
        return self._artifact_to_element_map.all_non_cg_element_keys()
    
    def similarity_between(self, req_element, code_element) -> float:
        return self._similarity_matrix.get_value(req_element, code_element)
    
    def set_matrix(self, new_matrix: TwoDimensionalMatrix):
        self._similarity_matrix = new_matrix
