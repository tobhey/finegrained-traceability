from Preprocessing.CodeASTTokenizer import JavaCodeASTTokenizer
from Preprocessing.Tokenizer import WordTokenizer
from Dataset import EANCI, EANCINoTrans
import FileUtil, os

def rename_eanci_code_files_and_solution_matrix():
    tok = JavaCodeASTTokenizer(EANCINoTrans(), WordTokenizer(EANCINoTrans(), True))
    code_files = FileUtil.get_files_in_directory(EANCINoTrans().code_folder(), True)
    code_file_to_class_name_map = {}
    old_sol_matrix = FileUtil.read_xml_format_solution_matrix(EANCINoTrans().folder() / "answer_req_code.xml")
    for code_file in code_files:
        code_file_representation = tok.tokenize(code_file)
        class_name = code_file_representation.classifiers[0].get_original_name()
        new_file_name = class_name + ".java"
        old_file_name = FileUtil.get_filename_from_path(code_file)
        code_file_to_class_name_map[FileUtil.get_filename_without_extension__from_path(old_file_name)] = new_file_name
        os.rename(code_file, EANCINoTrans().code_folder() / new_file_name)
    
    renamed_solution_links = []
    for old_req_name, old_code_name in old_sol_matrix.get_all_trace_links():
        renamed_solution_links.append(f"{old_req_name}.txt: {code_file_to_class_name_map[old_code_name]}")
    FileUtil.write_file(EANCINoTrans().EANCI_SOLUTION_MATRIX_PATH, "\n".join(renamed_solution_links
                                                                             
                                                                             
#rename_eanci_code_files_and_solution_matrix()
        