import javalang, FileUtil
from Preprocessing import Tokenizer, JavaLangUtil, CodeFileRepresentation, PycparserUtil
from Preprocessing.Tokenizer import WordTokenizer
from pycparser import parse_file
from Preprocessing.CommentParserUtil import parse_and_add_comments_to_file
from Dataset import Libest

"""
Tokenizer and AST parser for code files.
"""

class JavaCodeASTTokenizer(Tokenizer.Tokenizer):
    def __init__(self, dataset, tokenizer_for_comments):
        super(JavaCodeASTTokenizer, self).__init__(dataset)
        self._tokenizer_for_comments = tokenizer_for_comments
    def tokenize(self, file_path):
        text_as_string = FileUtil.read_textfile_into_string(file_path, self._dataset.encoding())
        tree = javalang.parse.parse(text_as_string)
        JavaLangUtil.COMMENT_TOKENIZER = self._tokenizer_for_comments
        file_name = FileUtil.get_filename_from_path(file_path)
        class_objects = [JavaLangUtil.extract_type(node, file_name) for node in tree.types]
        return CodeFileRepresentation.CodeFileRepresentation(class_objects, file_path)
    
    
    
class CCodeASTTokenizer(Tokenizer.Tokenizer):
    def __init__(self, dataset, tokenizer_for_comments=WordTokenizer(Libest())):
        super(CCodeASTTokenizer, self).__init__(dataset)
        self._tokenizer_for_comments = tokenizer_for_comments
        
    def tokenize(self, file_path):
        tree = parse_file(file_path, True, cpp_args=r'-I'+ str(Libest.FAKE_C_LIB_HEADER))
        class_object = PycparserUtil.extract_FileAST(tree, file_path)
        class_object = parse_and_add_comments_to_file(class_object, file_path, self._tokenizer_for_comments)
        return CodeFileRepresentation.CodeFileRepresentation([class_object], file_path)

