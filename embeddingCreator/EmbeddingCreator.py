import abc, logging, traceback

from javalang.parser import JavaSyntaxError, JavaParserError
from javalang.tokenizer import LexerError
from pathlib import Path

from embeddingCreator.EmbeddingContainer import EmbeddingContainer
from preprocessing.CodeASTTokenizer import FileExtensionNotSupportedError
from preprocessing.FileRepresentation import FileRepresentation
from preprocessing.Preprocessor import Preprocessor
from utility import FileUtil
from utility import Util

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
    
    def create_all_embeddings(self, input_directory, output_emb_filepath=None) -> [EmbeddingContainer]:
        """
        Creates embeddings for all files in the input directory.
        Writes all embeddings in a file at output_emb_filepath if not None.
        Returns the embeddings as list
        """
        log.info("Read directory: " + str(input_directory))
        embedding_list = self.embedd_all_files_in_directory(input_directory)
        if output_emb_filepath is not None:
            FileUtil.write_file(output_emb_filepath, "\n".join(map(str, embedding_list)))
        return embedding_list
        
    def embedd_all_files_in_directory(self, directory):
        all_filenames = FileUtil.get_files_in_directory(directory)
        all_embeddings = []
        for filename in all_filenames:
            try:
                file_representation = self._tokenize_and_preprocess(filename)
            except (FileNotFoundError, IsADirectoryError, PermissionError, UnicodeDecodeError,) as e:
                log.info(f"SKIPPED: Error on reading or tokenizing {filename}: {e}")
                continue
            except JavaSyntaxError as j:
                log.info(f"SKIPPED: JavaSyntaxError on tokenizing {filename} (Note: code files needs to be compilable): {j.at}")
                continue
            except (JavaParserError, LexerError) as j:
                log.info(f"SKIPPED: Error on tokenizing {filename} (Note: code files needs to be compilable): {j}")
                continue
            except (FileExtensionNotSupportedError):
                continue
            file_embedding = self._create_embeddings(file_representation)
            if file_embedding:
                all_embeddings.append(file_embedding)
            else:
                log.info(f"No embedding for {filename}")
        return all_embeddings
    
    def _tokenize_and_preprocess(self, file_path):
        log.debug(f"Tokenizing {file_path}")
        file_representation = self._tokenizer.tokenize(file_path)
        log.debug(f"preprocessing {file_path}")
        file_representation.preprocess(self._preprocessor)
        if self._preprocessed_token_output_directory:
            FileUtil.write_file(self._preprocessed_token_output_directory / (PREPROCESSED_TOKEN_FILENAME_PREFIX 
                +FileUtil.get_filename_from_path(file_path)), file_representation.get_printable_string())
        return file_representation
        
    def _create_word_embedding(self, word: str):
        return self._word_embedding_creator.create_word_embedding(word)
    
    def _create_word_embeddings_from_word_list(self, word_list: [str]):
        result = []
        for word in word_list:
            word_emb = self._create_word_embedding(word)
            if word_emb is None:
                log.info(f"No word embedding for '{word}', skip.")
            else:
                result.append(word_emb)
        return result
      
    def _embedd_and_average(self, word_list):
        word_embd = self._create_word_embeddings_from_word_list(word_list)
        return [Util.create_averaged_vector(word_embd)] if word_embd else []  # Return as (empty) lists to avoid is-None-check later on
            
    @abc.abstractmethod
    def _create_embeddings(self, file_representation: FileRepresentation) -> EmbeddingContainer:
        """
        Takes a file and creates an embedding container from it
        """
        pass
        
