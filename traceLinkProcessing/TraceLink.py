

class TraceLink:
    
    def __init__(self, req_key, code_key, similarity=None):
        self.req_key = req_key
        self.code_key = code_key
        self.similarity = similarity
        
    def __repr__(self):
        return f"{self.req_key} <-> {self.code_key}: {self.similarity}"

    def __eq__(self, other):
        return self.req_key == other.req_key and self.code_key == other.code_key
    
    def __hash__(self):
        return hash((self.req_key, self.code_key))

