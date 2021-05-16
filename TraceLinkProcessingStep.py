from abc import ABC
import logging
from TraceLink import TraceLink
from precalculating.AllTraceLinkCombinations import AllFileLevelTraceLinkCombinations

log = logging.getLogger(__name__)


class TraceLinkProcessingStep(ABC):
    
    def __init__(self, all_trace_link_combinations):
        self._all_trace_link_combinations = all_trace_link_combinations
    
    
class FileLevelTraceLinkCreator(TraceLinkProcessingStep):

    def __init__(self, all_trace_link_combinations):
        assert isinstance(all_trace_link_combinations, AllFileLevelTraceLinkCombinations)
        super(FileLevelTraceLinkCreator, self).__init__(all_trace_link_combinations)

    def process(self) -> [TraceLink]:
        all_trace_links = []
        for req_file_name in self._all_trace_link_combinations.all_req_file_names:
            for code_file_name in self._all_trace_link_combinations.all_code_file_names:
                similarity = self._all_trace_link_combinations.similarity_between(req_file_name, code_file_name)
                all_trace_links.append(TraceLink(req_file_name, code_file_name, similarity))
        return all_trace_links


class MajorityDecisionTraceLinkCreator(TraceLinkProcessingStep):
    