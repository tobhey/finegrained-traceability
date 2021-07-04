
import javalang

from preprocessing import Tokenizer, JavaLangUtil
from preprocessing.CodeFileRepresentation import CodeFileRepresentation
from utility import FileUtil


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

