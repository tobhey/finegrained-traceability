from autograd.builtins import isinstance
from Embedding import RequirementEmbedding
from precalculating.TraceLinkPrecalculator import FileLevelTraceLinkPrecalculator,\
    ElementLevelTraceLinkPrecalculator

class TraceLinkPrecalculatorBuilder:
    
    def __init__(self, req_embeddings, code_embeddings, similarity_function, output_file_path):
        self._req_embeddings = req_embeddings
        self._code_embeddings = code_embeddings
        self._similarity_function = similarity_function
        self._output_file_path = output_file_path
        
    def build(self):
        pass
    
    
class FileLevelTraceLinkPrecalculatorBuilder(TraceLinkPrecalculatorBuilder):
    
    def build(self):
        req_dict, code_dict = {}, {}
        for req in self._req_embeddings:
            req_dict[req.file_name] = req.file_vector
        for code in self._code_embeddings:
            code_dict[code.file_name] = code.file_vector
        return FileLevelTraceLinkPrecalculator(req_dict, code_dict, self._similarity_function, self._output_file_path)
    
class ElementLevelTraceLinkPrecalculatorBuilder(TraceLinkPrecalculatorBuilder):
    
    def build(self):
        req_dict, method_dict, non_cg_code_element_dict = {}, {}, {}
        
        for req_emb in self._req_embeddings:
            req_dict[req_emb.file_name] = req_emb.requirement_element_vectors
        for code_emb in self._code_embeddings:
            for method_key in code_emb.methods_dict:
                method_dict[code_emb.file_name] = (method_key, code_emb.methods_dict[method_key])
            for non_cg_element_key in code_emb.non_cg_dict:
                non_cg_code_element_dict[code_emb.file_name] = (non_cg_element_key, code_emb.non_cg_dict[non_cg_element_key])
        return ElementLevelTraceLinkPrecalculator(req_dict, method_dict, non_cg_code_element_dict, self._similarity_function, self._output_file_name)