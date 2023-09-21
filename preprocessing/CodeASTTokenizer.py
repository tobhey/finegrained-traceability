import logging
import os.path
from logging import getLogger

import esprima
import javalang

from preprocessing import Tokenizer, JavaLangUtil, PycparserUtil, JSParserUtil
from pycparser import parse_file
from preprocessing.CodeFileRepresentation import CodeFileRepresentation, IdentifierString, Classifier
from preprocessing.CommentParserUtil import parse_and_add_comments_to_file
from utility import FileUtil

log = getLogger(__name__)
logging.basicConfig(level=logging.INFO)

class JavaCodeASTTokenizer(Tokenizer.Tokenizer):
    """
    Tokenizes and parses a java code file and returns a code file representation object.
    """

    def __init__(self, dataset, tokenizer_for_comments):
        super(JavaCodeASTTokenizer, self).__init__(dataset)
        self._tokenizer_for_comments = tokenizer_for_comments

    def tokenize(self, file_path):
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        tree = javalang.parse.parse(text_as_string)
        JavaLangUtil.COMMENT_TOKENIZER = self._tokenizer_for_comments
        file_name = FileUtil.get_filename_from_path(file_path)
        class_objects = [JavaLangUtil.extract_type(node, file_name) for node in tree.types]
        return CodeFileRepresentation(class_objects, file_path)


class CCodeASTTokenizer(Tokenizer.Tokenizer):
    def __init__(self, dataset, tokenizer_for_comments):
        super(CCodeASTTokenizer, self).__init__(dataset)
        self._tokenizer_for_comments = tokenizer_for_comments

    def tokenize(self, file_path):
        tree = parse_file(file_path, True, cpp_args=r'-I' + str(self._dataset.FAKE_C_LIB_HEADER))
        class_object = PycparserUtil.extract_FileAST(tree, file_path)
        class_object = parse_and_add_comments_to_file(class_object, file_path, self._tokenizer_for_comments, mime='text/x-c')
        return CodeFileRepresentation([class_object], file_path)


class FileExtensionNotSupportedError(Exception):
    pass


class MixedASTTokenizer(Tokenizer.Tokenizer):


    def __init__(self, dataset, tokenizer_for_java_comments, tokenizer_for_other_comments):
        super(MixedASTTokenizer, self).__init__(dataset)
        self._tokenizer_for_java_comments = tokenizer_for_java_comments
        self._tokenizer_for_other_comments = tokenizer_for_other_comments

    def tokenize(self, file_path):
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        file_name = FileUtil.get_filename_from_path(file_path)
        if file_name.endswith(".java"):
            tree = javalang.parse.parse(text_as_string)
            JavaLangUtil.COMMENT_TOKENIZER = self._tokenizer_for_java_comments
            class_objects = [JavaLangUtil.extract_type(node, file_name) for node in tree.types]
            return CodeFileRepresentation(class_objects, file_path)
        elif file_name.endswith(".js"):
            tree = esprima.parse(text_as_string, comment=True, loc=True)
            class_object = JSParserUtil.extract_FileAST(tree, file_path)
            class_object = parse_and_add_comments_to_file(class_object, file_path, self._tokenizer_for_other_comments, mime='application/javascript')
            return CodeFileRepresentation([class_object], file_path)
        elif file_name.endswith(".jsp"):
            class_name = IdentifierString(file_name, file_name.replace(".jsp", ""))
            return CodeFileRepresentation([Classifier(class_name, IdentifierString(file_name, ""))], file_path)
        else:
            log.info("Ignore file with ending: " + str(os.path.splitext(file_path)[1]))
            raise FileExtensionNotSupportedError()

