import logging
from abc import ABC

from pathlib import Path

from embeddingCreator.EmbeddingContainer import RequirementEmbeddingContainer
from embeddingCreator.EmbeddingCreator import EmbeddingCreator
from preprocessing.FileRepresentation import UseCaseFileRepresentation
from utility import Util

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)

PREPROCESSED_REQ_OUTPUT_DIR = Path(__file__).parent.parent / "output/Preprocessed_Req"


class RequirementEmbeddingCreator(EmbeddingCreator, ABC):

    def __init__(self, requirements_word_chooser, preprocessor, wordemb_creator,
                  tokenizer, preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super().__init__(preprocessor, wordemb_creator, tokenizer, preprocessed_token_output_directory)
        self._requirements_word_chooser = requirements_word_chooser

    
class UCEmbeddingCreator(RequirementEmbeddingCreator):
    
    def _create_embeddings(self, file_representation: UseCaseFileRepresentation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        chosen_word_groups_embeddings = []
        requirement_element_vectors = []
        element_vectors = {}
        for word_group_id in chosen_word_groups:
            word_embeddings = self._create_word_embeddings_from_word_list(chosen_word_groups[word_group_id])
            chosen_word_groups_embeddings.append(word_embeddings)
            avg_vector = Util.create_averaged_vector(word_embeddings)
            if avg_vector is not None:
                requirement_element_vectors.append(avg_vector)
                element_vectors[word_group_id] = [avg_vector]
            
        #file_vector = [Util.create_averaged_vector([embedding for embeddings in chosen_word_groups_embeddings for embedding in embeddings])]  # flat average over all (nested) word embeddings in chosen_word_groups
        file_vector = [Util.create_averaged_vector(requirement_element_vectors)] if requirement_element_vectors else [] # flat average over all (nested) word embeddings in chosen_word_groups

        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, element_vectors)


class RequirementVectorEmbeddingCreator(RequirementEmbeddingCreator):
    
    def _create_embeddings(self, file_representation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        #chosen_word_groups_embeddings = []
        #requirement_element_vectors = []
        element_vectors = {}
        for word_group_id in chosen_word_groups:
            word_embeddings = self._word_embedding_creator.create_word_list_embedding(chosen_word_groups[word_group_id])
            #chosen_word_groups_embeddings.append(word_embeddings)
            #avg_vector = Util.create_averaged_vector(word_embeddings)
            #if avg_vector is not None:
            #    requirement_element_vectors.append(avg_vector)
            element_vectors[word_group_id] = word_embeddings

        file_vector = []# [Util.create_averaged_vector(requirement_element_vectors)] if requirement_element_vectors else [] # flat average over all (nested) word embeddings in chosen_word_groups

        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, element_vectors)

        #chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        #requirement_element_vectors = []
        #for word_group_id in chosen_word_groups:
        #    word_group_embedding = self._word_embedding_creator.create_word_list_embedding(chosen_word_groups[word_group_id])
        #    requirement_element_vectors.append(word_group_embedding)
            
        #file_vector = []
        #return RequirementEmbeddingContainer(file_representation.file_path, file_vector, requirement_element_vectors)

class RequirementBOEEmbeddingCreator(RequirementEmbeddingCreator):
    
    def _create_embeddings(self, file_representation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        chosen_word_groups_embeddings = []
        requirement_element_vectors = []
        element_vectors = {}
        for word_group_id in chosen_word_groups:
            word_embeddings = self._word_embedding_creator.create_word_list_embedding_boe(chosen_word_groups[word_group_id])
            chosen_word_groups_embeddings.append(word_embeddings)
            avg_vector = Util.create_averaged_vector(word_embeddings)
            if avg_vector is not None:
                requirement_element_vectors.append(avg_vector)
                element_vectors[word_group_id] = [avg_vector]

        file_vector = [Util.create_averaged_vector(requirement_element_vectors)] if requirement_element_vectors else [] # flat average over all (nested) word embeddings in chosen_word_groups

        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, element_vectors)

       # chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        #requirement_element_vectors = []
        #for word_group in chosen_word_groups:
        #    word_group_embedding = self._word_embedding_creator.create_word_list_embedding_boe(word_group)
        #    requirement_element_vectors.append(word_group_embedding)
                
        #file_vector = []
        #return RequirementEmbeddingContainer(file_representation.file_path, file_vector, requirement_element_vectors)
    
class MockUCEmbeddingCreator(RequirementEmbeddingCreator):

    def _create_embeddings(self, file_representation: UseCaseFileRepresentation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        file_vector = [word for word_group in chosen_word_groups for word in chosen_word_groups[word_group]]  # flat list over all (nested) words in chosen_word_groups
        
        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, chosen_word_groups)
    
