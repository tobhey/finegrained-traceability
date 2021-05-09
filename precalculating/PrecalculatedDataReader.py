import CodeToRequirementFileLevelTraceLink
import FileUtil
import abc
from TwoDimensionalMatrix import TwoDimensionalMatrix
from precalculating.TraceLinkPrecalculator import ElementLevelTraceLinkPrecalculator


class PrecalculatedDataReader(abc.ABC):
    
    @abc.abstractmethod
    @classmethod
    def read_preculated_file(cls, file_path) -> [CodeToRequirementFileLevelTraceLink]:
        pass
    
    
class FileLevelTraceLinkDataReader(PrecalculatedDataReader):
    
    @classmethod
    def read_preculated_file(cls, file_path) -> [CodeToRequirementFileLevelTraceLink]:
        req_code_similarity_matrix = TwoDimensionalMatrix.read_from_csv(file_path)
        filelevel_tracelink_list = []
        for req_filename in req_code_similarity_matrix:
            for code_filename in req_code_similarity_matrix:
                filelevel_tracelink_list.append(cls._create_filelevel_tracelink(req_code_similarity_matrix, req_filename, code_filename))
        return filelevel_tracelink_list

    @classmethod
    def _create_filelevel_tracelink(cls, req_code_similarity_matrix, req_filename, code_filename):
        filelevel_tracelink = CodeToRequirementFileLevelTraceLink()
        filelevel_tracelink.req_file = req_filename
        filelevel_tracelink.code_file = code_filename
        filelevel_tracelink.file_level_similarity = req_code_similarity_matrix.get_value(req_filename, code_filename)
        return filelevel_tracelink
    
    

class ElementLevelTraceLinkDataReader(PrecalculatedDataReader):
    # Todo Klass zur repräs von ElementLevel-Beziehungen hinzufügen
    @classmethod
    def read_preculated_file(cls, file_path) -> [CodeToRequirementFileLevelTraceLink]:
        precalculated_file = FileUtil.read_from_json(file_path)
        filelevel_tracelink_list = []
        for code_file in precalculated_file:
            filelevel_tracelink_list.append(cls._create_filelevel_tracelink(code_file))
        return filelevel_tracelink_list
    
    @classmethod
    def _create_filelevel_tracelink(cls, code_file):
        filelevel_tracelink = CodeToRequirementFileLevelTraceLink()
        filelevel_tracelink.code_file = code_file[ElementLevelTraceLinkPrecalculator.CODE_FILENAME]
        
        method_to_req_element_trace_links = []
        for method in code_file[ElementLevelTraceLinkPrecalculator.METHOD_LIST]:
            method_to_req_element_trace_links.append(cls._create_code_element_to_req_tracelink(method))
        filelevel_tracelink.method_to_req_element_trace_links = method_to_req_element_trace_links
            
        non_callgraph_code_element_to_req_element_trace_links = []
        for method in code_file[ElementLevelTraceLinkPrecalculator.NON_CG_CODE_ELEMENT_LIST]:
            non_callgraph_code_element_to_req_element_trace_links.append(cls._create_code_element_to_req_tracelink(method))
        filelevel_tracelink.non_callgraph_code_element_to_req_element_trace_links = non_callgraph_code_element_to_req_element_trace_links

            
            
    @classmethod
    def _create_code_element_to_req_tracelink(cls, code_element):