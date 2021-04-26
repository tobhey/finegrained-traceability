import abc
from abc import abstractclassmethod, abstractmethod
import logging
from TwoDimensionalMatrix import TwoDimensionalMatrix
import FileUtil

log = logging.getLogger(__name__)

class TraceLinkPrecalculator(abc.ABC):
    
    def __init__(self, req_dict, code_dict, similarity_function, output_file_name):
        self._req_dict = req_dict
        self._code_dict = code_dict
        self._similarity_function = similarity_function
        self._output_file_name = output_file_name
        
    @abstractmethod
    def precalculate_trace_links(self, output_file_name):
        pass
    
    
class FileLevelTraceLinkPrecalculator(TraceLinkPrecalculator):

    
    def precalculate_trace_links(self):
        similarity_matrix = TwoDimensionalMatrix.create_empty(self._req_dict.keys(), self._code_dict.keys())
        similarity_matrix = self._fill_with_similarities(similarity_matrix)
        similarity_matrix.write_to_csv(self._output_file_name)
        
    def _fill_with_similarities(self, similarity_matrix):
        num_code_files = len(self._code_dict.keys())
        
        for i, code_file_name in enumerate(self._code_dict):
            log.info(f"precalculating for code file {i + 1} of {num_code_files}")
            
            for req_file_name in self._req_dict:
                file_level_similarity = self._similarity_function(self._req_dict[req_file_name], self.code_file_entry[code_file_name])
                similarity_matrix.set_value(req_file_name, code_file_name, file_level_similarity)
                
        return similarity_matrix


class ElementLevelTraceLinkPrecalculator(TraceLinkPrecalculator):
    """
    resulting json file:  
                  
    [dict{
        code_filename=str,
        method_list=
                [dict{
                    code_element=str,
                    requirement_list=[
                        dict{
                            requirement_filename=str
                            requirement_element_list=[
                                    dict{
                                        requirement_element=str
                                        similarity=float
                                    }
                            ]
                        }
                     ]
                   }
                 ],
        non_cg_code_element_list=[ same as method_list ]
        
         }
    ]
    """
    CODE_FILENAME = "code_filename"
    METHOD_LIST = "method_list"
    NON_CG_CODE_ELEMENT_LIST = "non_cg_code_element_list"
    CODE_ELEMENT = "code_element"
    REQUIREMENT_LIST = "requirement_list"
    REQUIREMENT_FILENAME = "requirement_filename"
    REQUIREMENT_ELEMENT = "requirement_element"
    REQUIREMENT_ELEMENT_LIST = "requirement_element_list"
    SIMILARITY = "similarity"

    def __init__(self,req_dict, code_method_dict, code_non_cg_element_dict, similarity_function, output_file_name):
        super(ElementLevelTraceLinkPrecalculator, self).__init__(req_dict, code_method_dict, similarity_function, output_file_name)
        self._code_non_cg_element_dict = code_non_cg_element_dict
        
    def precalculate_trace_links(self):
        all_code_files = self._precalculate_code_files()
        if self._output_file_name:
            FileUtil.write_to_json(self._output_file_name, all_code_files)
        return all_code_files
    
    def _precalculate_code_files(self):
        all_code_files = []
        num_code_files = len(self._code_dict.keys())
        for i, code_file_name in enumerate(self._code_dict):
            log.info(f"precalculating for code file {i + 1} of {num_code_files}")
            code_file_entry = dict()
            code_file_entry[self.CODE_FILENAME] = code_file_name
            code_file_entry[self.METHOD_LIST] = self._precalculate_code_elements(self._code_dict[code_file_name])
            code_file_entry[self.NON_CG_CODE_ELEMENT_LIST] = self._precalculate_code_elements(self._code_non_cg_element_dict[code_file_name])
            
            all_code_files.append(code_file_entry)
        
        return all_code_files

    def _precalculate_code_elements(self, code_elem_list):
        code_elem_entry_list = []
        num_elems = len(code_elem_list)
        for j, (code_element_key, code_element_vector) in enumerate(code_elem_list):
            log.info(f"code element {j + 1} of {num_elems}")
            code_elem_entry = dict()
            code_elem_entry[self.CODE_ELEMENT] = code_element_key
            code_elem_entry[self.REQUIREMENT_LIST] = self._precalculate_requirement(code_element_vector)
            code_elem_entry_list.append(code_elem_entry)
        
        return code_elem_entry_list

    def _precalculate_requirement(self, code_element_vector):
        requirements_entry_list = []
        for req_file_name in self._req_dict:
            requirement_entry = dict()
            requirement_entry[self.REQUIREMENT_FILENAME] = req_file_name
            requirement_entry[self.REQUIREMENT_ELEMENT_LIST] = self._precalculate_req_elements(code_element_vector, self._req_dict[req_file_name])
            requirements_entry_list.append(requirement_entry)
        
        return requirements_entry_list

    def _precalculate_req_elements(self, code_element_vector, req_elem_list):
        requirement_elem_entry_list = []
        for req_elem in req_elem_list:
            requirement_elem_entry = dict()
            requirement_elem_entry[self.REQUIREMENT_ELEMENT] = self._set_element_if_string(req_elem)
            requirement_elem_entry[self.SIMILARITY] = self._similarity_function(req_elem, code_element_vector)
            requirement_elem_entry_list.append(requirement_elem_entry)
        return requirement_elem_entry_list


            
