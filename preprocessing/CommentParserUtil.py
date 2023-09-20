"""
Comments in C files needs to be parsed separately because the pycparser does not parse comments
"""
import logging
from comment_parser import comment_parser
from preprocessing.CodeFileRepresentation import IdentifierString, Enum_
from preprocessing.Tokenizer import WordTokenizer

log = logging.getLogger(__name__)

def parse_and_add_comments_to_file(class_obj, file_path, tokenizer, mime):
    comment_list = comment_parser.extract_comments(file_path, mime)
    code_elem_dict = {}
    all_line_beginnings = []
    for method in class_obj.methods:
        code_elem_dict[method.line] = method
        all_line_beginnings.append(method.line)
    for attr in class_obj.attributes:
        code_elem_dict[attr.line] = attr
        all_line_beginnings.append(attr.line)
    for inner_classif in class_obj.inner_classifiers:
        code_elem_dict[inner_classif.line] = inner_classif
        all_line_beginnings.append(inner_classif.line)
    all_line_beginnings.sort()
    
    for comm in comment_list:
        comm_begin = comm.line_number()
        line_after_comm_end = comm_begin + 1 # one line comment
        if comm.is_multiline():# multi line comment
            comm_text = comm.text()
            line_after_comm_end += comm_text.count("\n")
            if comm_text.startswith("\n"):
                comm_text = comm_text[1:]
                
            if line_after_comm_end < all_line_beginnings[0]:
                continue # Comment is before the first function/typedef, but doesn't belong to it -> ignore
            elif line_after_comm_end in code_elem_dict: # Top level comment (not inside method body) that belongs to a function/typedef
                code_elem_dict[line_after_comm_end].comment = IdentifierString(code_elem_dict[line_after_comm_end].name.file_name, *(tokenizer.tokenize_to_string_list(comm_text)))
            else: # comment inside method body
                containing_method = code_elem_dict[_find_closest_line_smaller_than(comm_begin, all_line_beginnings)]
                if isinstance(containing_method, Enum_):
                    continue
                # Add comment to the method body
                containing_method.body += IdentifierString(containing_method.name.file_name, *(tokenizer.tokenize_to_string_list(comm_text)))
    return class_obj


def _find_closest_line_smaller_than(line, all_line_beginnings):
    i = 0
    while i + 1 < len(all_line_beginnings) and all_line_beginnings[i + 1] < line:
        i += 1
    return all_line_beginnings[i]
