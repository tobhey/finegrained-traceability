import abc, logging
import re

from nltk.tokenize import word_tokenize, sent_tokenize

from preprocessing.FileRepresentation import FileRepresentation, TextFileRepresentation, TextFileGroupedRepresentation, \
    UseCaseFileRepresentation
from utility import FileUtil

log = logging.getLogger(__name__)


class Tokenizer(abc.ABC):
    
    def __init__(self, dataset):
        self._dataset = dataset

    @abc.abstractmethod
    def tokenize(self, file_path) -> FileRepresentation:
        pass

    
class NaturalSpeechTokenizer(Tokenizer):

    def __init__(self, dataset, italian=False):
        super(NaturalSpeechTokenizer, self).__init__(dataset)
        self._italian = italian
        
    def tokenize_to_string_list(self, text: str):
        if self._italian:
            tokenized_text = " ".join(text.split("'"))  # The italian tokenizer does not split apostrophes
            return word_tokenize(tokenized_text, "italian")
        return word_tokenize(text)

    
class WordTokenizer(NaturalSpeechTokenizer):
    """
    Tokenizer result: TextFileRepresentation
        > A flat list, each element is a string representing a single word
    """
        
    def tokenize(self, file_path):
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        if self._italian:
            tokenized_text = " ".join(text_as_string.split("'"))
            return TextFileRepresentation(word_tokenize(tokenized_text), file_path)
        else:
            return TextFileRepresentation(word_tokenize(text_as_string), file_path)

    
class SentenceTokenizer(NaturalSpeechTokenizer):
    """
    Tokenizer result: TextFileRepresentation
        > A flat list, each element is a string representing a single sentence
    """
        
    def tokenize(self, file_path) -> FileRepresentation:
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        if self._italian:
            return TextFileRepresentation(sent_tokenize(text_as_string, language="italian"), file_path)
        return TextFileRepresentation(sent_tokenize(text_as_string), file_path)
    
    def tokenize_to_string_list(self, text: str):
        if self._italian:
            return sent_tokenize(text, language="italian")
        return sent_tokenize(text)
    
    def tokenize_all_sentences_in_directory(self, directory) -> [str]:
        sentences = []
        for file in FileUtil.get_files_in_directory(directory):
            if self._italian:
                sentences += sent_tokenize(FileUtil.read_textfile_into_string(file, self._dataset.encoding()), language="italian")
            else: 
                sentences += sent_tokenize(FileUtil.read_textfile_into_string(file, self._dataset.encoding()))
        return sentences


class WordAndSentenceTokenizer(NaturalSpeechTokenizer):
    """
    Tokenizer result: TextFileGroupedRepresentation
        > A nested list, each element is another list which contains the words of a single sentence.
    """
        
    def tokenize(self, file_path) -> FileRepresentation:
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        word_tokenized_sentences = None
        if self._italian:
            tokens = [word_tokenize(" ".join(sent.split("'")), language="italian") for sent in sent_tokenize(text_as_string, language="italian")]
            return TextFileGroupedRepresentation(tokens, file_path) 
        else:
            word_tokenized_sentences = [word_tokenize(sent) for sent in sent_tokenize(text_as_string)]
        return TextFileGroupedRepresentation(word_tokenized_sentences, file_path)
    
    def tokenize_to_string_list(self, text: str):
        if self._italian:
            return [word_tokenize(" ".join(sent.split("'")), language="italian") for sent in sent_tokenize(text, language="italian")]
        return [word_tokenize(sent) for sent in sent_tokenize(text)]


class UCTokenizer(NaturalSpeechTokenizer):
    
    def __init__(self, dataset=None, italian=False):
        super(UCTokenizer, self).__init__(dataset, italian)
    
    def tokenize(self, file_path):
        text_lines = FileUtil.read_textfile_into_lines_list(file_path, self._dataset.encoding())
        
        uc_name_words = []
        uc_actor_words = []
        uc_precond_words = []
        uc_postcond_words = []
        uc_description_words = []
        uc_quality_req_words = []
        uc_flow_of_events_words = []
        last_word_category = uc_description_words  # Default

        for line in text_lines:
            line = line.lstrip()  # Remove leading white spaces/tabs
            if self._dataset.UC_NAME_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_NAME_TEMPLATE_REGEX.match(line).group(0)
                uc_name_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_name_words
            elif self._dataset.UC_DESCRIPTION_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_DESCRIPTION_TEMPLATE_REGEX.match(line).group(0)
                uc_description_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_description_words
            elif self._dataset.UC_ACTOR_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_ACTOR_TEMPLATE_REGEX.match(line).group(0)
                uc_actor_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_actor_words
            elif self._dataset.UC_PRECONDITION_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_PRECONDITION_TEMPLATE_REGEX.match(line).group(0)
                uc_precond_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_precond_words
            elif self._dataset.UC_POSTCONDITION_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_POSTCONDITION_TEMPLATE_REGEX.match(line).group(0)
                uc_postcond_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_postcond_words
            elif self._dataset.UC_FLOW_OF_EVENTS_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_FLOW_OF_EVENTS_TEMPLATE_REGEX.match(line).group(0)
                uc_flow_of_events_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_flow_of_events_words
            elif self._dataset.UC_QUALI_REQ_TEMPLATE_REGEX.match(line):
                matched_string = self._dataset.UC_QUALI_REQ_TEMPLATE_REGEX.match(line).group(0)
                uc_quality_req_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_quality_req_words
            elif self._dataset.UC_USER_TEMPLATE_REGEX.match(line):
                # part of flow of events
                matched_string = self._dataset.UC_USER_TEMPLATE_REGEX.match(line).group(0)
                uc_flow_of_events_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_flow_of_events_words
            elif self._dataset.UC_SYSTEM_TEMPLATE_REGEX.match(line):
                # part of flow of events
                matched_string = self._dataset.UC_SYSTEM_TEMPLATE_REGEX.match(line).group(0)
                uc_flow_of_events_words += self.tokenize_to_string_list(line[len(matched_string):])
                last_word_category = uc_flow_of_events_words    
            else:
                last_word_category += self.tokenize_to_string_list(line)
                
        complete_uc_flow_of_events_words_string = " ".join(uc_flow_of_events_words)
        if self._italian:
            uc_flow_of_events_words = [word_tokenize(" ".join(sent.split("'")), language="italian") for sent in sent_tokenize(complete_uc_flow_of_events_words_string, language="italian")]
        else:
            uc_flow_of_events_words = [word_tokenize(sent) for sent in sent_tokenize(complete_uc_flow_of_events_words_string)]
        
        return UseCaseFileRepresentation(file_path, uc_name_words, uc_description_words, uc_actor_words, uc_precond_words, uc_postcond_words,
                                          uc_flow_of_events_words, uc_quality_req_words)

        
class JavaDocDescriptionOnlyTokenizer(NaturalSpeechTokenizer):
    JAVADOC_TAGS = r"(@param|@throws|@author|@version|@return)"
    
    def tokenize(self, file_path) -> FileRepresentation:
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        grp = re.search(self.JAVADOC_TAGS, text_as_string, re.RegexFlag.IGNORECASE)
        if grp:
            matched_tag = grp[0]
            substring_index = text_as_string.find(matched_tag)
            text_as_string = text_as_string[:substring_index]
            text_as_string = super(JavaDocDescriptionOnlyTokenizer, self).tokenize_to_string_list(text_as_string)
        return TextFileRepresentation(text_as_string, file_path)
                
    def tokenize_to_string_list(self, text: str):
        grp = re.search(self.JAVADOC_TAGS, text, re.RegexFlag.IGNORECASE)
        if grp:
            matched_tag = grp[0]
            substring_index = text.find(matched_tag)
            text = text[:substring_index]
            text = super(JavaDocDescriptionOnlyTokenizer, self).tokenize_to_string_list(text)
        return text
