from abc import ABC, abstractmethod
import logging

from TwoDimensionalMatrix import TwoDimensionalMatrix
from precalculating.AllTraceLinkCombinations import AllTraceLinkCombinations, \
    AllFileLevelTraceLinkCombinations

log = logging.getLogger(__name__)


class AllTraceLinkCombinationsBuilder(ABC):
    """
    The AllTraceLinkCombinationsBuilder takes requirement and class embedding containers and creates an AllTraceLinkCombinations object.
    """

    def __init__(self, _req_embedding_containers, code_embedding_containers, similarity_function):
        self._req_embedding_containers = _req_embedding_containers
        self._code_embedding_containers = code_embedding_containers
        self._similarity_function = similarity_function
        
    @abstractmethod
    def build(self) -> AllTraceLinkCombinations: 
        pass


class AllFileLevelTraceLinkCombinationsBuilder(AllTraceLinkCombinationsBuilder):
    
    def build(self):
        
        similarity_matrix = TwoDimensionalMatrix.create_empty()
        
        for code_emb_cont in self._code_embedding_containers:
            code_file_name = code_emb_cont.file_name
            code_vector = code_emb_cont.file_vector
            for req_emb_cont in self._req_embedding_containers:
                file_level_similarity = self._similarity_function(req_emb_cont.file_vector, code_vector)
                similarity_matrix.set_value(req_emb_cont.file_name, code_file_name, file_level_similarity)
        
        return AllFileLevelTraceLinkCombinations(similarity_matrix)
