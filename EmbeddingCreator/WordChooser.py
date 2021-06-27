import abc
from Preprocessing.CodeFileRepresentation import Classifier, Method
from Preprocessing.FileRepresentation import UseCaseFileRepresentation, \
    TextFileGroupedRepresentation


class ClassnameWordChooser:

    def choose_words_from(self, classifier: Classifier):
        return classifier.get_name_words()

    
class MethodWordChooser(abc.ABC):
    """
    Determines which words of a method are used for further processing (embedding calculation)
    """

    @abc.abstractmethod
    def choose_words_from(self, classifier: Classifier, method: Method) -> [str]:
        pass
    
    
class MethodSignatureChooser(MethodWordChooser):
    """
    Chooses classifier name as prefix and the method signature words: name, return type, param names, param types
    """

    def choose_words_from(self, classifier: Classifier, method: Method):
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + method.get_param_plain_list()
    
    
class MethodCommentSignatureChooser(MethodWordChooser):
    """
    Chooses classifier name as prefix, the method signature and comment words: name, return type, param names, param types, method (javadoc) comment
    """

    def choose_words_from(self, classifier: Classifier, method: Method):
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + method.get_param_plain_list() + method.get_comment_tokens()
    
    
class RequirementWordChooser(abc.ABC):
    
    @abc.abstractmethod
    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> [[str]]:
        pass

    
class UCNameWordChooser(RequirementWordChooser):
    """
    Chooses the UC name words
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> [[str]]:
        return [use_case_file_representation.name_words]


class UCNameDescWordChooser(RequirementWordChooser):
    """
    Chooses the UC name and description words, each of them grouped in the return list
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> [[str]]:
        return [use_case_file_representation.name_words, use_case_file_representation.description_words]


class UCNameDescFlowWordChooser(RequirementWordChooser):
    """
    Chooses the UC name, description and the flow of events words.
    Sentences in flow of events are in separate groups.
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> [[str]]:
        return [use_case_file_representation.name_words, use_case_file_representation.description_words] + [sentence for sentence in use_case_file_representation.flow_of_events_words]


class SentenceChooser(RequirementWordChooser):
    
    def choose_words_from(self, text_file_grouped_representation: TextFileGroupedRepresentation):
        return text_file_grouped_representation.grouped_token_list
