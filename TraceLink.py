from Paths import *

log = logging.getLogger(__name__)


class TraceLink:
    
    def __init__(self, req_embedding, code_embedding, similarity=None, valid=False):
        self.req_embedding = req_embedding
        self.code_embedding = code_embedding
        self.valid = valid
        self.similarity = similarity
        
    def get_req_key(self, with_extension=False):
        if with_extension:
            return self.req_embedding.file_name
        return self.req_embedding
    
    def get_code_key(self, with_extension=False):
        if with_extension:
            return self.code_embedding.file_name
        return self.code_embedding
    
    def __repr__(self):
        return f"{self.req_embedding} <-> {self.code_embedding}: {self.similarity}"

    def __eq__(self, other):
        return self.get_req_key() == other.get_req_key() and self.get_code_key() == other.get_code_key()
    
    def __hash__(self):
        return hash((self.get_req_key(), self.get_code_key()))

           
class CodeToRequirementsCandidates:
    """
    Represents a single code embedding and his requirement embeddings
    
    """

    def __init__(self, code_embedding):
        self.code_embedding = code_embedding
        self.req_embedding_candidates = []  # List of trace links of the same code embedding to various requirement embeddings
        
    def add_req_candidate(self, similarity, req_embedding):
        self.req_embedding_candidates.append(TraceLink(req_embedding, self.code_embedding, similarity=similarity))
        
    def has_tracelinks(self):
        return bool(self.req_embedding_candidates)
    
    def get_codefile_name(self):
        return self.code_embedding.file_name
    
    def get_all_candidates(self) -> [TraceLink]:
        return self.req_embedding_candidates
        
        
def normalize_value_range(trace_link_candidates):
        # map value range of wmd_sims to [0,1]
        if not trace_link_candidates:
            return trace_link_candidates
        first_sim = trace_link_candidates[0].get_all_candidates()[0].similarity
        max_val, min_val = first_sim, first_sim
        
        for tlc in trace_link_candidates:
            for link in tlc.get_all_candidates():
                if link.similarity < min_val:
                    min_val = link.similarity
                if link.similarity > max_val:
                    max_val = link.similarity
        # print("Min wmd distance: {}, Max wmd distance: {}".format(min_val, max_val))
        if max_val == min_val:
            return trace_link_candidates
        for tlc in trace_link_candidates:
            for link in tlc.get_all_candidates():
                link.similarity = (link.similarity - min_val) / (max_val - min_val)
        return trace_link_candidates
    
