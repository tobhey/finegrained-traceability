from Preprocessing.Preprocessor import Preprocessor, StopWordRemover, LowerCaseTransformer, NonLetterFilter,\
    Separator, UrlRemover, CamelCaseSplitter, WordLengthFilter, Lemmatizer
from Preprocessing.Tokenizer import WordTokenizer, SentenceTokenizer,\
    WordAndSentenceTokenizer, UCTokenizer
import logging
from Embedding import RequirementEmbedding
from WordEmbeddingCreator.WordEmbeddingCreator import MockWordEmbeddingCreator
from EmbeddingCreator import EmbeddingCreator
from Paths import *
import Util
from sklearn.feature_extraction.text import TfidfVectorizer
import pandas
from Dataset import Etour, Etour308
import FileUtil
import Paths
from builtins import isinstance
from TFIDFData import TFIDFData, TFIDFPrecalculator

logging.basicConfig(level=logging.INFO)
log = logging.getLogger(__name__ )

class RequirementEmbeddingCreator(EmbeddingCreator):
    def __init__(self, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(),
                  tokenizer=WordTokenizer(None), preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(RequirementEmbeddingCreator, self).__init__(preprocessor, wordemb_creator,
                                                           tokenizer, preprocessed_token_output_directory)
        
class AverageWordEmbeddingCreator(RequirementEmbeddingCreator):
    """Creates a requirement embedding by averaging its word vectors"""
    
    def _create_embeddings(self, file_representation):
        sub_vectors = self._create_word_embeddings_from_word_list(file_representation.token_list)
        return [RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(sub_vectors), sub_vectors)]
    
    
class AverageSentenceEmbeddingCreator(RequirementEmbeddingCreator):
    """Creates a requirement embedding by averaging its sentence vectors which are averages of its word vectors"""
    def __init__(self, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(),
                  tokenizer=WordAndSentenceTokenizer(None), preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(AverageSentenceEmbeddingCreator, self).__init__(preprocessor, wordemb_creator,
                                                           tokenizer, preprocessed_token_output_directory)
        
    def _create_embeddings(self, file_representation):
        sub_vectors = [Util.create_averaged_vector(self._create_word_embeddings_from_word_list(sen)) for sen in file_representation.grouped_token_list]
        return [RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(sub_vectors), sub_vectors)]


class AverageBERTSentenceEmbeddingCreator(RequirementEmbeddingCreator):
    """Creates a requirement embedding by averaging its sentence vectors
     and sentence vectors are created by a sentence embedder like BERT"""
    def __init__(self, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(),
                  tokenizer=SentenceTokenizer(None), preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(AverageBERTSentenceEmbeddingCreator, self).__init__(preprocessor, wordemb_creator,
                                                           tokenizer, preprocessed_token_output_directory)
        
    def _create_embeddings(self, file_representation):
        sub_vectors = self._create_word_embeddings_from_word_list(file_representation.token_list)
        return [RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(sub_vectors), sub_vectors)]
    
class UCAverageWordEmbeddingCreator(RequirementEmbeddingCreator):
    
    def __init__(self, tokenizer, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(),
                  preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(UCAverageWordEmbeddingCreator, self).__init__(preprocessor, wordemb_creator,
                                                           tokenizer, preprocessed_token_output_directory)
        self._with_name = True
        self._with_description = False
        self._with_actor = False
        self._with_precondition = False
        self._with_postcondition = False
        self._with_flow_of_events = False
        self._with_quality_req = False
        
    def _embedd_and_average(self, word_list):
        word_embd = self._create_word_embeddings_from_word_list(word_list)
        return [Util.create_averaged_vector(word_embd)] if word_embd else []
    
    def _create_embeddings(self, file_representation):
        req_embeddings = []
        partial_req_embeddings = []
        if self._with_name:
            partial_req_embeddings += self._embedd_and_average(file_representation.name_words)
        if self._with_description:
            partial_req_embeddings += self._embedd_and_average(file_representation.description_words)
        if self._with_actor:
            partial_req_embeddings += self._embedd_and_average(file_representation.actor_words)
        if self._with_precondition:
            partial_req_embeddings += self._embedd_and_average(file_representation.precondition_words)
        if self._with_postcondition:
            partial_req_embeddings += self._embedd_and_average(file_representation.postcondition_words)
        if self._with_flow_of_events:
            for sentence_group in file_representation.flow_of_events_words:
                partial_req_embeddings += self._embedd_and_average(sentence_group)
        if self._with_quality_req:
            partial_req_embeddings += self._embedd_and_average(file_representation.quality_requirement_words)
            
        if partial_req_embeddings:
            req_embeddings = [RequirementEmbedding(file_representation.file_path, Util.create_averaged_vector(partial_req_embeddings), partial_req_embeddings)]
        return req_embeddings 
    
    
class UCAverageWordEmbeddingCreatorWithNameDesc(UCAverageWordEmbeddingCreator):
    
    def __init__(self, tokenizer, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(), 
         preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR):
        super(UCAverageWordEmbeddingCreatorWithNameDesc, self).__init__(tokenizer, preprocessor=preprocessor, wordemb_creator=wordemb_creator, 
                                                           preprocessed_token_output_directory=preprocessed_token_output_directory)
        self._with_description = True
        
class UCAverageWordEmbeddingCreatorWithNameDescFlow(UCAverageWordEmbeddingCreator):
    
    def __init__(self, tokenizer, preprocessor=Preprocessor(), wordemb_creator=MockWordEmbeddingCreator(), 
         preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR):
        super(UCAverageWordEmbeddingCreatorWithNameDescFlow, self).__init__(tokenizer, preprocessor=preprocessor, wordemb_creator=wordemb_creator, 
                                                           preprocessed_token_output_directory=preprocessed_token_output_directory)
        self._with_description = True
        self._with_flow_of_events = True
        
class UCTFIDFWordEmbeddingCreator(UCAverageWordEmbeddingCreator):
    
    def __init__(self, tokenizer, precalculated_weights_file, preprocessor=Preprocessor(), 
                 wordemb_creator=MockWordEmbeddingCreator(),
                  preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR): 
        super(UCTFIDFWordEmbeddingCreator, self).__init__(tokenizer, preprocessor, wordemb_creator,
                                                           preprocessed_token_output_directory)
        if not precalculated_weights_file:
            log.info("No precalculated weights file read")
        else:
            self._tf_idf_data = TFIDFData(precalculated_weights_file)

    
    def _create_embeddings(self, file_representation):
        req_embeddings = []
        words_to_embedd = []
        if self._with_name:
            words_to_embedd += file_representation.name_words
        if self._with_description:
            words_to_embedd += file_representation.description_words
        if self._with_actor:
            words_to_embedd += file_representation.actor_words
        if self._with_precondition:
            words_to_embedd += file_representation.precondition_words
        if self._with_postcondition:
            words_to_embedd += file_representation.postcondition_words
        if self._with_flow_of_events:
            words_to_embedd += file_representation.flow_of_events_words
        if self._with_quality_req:
            words_to_embedd += file_representation.quality_requirement_words
            
        if words_to_embedd:
            document_word_dict = {} # document_word_dict[word] = tf-idf-value
            for word in words_to_embedd:
                document_word_dict[word] = self._tf_idf_data.get_weight(FileUtil.get_filename_from_path(file_representation.file_path), word)
            
            document_embedding = 0
            for word in document_word_dict:
                document_embedding += document_word_dict[word] * self._create_word_embedding_2(word, False)
                
            document_embedding /= sum(document_word_dict.values()) # normalize
            req_embeddings = [RequirementEmbedding(file_representation.file_path, document_embedding, None)]
        return req_embeddings 
    
    def precalculate_weights(self, dataset, output_filename=None):
        assert isinstance(self._tokenizer, UCTokenizer), self.__class__.__name__ + " needs a UCTokenizer as Tokenizer"
        self._tokenizer._dataset = dataset
        all_files = FileUtil.get_files_in_directory(dataset.req_folder())
        file_names = []
        file_contents = []
        for file in all_files:
            content = []
            file_representation = self._tokenize_and_preprocess(file)
            if self._with_name:
                content += file_representation.name_words
            if self._with_description:
                content += file_representation.description_words
            if self._with_actor:
                content += file_representation.actor_words
            if self._with_precondition:
                content += file_representation.precondition_words
            if self._with_postcondition:
                content.appendfile_representation.postcondition_words
            if self._with_flow_of_events:
                content += file_representation.flow_of_events_words
            if self._with_quality_req:
                content += file_representation.quality_requirement_words
            content = " ".join(content)
            if content and not content.isspace():
                file_contents.append(content)
                file_names.append(FileUtil.get_filename_from_path(file_representation.file_path))
        
        if not output_filename:
            output_filename = Paths.precalculated_tfidf_weights_filename(dataset, self.__class__.__name__)
        TFIDFPrecalculator().precalculate_and_write(file_contents, file_names, output_filename)
        
    @classmethod
    def default_precalculated_weights_file(cls, dataset):
        return Paths.precalculated_tfidf_weights_filename(dataset, cls.__name__)
        
        
class UCTFIDFWordEmbeddingCreatorWithNameDesc(UCTFIDFWordEmbeddingCreator):
    
    def __init__(self, tokenizer, precalculated_weights_file, preprocessor=Preprocessor(), 
                 wordemb_creator=MockWordEmbeddingCreator(),
                  preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR):
        super(UCTFIDFWordEmbeddingCreatorWithNameDesc, self).__init__(tokenizer, precalculated_weights_file, preprocessor=preprocessor, 
                                         wordemb_creator=wordemb_creator, preprocessed_token_output_directory=preprocessed_token_output_directory)
        self._with_description = True
      
class UCTFIDFWordEmbeddingCreatorWithNameDescFlow(UCTFIDFWordEmbeddingCreator):
    
    def __init__(self, tokenizer, precalculated_weights_file, preprocessor=Preprocessor(), 
                 wordemb_creator=MockWordEmbeddingCreator(),
                  preprocessed_token_output_directory=PREPROCESSED_REQ_OUTPUT_DIR):
        super(UCTFIDFWordEmbeddingCreatorWithNameDescFlow, self).__init__(tokenizer, precalculated_weights_file, preprocessor=preprocessor, 
                                         wordemb_creator=wordemb_creator, preprocessed_token_output_directory=preprocessed_token_output_directory)
        self._with_description = True
        self._with_flow_of_events = True
  
"""        
CAMEL = CamelCaseSplitter()
LOWER = LowerCaseTransformer()
LETTER = NonLetterFilter()
URL = UrlRemover()
SEP = Separator()
STOP = StopWordRemover()
LEMMA = Lemmatizer()
W_LENGTH = WordLengthFilter(2)
REQ_PREPROCESSOR = Preprocessor([URL, SEP, LETTER, CAMEL, LOWER, LEMMA, STOP, W_LENGTH])
"""
#UCTFIDFWordEmbeddingCreator(UCTokenizer(), REQ_PREPROCESSOR).precalculate_weights(Etour308())
#UCTFIDFWordEmbeddingCreatorWithNameDesc(UCTokenizer(), None, REQ_PREPROCESSOR).precalculate_weights(Etour308())
#UCTFIDFWordEmbeddingCreatorWithNameDescFlow(UCTokenizer(), None, REQ_PREPROCESSOR).precalculate_weights(Etour308())