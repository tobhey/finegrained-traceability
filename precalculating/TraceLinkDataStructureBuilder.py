from abc import ABC, abstractmethod
import logging

from TwoDimensionalMatrix import TwoDimensionalMatrix
from precalculating.ArtifactToElementMap import ArtifactToElementMap
from precalculating.TraceLinkDataStructure import TraceLinkDataStructure, \
    FileLevelTraceLinkDataStructure, ElementLevelTraceLinkDataStructure

log = logging.getLogger(__name__)


class TraceLinkDataStructureBuilder(ABC):
    """
    The TraceLinkDataStructureBuilder takes requirement and class embedding containers and creates an TraceLinkDataStructure object.
    """

    def __init__(self, req_embedding_containers, code_embedding_containers, similarity_function):
        self._req_embedding_containers = req_embedding_containers
        self._code_embedding_containers = code_embedding_containers
        self._similarity_function = similarity_function
        
    @abstractmethod
    def build(self) -> TraceLinkDataStructure: 
        pass


class FileLevelTraceLinkDataStructureBuilder(TraceLinkDataStructureBuilder):
    
    def build(self):
        
        similarity_matrix = TwoDimensionalMatrix.create_empty()
        
        for code_emb_cont in self._code_embedding_containers:
            code_file_name = code_emb_cont.file_name
            code_vector = code_emb_cont.file_vector
            for req_emb_cont in self._req_embedding_containers:
                file_level_similarity = self._similarity_function(req_emb_cont.file_vector, code_vector)
                similarity_matrix.set_value(req_emb_cont.file_name, code_file_name, file_level_similarity)
        
        return FileLevelTraceLinkDataStructure(similarity_matrix)


class ElementLevelTraceLinkDataStructureBuilder(TraceLinkDataStructureBuilder):

    def build(self):
        similarity_matrix = TwoDimensionalMatrix.create_empty()
        req_file_to_req_element_id_map, code_file_to_method_map, code_file_to_non_cg_element_map = {}, {}, {}
        
        for req_emb_cont in self._req_embedding_containers:
            req_file_to_req_element_id_map[req_emb_cont.file_name] = [self._build_req_element_key(req_emb_cont.file_name, index) for index, _ in enumerate(req_emb_cont.requirement_element_vectors)]
        
        num_code_files = len(self._code_embedding_containers)
        for i, code_emb_cont in enumerate(self._code_embedding_containers):
            log.info(f"precalculating for code file {i + 1} of {num_code_files}")
            similarity_matrix, method_keys, non_cg_elems = self._calculate_similarities_for_all_code_elements(similarity_matrix, req_emb_cont, code_emb_cont)
            code_file_to_method_map[code_emb_cont.file_name] = method_keys
            code_file_to_non_cg_element_map[code_emb_cont.file_name] = non_cg_elems
            
        artifact_to_element_map = ArtifactToElementMap(req_file_to_req_element_id_map, code_file_to_method_map, code_file_to_non_cg_element_map)

        return ElementLevelTraceLinkDataStructure(similarity_matrix, artifact_to_element_map)
        
    def _calculate_similarities_for_all_code_elements(self, similarity_matrix, req_emb_cont, code_emb_cont):
        similarity_matrix, method_keys = self._calculate_similarities_for_code_element(similarity_matrix, req_emb_cont, code_emb_cont.methods_dict)
        similarity_matrix, non_cg_elems = self._calculate_similarities_for_code_element(similarity_matrix, req_emb_cont, code_emb_cont.non_cg_dict)
        
        return similarity_matrix, method_keys, non_cg_elems
            
    def _calculate_similarities_for_code_element(self, similarity_matrix, req_emb_cont, element_dict):
        code_element_keys = []
        for code_element_key in element_dict:
            code_element_keys.append(code_element_key)
            element_vector = element_dict[code_element_key]
            similarity_matrix = self._calculate_similarities_to_all_req_elements(similarity_matrix, req_emb_cont, code_element_key, element_vector)
        
        return similarity_matrix, code_element_keys
    
    def _calculate_similarities_to_all_req_elements(self, similarity_matrix, req_emb_cont, element_key, element_vector):
        for req_emb_cont in self._req_embedding_containers:
            for index, req_element in enumerate(req_emb_cont.requirement_element_vectors):
                req_elem_key = self._build_req_element_key(req_emb_cont.file_name, index)
                similarity_matrix.set_value(req_elem_key, element_key, self._similarity_function(req_element, element_vector))
        return similarity_matrix
                
    def _build_req_element_key(self, req_file_name, index):
        return f"{req_file_name}.{index}"
            
