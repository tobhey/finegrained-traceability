from precalculating.TraceLinkPrecalculator import FileLevelTraceLinkPrecalculator,\
    ElementLevelTraceLinkPrecalculator

class TraceLinkPrecalculatorBuilder:
    """
    A TraceLinkPrecalculatorBuilder takes requirement and class embedding containers and initializes a
    TraceLinkPrecalculator with them (in order to calculate a precalculated file out of the embeddings).
    """
    def __init__(self, _req_embedding_containers, code_embedding_containers, similarity_function, output_file_path):
        self._req_embedding_containers = _req_embedding_containers
        self._code_embedding_containers = code_embedding_containers
        self._similarity_function = similarity_function
        self._output_file_path = output_file_path
        
    def build(self):
        pass
    
    
class FileLevelTraceLinkPrecalculatorBuilder(TraceLinkPrecalculatorBuilder):
    
    def build(self):
        req_dict, code_dict = {}, {}
        for req_emb_cont in self._req_embedding_containers:
            req_dict[req_emb_cont.file_name] = req_emb_cont.file_vector
        for code_emb_cont in self._code_embedding_containers:
            code_dict[code_emb_cont.file_name] = code_emb_cont.file_vector
        return FileLevelTraceLinkPrecalculator(req_dict, code_dict, self._similarity_function, self._output_file_path)
    
class ElementLevelTraceLinkPrecalculatorBuilder(TraceLinkPrecalculatorBuilder):
    
    def build(self):
        req_dict, method_dict, non_cg_code_element_dict = {}, {}, {}
        
        for req_emb_cont in self._req_embedding_containers:
            req_dict[req_emb_cont.file_name] = req_emb_cont.requirement_element_vectors
        for code_emb_cont in self._code_embedding_containers:
            for method_key in code_emb_cont.methods_dict:
                method_dict[code_emb_cont.file_name] = (method_key, code_emb_cont.methods_dict[method_key])
            for non_cg_element_key in code_emb_cont.non_cg_dict:
                non_cg_code_element_dict[code_emb_cont.file_name] = (non_cg_element_key, code_emb_cont.non_cg_dict[non_cg_element_key])
        return ElementLevelTraceLinkPrecalculator(req_dict, method_dict, non_cg_code_element_dict, self._similarity_function, self._output_file_name)