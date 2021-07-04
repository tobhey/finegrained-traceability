from preprocessing.FileRepresentation import FileRepresentation, Preprocessable
from utility import Util


class CodeFileRepresentation(FileRepresentation):
    """
    Represents a code file (compilation unit).
    Can contain multiple top-level classifiers.
    """
    
    def __init__(self, classifiers, file_path):
        self.classifiers = classifiers
        super(CodeFileRepresentation, self).__init__(file_path)
        self.token_list = self._create_token_list()  # List of all tokens / word in the code file representation
                
    def _create_token_list(self):
        all_tokens = []
        for classifier in self.classifiers:
            all_tokens += classifier.token_list
        return all_tokens
    
    def preprocess(self, preprocessor):
        for cls in self.classifiers:
            cls.preprocess(preprocessor)
            
    def get_all_comments(self, class_comm=True, meth_comm=True, attr_comm=True):
        """ each comment token as independent list element, grouped by comment
           [[comm_1_token1, comm_1_token_2], [comm2_token1, comm2_token_2],...]
           token can be a single word or sentence, depending on the used tokenizer
        """
        all_comms = []
        for cls in self.classifiers:
            c = cls.get_all_comment_tokens(class_comm, meth_comm, attr_comm)
            if c:
                all_comms += c
        return all_comms
        
    def get_all_whole_comment_strings_as_list(self, class_comm=True, meth_comm=True, attr_comm=True):
        # each result list element is a single comment string containing all its sentences
        all_comms = self.get_all_comments(class_comm, meth_comm, attr_comm)
        return [" ".join(comm) for comm in all_comms]
    
    def get_all_comment_tokens_as_list(self, class_comm=True, meth_comm=True, attr_comm=True):
        # Each list elem is a comment token and not grouped by comment (flat list)
        # token can be a single word or sentence, depending on the used tokenizer
        all_comms = self.get_all_comments(class_comm, meth_comm, attr_comm)
        flat_list = []
        for comm in all_comms:
            for comm_token in comm:
                flat_list.append(comm_token)
        return flat_list
                                 
    def get_printable_string(self):
        return "\n".join(map(lambda x: x.get_printable_string(), self.classifiers))

    
class IdentifierString(Preprocessable):
    """
    Represents an identifier. An identifier can be a word composite.
    An IdentifierString internally consists of a list of strings.
    Separate the composite tokens through preprocessing.
    """

    def __init__(self, file_name, *tokens: str):
        """
        Takes strings as arguments. These strings can be word composites.
        Separate the tokens by calling the preprocess() method with an
        appropriate preprocessor.
        """
        self.file_name = file_name
        self.tokens = []
        for word in tokens:
            self.tokens.append(word)
        
    def preprocess(self, preprocessor):
        if self.tokens:
            self.tokens = preprocessor.run_preprocessing(self.tokens)
            self.__clean_up_strings()
        
    def __clean_up_strings(self):  # Delete any empty strings in the word list
        self.tokens = [word for word in self.tokens if word and not word == ""]

    def get_printable_string(self):
        return "|".join(self.tokens)
    
    def __repr__(self):
        return "".join(self.tokens)
    
    def __add__(self, other):
        return IdentifierString(*(self.tokens + other.tokens))
    
    def __bool__(self):
        self.__clean_up_strings()
        return bool(self.tokens)
    
        
class Parameter(Preprocessable):

    def __init__(self, param_type: IdentifierString, param_name: IdentifierString):
        self.param_type = param_type
        self.param_name = param_name
        
    def get_param_tuple(self) -> ([str], [str]):
        """
        Returns parameter as: ([type, tokens], [name, tokens]).
        The type and name string list can contain multiple tokens if a camel case splitter was applied beforehand.
        Returns None if both type and name are None or empty (possibly due to preprocessing)
        """
        param_tuple = [None] * 2
        if self.param_type:
            param_tuple[0] = self.param_type.tokens
        if self.param_name:
            param_tuple[1] = self.param_name.tokens
        if param_tuple == [None, None]:
            return None
        return tuple(param_tuple)
    
    def get_param_words(self) -> [str]:
        """
        Returns the type and the name as plain list. No assumptions about order.
        """
        return self.param_type.tokens + self.param_name.tokens
    
    def __bool__(self):
        return bool(self.param_name) or bool(self.param_type)
        
    def preprocess(self, preprocessor):
        self.param_name.preprocess(preprocessor)
        self.param_type.preprocess(preprocessor)
            
    def get_printable_string(self):
        return self.param_type.get_printable_string() + ":" + self.param_name.get_printable_string()


class Method(Preprocessable):
    """
    Represents a method.
    body_statements contains all identifiers in the method body.
    """

    def __init__(self, return_type: IdentifierString, name: IdentifierString,
                  comment: IdentifierString, body: IdentifierString, left_side_identifiers: IdentifierString, parameters: [Parameter]=[], line=None):
        self.return_type = return_type
        self.name = name
        self.original_name = Util.deep_copy(name)
        self.parameters = parameters
        self.original_parameters = Util.deep_copy(parameters)
        self.comment = comment
        self.body = body
        self.left_side_identifiers = left_side_identifiers
        self.line = line  # The line number in the code file where the method signature is written
        self.token_list = self._create_token_list()
    
    def _create_token_list(self):
        return self.get_name_words() + self.get_param_plain_list() + self.get_returntype_words() + self.get_comment_tokens() + self.get_body_words()
    
    def set_params(self, param_list):
        self.parameters = param_list
        self.original_parameters = Util.deep_copy(param_list)
        
    def get_body_words(self) -> [str]:
        """
        Returns the method body identifier tokens as string list.
        The string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        return self.body.tokens

    def get_left_side_identifier_words(self) -> [str]:
        """
        Returns the identifiers of the method body which are on the left side of an assignment as a string list.
        The string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        return self.left_side_identifiers.tokens

    def get_name_words(self) -> [str]:
        """
        Returns the method name tokens as string list.
        The string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        return self.name.tokens

    def get_original_name(self) -> [str]:
        """
        Returns the original un-preprocessed method name
        """
        assert len(self.original_name.tokens) == 1
        return self.original_name.tokens[0]
    
    def get_returntype_words(self) -> [str]:
        """
        Returns the return type tokens as string list.
        The string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        return self.return_type.tokens

    def get_param_tuples(self) -> [([str], [str])]:  # returns [([param_type], [param_name])]
        """
        Returns the param tokens as tuple list:
        [([boolean], [is, active]), ([type, tokens], [name, tokens]), ...]
        The type and name string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        self.__check_and_clean_param_list
        return [param.get_param_tuple() for param in self.parameters]
    
    def get_param_plain_list(self) -> [str]:
        """
        Returns param tokens as plain string list (No assumptions about order):
        [type1, name1, type2, name2...]
        """
        self.__check_and_clean_param_list
        param_words = []
        for param in self.parameters:
            param_words.extend(param.get_param_words())
        return param_words
    
    def get_original_param_type_list(self) -> [str]:
        """
        Returns un-preprocessed param types as plain string list.
        """
        result_list = []
        for param in self.original_parameters:
            if len(param.param_type.tokens) > 1:
                print("d")
            assert len(param.param_type.tokens) == 1, param.param_type.tokens
            result_list += [param.param_type.tokens[0]]
        return result_list
    
    def get_comment_tokens(self) -> [str]:
        """
        Returns the comment tokens as string list.
        """
        if self.comment:
            return self.comment.tokens
        return []

    def __bool__(self):  # False if no name, no return, no params, no comment and no identifiers in body
        return (bool(self.name) or bool(self.comment) or bool(self.body) or bool(self.left_side_identifiers)
                or bool(self.return_type) or self.__check_and_clean_param_list())
        
    def preprocess(self, preprocessor):
        self.name.preprocess(preprocessor)
        for param in self.parameters:
            param.preprocess(preprocessor)
        self.body.preprocess(preprocessor)
        self.left_side_identifiers.preprocess(preprocessor)
        self.return_type.preprocess(preprocessor)
        preprocessor.javadoc = True
        self.comment.preprocess(preprocessor)
            
    def __check_and_clean_param_list(self):  # Deletes parameters if empty
        if self.parameters:
            for i, param in enumerate(self.parameters):
                if not param:
                    self.parameters.pop(i)
        return bool(self.parameters)  # return true if param list is empty
    
    def get_printable_string(self):
        method_string = ""
        if self.comment:
            method_string = "[METH_COMM] " + self.comment.get_printable_string() + "\n"
        params_string = ", ".join(map(lambda p : p.get_printable_string(), self.parameters))
        method_string += "METH| " + self.return_type.get_printable_string() + " " + self.name.get_printable_string() + "(" + params_string + ")\n"
        if self.body:
            method_string += "[ALL_BODY_IDENTIFIERS] " + self.body.get_printable_string()
        if self.left_side_identifiers:
            method_string += "\n[LEFTSIDE_IDENTIFIERS] " + self.left_side_identifiers.get_printable_string()
        method_string += "\n"
        return  method_string


class Attribute(Preprocessable):

    def __init__(self, a_type: IdentifierString, a_name: IdentifierString, init_value: IdentifierString,
                  comment: IdentifierString, line=None):
        self.a_type = a_type
        self.a_name = a_name
        self.init_value = init_value
        self.comment = comment
        self.line = line
        self.token_list = self._create_token_list()
    
    def _create_token_list(self):
        return self.get_attribute_plain_list() + self.get_comment_tokens()
        
    def get_attribute_tuple(self) -> ([str], [str], [str], [str]):
        """
        Returns the attribute type, name and comment tokens as tuple:
        ([type, tokens], [name, tokens], [init, value, tokens], [comment, tokens])
        If all values are emtpy, a single, plain None is returned
        """
        attr_tuple = (self.a_type.tokens, self.a_name.tokens, self.init_value.tokens, self.comment.tokens)
        if attr_tuple == ([], [], [], []):
            return None
        return attr_tuple
    
    def get_attribute_plain_list(self) -> [str]:
        return self.a_type.tokens + self.a_name.tokens + self.init_value.tokens
    
    def get_comment_tokens(self) -> [str]:
        if self.comment:
            return self.comment.tokens
        return []

    def __bool__(self):
        return bool(self.a_name) or bool(self.a_type) or bool(self.init_value) or bool(self.comment)
            
    def preprocess(self, preprocessor):
        self.a_name.preprocess(preprocessor)
        self.a_type.preprocess(preprocessor)
        self.init_value.preprocess(preprocessor)
        preprocessor.javadoc = True
        self.comment.preprocess(preprocessor)
            
    def get_printable_string(self):
        attr_string = ""
        if self.comment:
            attr_string = self.comment.get_printable_string() + "\n"
        attr_string += ("ATTR| " + self.a_type.get_printable_string() + " " + self.a_name.get_printable_string()
                        +self.init_value.get_printable_string())
        return attr_string

     
class Classifier(Preprocessable):
    
    def __init__(self, name: IdentifierString, comment: IdentifierString, attributes: [Attribute]=[],
                 methods: [Method]=[], inner_classifiers=[], extended_classifiers=[], implemented_classifiers=[], line=None):
        self.name = name
        self.original_name = Util.deep_copy(name)
        self.extended_classifiers = extended_classifiers
        self.implemented_classifiers = implemented_classifiers
        self.attributes = attributes
        self.methods = methods
        self.comment = comment
        self.inner_classifiers = inner_classifiers
        self.line = line
        self.token_list = self._create_token_list()
        
    def _create_token_list(self):
        all_tokens = []
        all_tokens += self.get_name_words()
        all_tokens += self.get_super_classifiers_plain_list()
        all_tokens += self.get_comment_tokens()
        for attr in self.attributes:
            all_tokens += attr.token_list
        for meth in self.methods:
            all_tokens += meth.token_list
        for inner_cls in self.inner_classifiers:
            all_tokens += inner_cls.token_list
        return all_tokens
        
    def get_super_classifiers(self) -> [[str]]:
        """
        Returns all super classifier (no discrimination between interfaces and classes) as:
        [[first, super, classifier, tokens], [second, super, classifier, tokens], ...] 
        The internal super classifier string list can contain multiple tokens 
        if a camel case splitter was applied beforehand.
        """
        super_classifiers = self.extended_classifiers + self.implemented_classifiers
        if super_classifiers:
            return [s_clsf.tokens for s_clsf in super_classifiers]
        return None
    
    def get_super_classifiers_plain_list(self) -> [str]:
        res = []
        super_classifiers = self.extended_classifiers + self.implemented_classifiers
        for s_cls in super_classifiers:
            res += s_cls.tokens
        return res
    
    def get_extended_classifiers_plain_list(self) -> [str]:
        res = []
        for s_cls in self.extended_classifiers:
            res += s_cls.tokens
        return res
    
    def get_implemented_classifiers_plain_list(self) -> [str]:
        res = []
        for s_cls in self.implemented_classifiers:
            res += s_cls.tokens
        return res

    def get_name_words(self) -> [str]:
        """
        Returns the class name tokens as string list.
        The string list can contain multiple tokens if a camel case splitter was applied beforehand.
        """
        return self.name.tokens
    
    def get_original_name(self) -> str:
        """
        Returns the original un-preprocessed class name
        """
        assert len(self.original_name.tokens) == 1
        return self.original_name.tokens[0]
    
    def get_attribute_tuples(self):
        return [a.get_attribute_tuple() for a in self.attributes]
    
    def get_comment_tokens(self) -> [str]:
        """
        Returns only the class comment
        """
        if self.comment:
            return self.comment.tokens
        return []
    
    def get_all_comment_tokens(self, class_comm=True, meth_comm=True, attr_comm=True):
        all_comms = []
        if class_comm:
            if self.get_comment_tokens():
                all_comms += [self.get_comment_tokens()]
        if attr_comm:
            for attr in self.attributes:
                if attr.get_comment_tokens():
                    all_comms += [attr.get_comment_tokens()]
        if meth_comm:
            for meth in self.methods:
                if meth.get_comment_tokens():
                    all_comms += [meth.get_comment_tokens()]
        return all_comms

    def preprocess(self, preprocessor):
        self.name.preprocess(preprocessor)
        preprocessor.javadoc = True
        self.comment.preprocess(preprocessor)
        for s in self.extended_classifiers:
            s.preprocess(preprocessor)
        for s in self.implemented_classifiers:
            s.preprocess(preprocessor)
        for a in self.attributes:
            a.preprocess(preprocessor)
        for m in self.methods:
            m.preprocess(preprocessor)
        for i in self.inner_classifiers:
            i.preprocess(preprocessor)
        self.__delete_empty_attributes()
        self.__delete_empty_methods()
        self.__delete_empty_extended_classifiers()
        self.__delete_empty_implemented_classifiers()
        self.__delete_empty_inner_classifiers()
        
    def __delete_empty_attributes(self):
        # Deletes all empty attributes in self.attributes 
        for i, attr in enumerate(self.attributes):
            if not attr:
                self.attributes.pop(i)
            
    def __delete_empty_methods(self): 
        # Deletes all empty method in self.methods
        for i, meth in enumerate(self.methods):
            if not meth:
                self.methods.pop(i)

    def __delete_empty_extended_classifiers(self):
        # Deletes all empty super classifiers in self.super_classifiers
        for i, s_cls in enumerate(self.extended_classifiers):
            if not s_cls:
                self.extended_classifiers.pop(i)
        
    def __delete_empty_implemented_classifiers(self):
        # Deletes all empty super classifiers in self.implemented_classifiers
        for i, s_cls in enumerate(self.implemented_classifiers):
            if not s_cls:
                self.implemented_classifiers.pop(i)  
                      
    def __delete_empty_inner_classifiers(self):
        # Deletes all empty inner classifiers in self.inner_classifiers
        for i, i_cls in enumerate(self.inner_classifiers):
            if not i_cls:
                self.inner_classifiers.pop(i)
          
    def __bool__(self):
        return bool(self.name) or bool(self.extended_classifiers) or bool(self.__delete_empty_implemented_classifiers) or bool(self.methods) or bool(self.comment) \
            or bool(self.attributes) or bool(self.inner_classifiers)
              
    def get_printable_string(self):
        comment_string = super_classifier_string = method_string = attr_string = inner_string = ""
        if self.comment:
            comment_string = "[CLASS_COMM] " + self.comment.get_printable_string() + "\n"
        super_classifiers = self.extended_classifiers + self.implemented_classifiers
        if super_classifiers:
            super_classifier_string = ", ".join(map(lambda s: s.get_printable_string(), super_classifiers))
        if self.attributes:
            attr_string = "\n".join(map(lambda a : a.get_printable_string(), self.attributes))
        if self.methods:
            method_string = "\n".join(map(lambda m : m.get_printable_string(), self.methods))
        if self.inner_classifiers:
            inner_string = "\n".join(map(lambda i : i.get_printable_string(), self.inner_classifiers))
        return (comment_string + "CLASSIF| " + self.name.get_printable_string() + "(" + super_classifier_string + ")\n"
                        +inner_string + "\n" + attr_string + "\n" + method_string)
       

class Enum_(Classifier):

    def __init__(self, name: IdentifierString, comment: IdentifierString, attributes: [Attribute]=[],
                 methods: [Method]=[], constants=[], extended_classifiers=[], implemented_classifiers=[], line=None):
        self.constants = constants
        super(Enum_, self).__init__(name, comment, attributes, methods, [], extended_classifiers, implemented_classifiers, line)
        
    def _create_token_list(self):
        all_tokens = super(Enum_, self)._create_token_list()
        all_tokens += self.get_constant_words()
        return all_tokens
    
    def get_constant_words(self) -> [str]:
        constant_words = []
        for c in self.constants:
            constant_words += c.tokens
        return constant_words
    
    def preprocess(self, preprocessor):
        super(Enum_, self).preprocess(preprocessor)
        for c in self.constants:
            c.preprocess(preprocessor)
        self.__delete_empty_constants()
        
    def __delete_empty_constants(self):
        for i, constant in enumerate(self.constants):
            if not constant:
                self.constants.pop(i)
    
    def __bool__(self):
        return bool(self.name) or bool(self.extended_classifiers) or bool(self.methods) or bool(self.comment) \
            or bool(self.attributes) or bool(self.constants)or bool(self.implemented_classifiers)
    
    def get_printable_string(self):
        comment_string = super_classifier_string = method_string = attr_string = const_string = ""
        if self.comment:
            comment_string = self.comment.get_printable_string() + "\n"
        super_classifiers = self.extended_classifiers + self.implemented_classifiers
        if super_classifiers:
            super_classifier_string = ", ".join(map(lambda s: s.get_printable_string(), super_classifiers))
        if self.attributes:
            attr_string = "\n".join(map(lambda a : a.get_printable_string(), self.attributes))
        if self.methods:
            method_string = "\n".join(map(lambda m : m.get_printable_string(), self.methods))
            
        if self.constants:
            const_string = ", ".join(map(lambda c : c.get_printable_string(), self.constants))
        return (comment_string + "ENUM| " + self.name.get_printable_string() + "(" + super_classifier_string + ")\n"
                        +const_string + "\n" + attr_string + "\n" + method_string)
