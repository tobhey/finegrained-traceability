import abc, Util, logging, FileUtil

from EmbeddingContainer import ClassEmbeddingContainer
from EmbeddingCreator.EmbeddingCreator import EmbeddingCreator
from Preprocessing.CallGraphUtil import build_method_param_dict_key, \
    build_class_method_param_dict_key2

log = logging.getLogger(__name__)


class CodeEmbeddingCreator(EmbeddingCreator):

    def __init__(self, method_word_chooser, classname_word_chooser, preprocessor, word_embedding_creator, tokenizer, preprocessed_token_output_directory, classname_as_optional_voter=True):
        super(CodeEmbeddingCreator, self).__init__(preprocessor, word_embedding_creator, tokenizer, preprocessed_token_output_directory)
        
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
        class_embedding = ClassEmbeddingContainer(file_representation.file_path, class_vector, classifier.get_original_name())
        class_embedding = self._add_method_vectors(classifier, class_embedding)
        
        if not class_embedding.has_method_entries():
            # Class has no method vectors (e.g. empty class or all methods are not public)
            class_embedding = self._handle_no_method_vectors_case(classifier, class_embedding)
            
        return [class_embedding]

    def _calculate_class_vector(self, classifier):
        words_to_embedd = classifier.get_name_words()
        [words_to_embedd.extend(self._method_word_chooser(classifier, method)) for method in classifier.methods]
        return self._embedd_and_average(words_to_embedd)
    
    def _add_method_vectors(self, classifier, class_embedding):
        for method in classifier.methods:
            method_vector = self._embedd_and_average(self._method_word_chooser(classifier, method))
            if method_vector is None:  # Can happen if e.g. all method words are removed via preprocessing
                continue 
            method_key = build_method_param_dict_key(method.get_original_name(), method.get_original_param_type_list())
            method_key_with_classifier = build_class_method_param_dict_key2(classifier.get_original_name(), method_key)
            class_embedding.set_method_vector(method_key_with_classifier, method_vector)
        return class_embedding

    def _handle_no_method_vectors_case(self, classifier, class_embedding):
        # Use class name as replacement element vector
        # The class name can be empty due to preprocessing -> no embedding in this case
        class_name_vector = self._embedd_and_average(self._classname_word_chooser.choose_words_from(classifier))
        if class_name_vector:
            class_embedding.set_non_cg_vector(ClassEmbeddingContainer.CLASS_NAME_VOTER, class_name_vector)
        return class_embedding
                
    
class MethodSignatureCallGraphEmbeddingCreator(CodeEmbeddingCreator):
    
    def _calculate_method_vector(self, classifier, method):
        method_words = classifier.get_name_words()
        method_words += method.get_name_words()
        method_words += method.get_returntype_words()
        method_words += method.get_param_plain_list()
        return Util.create_averaged_vector(self._create_word_embeddings_from_word_list(method_words, False))

    
class MethodCommentSignatureCallGraphEmbeddingCreator(CodeEmbeddingCreator):
    
    def _calculate_method_vector(self, classifier, method):
        method_words = classifier.get_name_words()
        method_words += method.get_name_words()
        method_words += method.get_returntype_words()
        method_words += method.get_param_plain_list()
        method_words += method.get_comment_tokens()
        method_subvectors = self._create_word_embeddings_from_word_list(method_words, False)
        return Util.create_averaged_vector(method_subvectors)
