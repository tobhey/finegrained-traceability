import logging, Util
from Embedding import RequirementEmbedding
from EmbeddingCreator import EmbeddingCreator
from Paths import PREPROCESSED_REQ_OUTPUT_DIR

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__ )

class RequirementEmbeddingCreator(EmbeddingCreator):
    def __init__(self, preprocessor, wordemb_creator,
                  tokenizer, preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(RequirementEmbeddingCreator, self).__init__(preprocessor, wordemb_creator,
                                                           tokenizer, preprocessed_token_output_directory)
        
class AverageWordEmbeddingCreator(RequirementEmbeddingCreator):
    """Creates a requirement embedding by averaging its word vectors"""
    
    def _create_embeddings(self, file_representation):
        word_vectors = self._create_word_embeddings_from_word_list(file_representation.token_list)
        if word_vectors:
            return RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(word_vectors), word_vectors)
        return None
    
    
class AverageSentenceEmbeddingCreator(RequirementEmbeddingCreator):
    """Creates a requirement embedding by averaging its sentence vectors which are averages of its word vectors"""
        
    def _create_embeddings(self, file_representation):
        sentence_vectors = [Util.create_averaged_vector(self._create_word_embeddings_from_word_list(sen)) for sen in file_representation.grouped_token_list]
        if sentence_vectors:
            return RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(sentence_vectors), sentence_vectors)
        return None
    
class UCAverageWordEmbeddingCreator(RequirementEmbeddingCreator):
    
    def _create_embeddings(self, file_representation):
        partial_req_embeddings = []
        partial_req_embeddings += self._embedd_and_average(file_representation.name_words)
        partial_req_embeddings += self._embedd_and_average(file_representation.description_words)
        for sentence_group in file_representation.flow_of_events_words:
            partial_req_embeddings += self._embedd_and_average(sentence_group)
            
        if partial_req_embeddings:
            return RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(partial_req_embeddings), partial_req_embeddings)
        return None
