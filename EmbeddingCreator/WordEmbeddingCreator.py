from abc import ABC, abstractmethod
import logging
import random

import gensim.models.wrappers

import Util

COLAB_MODEL_PATH = "/content/drive/My Drive/models/cc.en.300.bin"

log = logging.getLogger(__name__)


class WordEmbeddingCreator(ABC) :
    
    @abstractmethod
    def create_word_embedding(self, word):
        """
        Creates an embedding vector for the given word
        """
        pass
    
    @abstractmethod
    def word_movers_distance(self, str_list_1: [str], str_list_2: [str]):
        """
        Calculates the word mover's distance between two bag of words
        """
        pass

    
class FastTextEmbeddingCreator(WordEmbeddingCreator):
    """
    Use this for monolingual fasttext models that uses .bin models
    """

    def __init__(self, model_path=COLAB_MODEL_PATH):
        self._model = gensim.models.wrappers.FastText.load_fasttext_format(model_path)
        self._model.init_sims(replace=True)  # normalizes vectors to length 1

    def create_word_embedding(self, word):
        try:
            return self._model[word]
        except KeyError as k:
            log.info(k)
            return None
    
    def word_movers_distance(self, str_list_1, str_list_2):
        return self._model.wmdistance(str_list_1, str_list_2)

     
class RandomWordEmbeddingCreator(WordEmbeddingCreator):
    """
    Creates random embedding vectors
    For testing purposes since initializing fastText model is expensive
    """

    def __init__(self, seed=None):
        if seed:
            random.seed(seed)
    
    def create_word_embedding(self, word):
        return Util.random_numpy_array(-1, 1, 300)
    
    def word_movers_distance(self, str_list_1, str_list_2):
        assert isinstance(str_list_1, list)
        assert isinstance(str_list_2, list)
        return random.uniform(0, 2)
    
