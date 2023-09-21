import numpy as np
import torch
from embeddingCreator.unixcoder import UniXcoder
from embeddingCreator.WordEmbeddingCreator import WordEmbeddingCreator

class UniXcoderEmbeddingCreator(WordEmbeddingCreator):
    """
    Creates context sensitive embedding vectors based on the UniXcoder lm
    """

    def __init__(self, model_path):
        self._device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self._model = UniXcoder(model_path)
        self._model.eval()

    def create_word_embedding(self, word):
        pass

    def word_movers_distance(self, str_list_1: [str], str_list_2: [str]):
        pass

    def create_word_list_embedding(self, str_list: [str]):
        tensor_token_ids = self._prepare_input_to_model(str_list)
        return self._model(tensor_token_ids)[1].detach().numpy()

    def create_word_list_embedding_boe(self, str_list: [str]):
        tensor_token_ids = self._prepare_input_to_model(str_list)
        boe = np.squeeze(self._model(tensor_token_ids)[0])
        return [vector.detach().numpy().reshape(1, -1) for vector in boe]

    def _prepare_input_to_model(self, str_list: [str]):
        joined_str_list = " ".join(str_list)
        tokenized_str_list = self._model.tokenize([joined_str_list], max_length=512,mode="<encoder-only>")[0]
        token_ids= [int(tokenized_str) for tokenized_str in tokenized_str_list]
        return torch.tensor([token_ids])



