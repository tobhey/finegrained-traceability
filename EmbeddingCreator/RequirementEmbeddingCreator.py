import logging, Util

from EmbeddingContainer import RequirementEmbeddingContainer
from EmbeddingCreator.EmbeddingCreator import EmbeddingCreator
from Paths import PREPROCESSED_REQ_OUTPUT_DIR

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__)


class RequirementEmbeddingCreator(EmbeddingCreator):

    def __init__(self, requirements_word_chooser, preprocessor, wordemb_creator,
                  tokenizer, preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super().__init__(preprocessor, wordemb_creator, tokenizer, preprocessed_token_output_directory)
        self._requirements_word_chooser = requirements_word_chooser

    
class UCEmbeddingCreator(RequirementEmbeddingCreator):
    
    def _create_embeddings(self, file_representation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        chosen_word_groups_embeddings = []
        requirement_element_vectors = []
        for word_group in chosen_word_groups:
            word_embeddings = self._create_word_embeddings_from_word_list(word_group)
            chosen_word_groups_embeddings.append(word_embeddings)
            requirement_element_vectors.append(Util.create_averaged_vector(word_embeddings))
            
        file_vector = Util.create_averaged_vector([word for word_group in chosen_word_groups for word in word_group])  # flat average over all (nested) word embeddings in chosen_word_groups
        
        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, requirement_element_vectors)
    
    
class MockUCEmbeddingCreator(RequirementEmbeddingCreator):

    def _create_embeddings(self, file_representation):
        chosen_word_groups = self._requirements_word_chooser.choose_words_from(file_representation)
        file_vector = [word for word_group in chosen_word_groups for word in word_group]  # flat list over all (nested) words in chosen_word_groups
        
        return RequirementEmbeddingContainer(file_representation.file_path, file_vector, chosen_word_groups)
    
