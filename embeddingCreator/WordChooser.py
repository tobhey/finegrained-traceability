import abc
from preprocessing.CodeFileRepresentation import Classifier, Method
from preprocessing.FileRepresentation import UseCaseFileRepresentation, \
    TextFileGroupedRepresentation


class ClassnameWordChooser:

    def choose_words_from(self, classifier: Classifier):
        return classifier.get_name_words()


class ClassnameCommentWordChooser:

    def choose_words_from(self, classifier: Classifier):
        return classifier.get_name_words() + classifier.get_comment_tokens()


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

    def __init__(self, use_param_types=True):
        self.use_param_types = use_param_types

    def choose_words_from(self, classifier: Classifier, method: Method):
        if self.use_param_types:
            param_list = method.get_param_plain_list()
        else:
            param_list = method.get_param_names_plain_list()
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + param_list



class MethodBodySignatureChooser(MethodWordChooser):
    """
    Chooses classifier name as prefix, the method signature and body words: name, return type, param names, param types, method body
    """
    def __init__(self, use_param_types=True):
        self.use_param_types = use_param_types

    def choose_words_from(self, classifier: Classifier, method: Method):
        if self.use_param_types:
            param_list = method.get_param_plain_list()
        else:
            param_list = method.get_param_names_plain_list()
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + param_list  + method.get_body_words()


class MethodBodyCommentSignatureChooser(MethodWordChooser):
    """
    Chooses classifier name as prefix, the method signature, comment and body words: name, return type, param names, param types, method (javadoc) comment, method body
    """
    def __init__(self, use_param_types=True):
        self.use_param_types = use_param_types

    def choose_words_from(self, classifier: Classifier, method: Method):
        if self.use_param_types:
            param_list = method.get_param_plain_list()
        else:
            param_list = method.get_param_names_plain_list()
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + param_list + method.get_comment_tokens() + method.get_body_words()


class MethodCommentSignatureChooser(MethodWordChooser):
    """
    Chooses classifier name as prefix, the method signature and comment words: name, return type, param names, param types, method (javadoc) comment
    """

    def __init__(self, use_param_types=True):
        self.use_param_types = use_param_types

    def choose_words_from(self, classifier: Classifier, method: Method):

        if self.use_param_types:
            param_list = method.get_param_plain_list()
        else:
            param_list = method.get_param_names_plain_list()
        return classifier.get_name_words() + method.get_name_words() + method.get_returntype_words() + param_list + method.get_comment_tokens()


class RequirementWordChooser(abc.ABC):

    @abc.abstractmethod
    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> {}:
        pass


class UCNameWordChooser(RequirementWordChooser):
    """
    Chooses the UC name words
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> {}:
        result = {
            use_case_file_representation.get_id_for_attribute("name_words"): use_case_file_representation.name_words}
        return result


class UCNameDescWordChooser(RequirementWordChooser):
    """
    Chooses the UC name and description words, each of them grouped in the return list
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> {}:
        result = {
            use_case_file_representation.get_id_for_attribute("name_words"): use_case_file_representation.name_words,
            use_case_file_representation.get_id_for_attribute("description_words"): use_case_file_representation.description_words}
        return result


class UCNameDescFlowWordChooser(RequirementWordChooser):
    """
    Chooses the UC name, description and the flow of events words.
    Sentences in flow of events are in separate groups.
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> {}:
        result = {
            use_case_file_representation.get_id_for_attribute("name_words"): use_case_file_representation.name_words,
            use_case_file_representation.get_id_for_attribute(
                "description_words"): use_case_file_representation.description_words}
        result.update(use_case_file_representation.get_grouped_id_value_pairs("flow_of_events_words"))
        return result

class UCAllWordChooser(RequirementWordChooser):
    """
    Chooses words of all UC template entries.
    """

    def choose_words_from(self, use_case_file_representation: UseCaseFileRepresentation) -> {}:
        return use_case_file_representation.get_all_groups()


class SentenceChooser(RequirementWordChooser):

    def choose_words_from(self, text_file_grouped_representation: TextFileGroupedRepresentation) -> {}:
        return text_file_grouped_representation.get_grouped_id_value_pairs()
