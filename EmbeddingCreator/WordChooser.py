import abc
from Preprocessing.CodeFileRepresentation import Classifier, Method

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