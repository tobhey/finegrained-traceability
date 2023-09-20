import logging

from pathlib import Path

from embeddingCreator.EmbeddingContainer import ClassEmbeddingContainer
from embeddingCreator.EmbeddingCreator import EmbeddingCreator
from preprocessing.CallGraphUtil import build_class_method_param_dict_key
from utility import FileUtil

log = logging.getLogger(__name__)

PREPROCESSED_CODE_OUTPUT_DIR = Path(__file__).parent.parent / "output/Preprocessed_Code"


class CodeEmbeddingCreator(EmbeddingCreator):

    def __init__(self, method_word_chooser, classname_word_chooser, preprocessor, word_embedding_creator, tokenizer, preprocessed_token_output_directory=PREPROCESSED_CODE_OUTPUT_DIR, classname_as_optional_voter=True):
        super().__init__(preprocessor, word_embedding_creator, tokenizer, preprocessed_token_output_directory)
        
        self._method_word_chooser = method_word_chooser
        self._classname_word_chooser = classname_word_chooser
        self._classname_as_optional_voter = classname_as_optional_voter
    
    def _create_embeddings(self, file_representation):
        if len(file_representation.classifiers) == 0:
            log.info(f"{FileUtil.get_filename_from_path(file_representation.file_path)} has no classifier, skipping.")
            return None
        classifier = file_representation.classifiers[0]
        
        if len(file_representation.classifiers) > 1:
            log.info(f"{FileUtil.get_filename_from_path(file_representation.file_path)} has more than one top level classifier. Only processing the first: {classifier.get_original_name()}")
        
        class_vector = self._calculate_class_vector(classifier)
        class_embedding_container = ClassEmbeddingContainer(file_representation.file_path, class_vector, classifier.get_original_name())
        class_embedding_container = self._add_method_vectors(classifier, class_embedding_container)
        
        if self._classname_as_optional_voter and not class_embedding_container.has_method_entries() :
            # Class has no method vectors (e.g. empty class or all methods are not public)
            class_embedding_container = self._handle_no_method_vectors_case(classifier, class_embedding_container)
        elif not self._classname_as_optional_voter:
            class_embedding_container = self._handle_no_method_vectors_case(classifier, class_embedding_container)
        return class_embedding_container

    def _calculate_class_vector(self, classifier):
        words_to_embedd = [] + classifier.get_name_words()
        [words_to_embedd.extend(self._method_word_chooser.choose_words_from(classifier, method)) for method in classifier.methods]
        return self._embedd_and_average(words_to_embedd)
    
    def _add_method_vectors(self, classifier, class_embedding_container):
        for method in classifier.methods:
            method_vector = self._embedd_and_average(self._method_word_chooser.choose_words_from(classifier, method))
            if method_vector is None:  # Can happen if e.g. all method words are removed via preprocessing
                continue 
            method_key_with_classifier = build_class_method_param_dict_key(classifier.get_original_name(), method.get_original_name(), method.get_original_param_type_list())
            class_embedding_container.set_method_vector(method_key_with_classifier, method_vector)
        return class_embedding_container

    def _handle_no_method_vectors_case(self, classifier, class_embedding_container):
        # Use class name as replacement element vector
        # The class name can be empty due to preprocessing -> no embedding in this case
        class_name_vector = self._embedd_and_average(self._classname_word_chooser.choose_words_from(classifier))
        if class_name_vector:
            class_embedding_container.set_non_cg_vector(self._build_class_name_voter_key(classifier), class_name_vector)
        return class_embedding_container
    
    def _build_class_name_voter_key(self, classifier):
        return classifier.get_original_name() + "." + ClassEmbeddingContainer.CLASS_NAME_VOTER


class CodeVectorEmbeddingCreator(CodeEmbeddingCreator):

    def _calculate_class_vector(self, classifier):
        words_to_embedd = [] + classifier.get_name_words()
        [words_to_embedd.extend(self._method_word_chooser.choose_words_from(classifier, method)) for method in classifier.methods]
        return self._word_embedding_creator.create_word_list_embedding(words_to_embedd)
    
    def _add_method_vectors(self, classifier, class_embedding_container):
        for method in classifier.methods:
            method_key_with_classifier = build_class_method_param_dict_key(classifier.get_original_name(), method.get_original_name(), method.get_original_param_type_list())
            method_vector = self._word_embedding_creator.create_word_list_embedding(self._method_word_chooser.choose_words_from(classifier, method))
            if method_vector is None:  # Can happen if e.g. all method words are removed via preprocessing
                continue 
            class_embedding_container.set_method_vector(method_key_with_classifier, method_vector)
        return class_embedding_container

    def _handle_no_method_vectors_case(self, classifier, class_embedding_container):
        # Use class name as replacement element vector
        # The class name can be empty due to preprocessing -> no embedding in this case
        class_name_vector = self._word_embedding_creator.create_word_list_embedding(self._classname_word_chooser.choose_words_from(classifier))
        if class_name_vector is not None:
            class_embedding_container.set_non_cg_vector(self._build_class_name_voter_key(classifier), class_name_vector)
        return class_embedding_container
    

class CodeBOEEmbeddingCreator(CodeEmbeddingCreator):

    def _calculate_class_vector(self, classifier):
        words_to_embedd = [] + classifier.get_name_words()
        [words_to_embedd.extend(self._method_word_chooser.choose_words_from(classifier, method)) for method in classifier.methods]
        return self._word_embedding_creator.create_word_list_embedding_boe(words_to_embedd)
    
    def _add_method_vectors(self, classifier, class_embedding_container):
        for method in classifier.methods:
            method_key_with_classifier = build_class_method_param_dict_key(classifier.get_original_name(), method.get_original_name(), method.get_original_param_type_list())
            method_vector = self._word_embedding_creator.create_word_list_embedding_boe(self._method_word_chooser.choose_words_from(classifier, method))
            if method_vector is None:  # Can happen if e.g. all method words are removed via preprocessing
                continue 
            class_embedding_container.set_method_vector(method_key_with_classifier, method_vector)
        return class_embedding_container

    def _handle_no_method_vectors_case(self, classifier, class_embedding_container):
        # Use class name as replacement element vector
        # The class name can be empty due to preprocessing -> no embedding in this case
        class_name_vector = self._word_embedding_creator.create_word_list_embedding_boe(self._classname_word_chooser.choose_words_from(classifier))
        if class_name_vector is not None:
            class_embedding_container.set_non_cg_vector(self._build_class_name_voter_key(classifier), class_name_vector)
        return class_embedding_container

                
class MockCodeEmbeddingCreator(CodeEmbeddingCreator):
    """
    Does not embedd, returns the chosen words as bag of words.
    """

    def _calculate_class_vector(self, classifier):
        chosen_words = [] + classifier.get_name_words()
        [chosen_words.extend(self._method_word_chooser.choose_words_from(classifier, method)) for method in classifier.methods]
        return chosen_words
    
    def _add_method_vectors(self, classifier, class_embedding):
        for method in classifier.methods:
            method_words = self._method_word_chooser.choose_words_from(classifier, method)
            if method_words is None:  # Can happen if e.g. all method words are removed via preprocessing
                continue 
            method_key_with_classifier = build_class_method_param_dict_key(classifier.get_original_name(), method.get_original_name(), method.get_original_param_type_list())
            class_embedding.set_method_vector(method_key_with_classifier, method_words)
        return class_embedding

    def _handle_no_method_vectors_case(self, classifier, class_embedding):
        # Use class name as replacement 
        # The class name can be empty due to preprocessing -> no embedding in this case
        class_name_words = self._classname_word_chooser.choose_words_from(classifier)
        if class_name_words:
            class_embedding.set_non_cg_vector(self._build_class_name_voter_key(classifier), class_name_words)
        return class_embedding
    
