
class CodeToRequirementFileLevelTraceLink:
    
    def __init__(self):
        self.req_file
        self.code_file
        self.file_level_similarity
        self.element_level_trace_links
        
        
class CodeElementToRequirementTraceLink:
    
    def __init__(self):
        self.code_element
        self.req_file
        self.code_elem_to_req_similarity
        self.method_to_req_element_trace_links
        self.non_callgraph_code_element_to_req_element_trace_links
        
        
class ElementLevelTraceLink:
    
    def __init__(self):
        self.code_element
        self.req_element
        self.element_level_similarity