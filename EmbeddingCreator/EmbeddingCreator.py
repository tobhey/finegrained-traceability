import abc, logging, os
from pathlib import Path
from Embedding import Embedding, MockEmbedding
from Preprocessing.Preprocessor import Preprocessor
from Preprocessing.FileRepresentation import FileRepresentation
import FileUtil
from distutils.tests.test_file_util import FileUtilTestCase

log = logging.getLogger(__name__)

PREPROCESSED_TOKEN_FILENAME_PREFIX = "PreprocessedTokens_"

class EmbeddingCreator(abc.ABC):
    """
    Superclass of all embedding creators.
    Initialize with a preprocessor, a tokenizer and a word embedding creator and 
    call the create_all_embeddings method too create embedding objects for all files in the given directory
    """
    def __init__(self, preprocessor: Preprocessor, word_embedding_creator, 
                 tokenizer, preprocessed_token_output_directory: Path):
        self._preprocessed_token_output_directory = preprocessed_token_output_directory
        self._preprocessor = preprocessor
        self._word_embedding_creator = word_embedding_creator
        self._tokenizer = tokenizer
    
    def create_all_embeddings(self, input_directory, output_emb_filepath=None) -> [Embedding]:
        """
        Creates embeddings for all files in the input directory.
        Writes all embeddings in a file at output_emb_filepath if not None.
        Returns the embeddings as list
        """
        log.info("Read directory: " + str(input_directory))
        embedding_list = self.read_directory(input_directory)
        if output_emb_filepath is not None:
            FileUtil.write_file(output_emb_filepath, "\n".join(map(str, embedding_list)))
        return embedding_list
        
    def read_directory(self, directory):
        all_filenames = FileUtil.get_files_in_directory(directory)
        all_embeddings = []
        for filename in all_filenames:
            file_representation = self._tokenize_and_preprocess(filename)
            all_embeddings += self._create_embeddings(file_representation)
        return all_embeddings
    
    def _tokenize_and_preprocess(self, file_path):
        log.debug(f"Tokenizing {file_path}")
        file_representation = self._tokenizer.tokenize(file_path)
        log.debug(f"Preprocessing {file_path}")
        file_representation.preprocess(self._preprocessor)
        if self._preprocessed_token_output_directory:
            FileUtil.write_file(self._preprocessed_token_output_directory / (PREPROCESSED_TOKEN_FILENAME_PREFIX 
                + FileUtil.get_filename_from_path(file_path)), file_representation.get_printable_string())
        return file_representation
        
    def _create_word_embedding(self, word: str):
        return self._word_embedding_creator.create_word_embedding(word)
    
    def _create_word_embedding_2(self, word: str):
        emb = self._word_embedding_creator.create_word_embedding(word)
        return emb if emb is not None else 0
    
    def _create_word_embeddings_from_word_list(self, word_list: [str]):
        result = []
        for word in word_list:
            word_emb = self._create_word_embedding(word)
            if word_emb is None:
                log.info(f"No word embedding for '{word}', skip.")
            else:
                result.append(word_emb)
        return result
      
            
    @abc.abstractclassmethod
    def _create_embeddings(self, file_representation: FileRepresentation) -> [Embedding]:
        """
        Takes a file and creates one or multiple embeddings
        """
        pass
        

class MockEmbeddingCreator(EmbeddingCreator):
    """Sets the file representation as vector attribute of the returned Embedding objects"""
    def __init__(self, preprocessor: Preprocessor, word_embedding_creator, 
                 tokenizer, preprocessed_token_output_directory: Path=None):
        super(MockEmbeddingCreator, self).__init__(preprocessor, word_embedding_creator, tokenizer, preprocessed_token_output_directory)
        
    def _create_embeddings(self, file_representation):
        return [MockEmbedding(file_representation.file_path, file_representation)]
    