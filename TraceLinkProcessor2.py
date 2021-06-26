
from TraceLink import TraceLink
from TraceLinkProcessingStep import MajorityDecision, FileLevelTraceLinkCreator, \
    MajorityDecisionTraceLinkCreator


class TraceLinkFilter:
    
    def __init__(self, similarity_filter, normalize=False):
        self._similarity_filter = similarity_filter
        # Hier würde normalisiert werden
        pass
    
    def run(self, trace_links, drop_thresholds) -> dict[float, [TraceLink]]:
        """
        Returns a dictionary. File level threshold as keys, filtered trace link list as value
        """
        filtered_trace_links = {}
        for thresh in drop_thresholds:
            filtered_trace_links[thresh] = self._apply_threshold(trace_links, thresh)
        return filtered_trace_links
            
    def _apply_threshold(self, trace_links, drop_thresh):
        return [self._similarity_filter(trace_link.similarity, drop_thresh) for trace_link in trace_links]

            
class FileLevelProcessor:

    def __init__(self, trace_link_provider, similarity_filter, file_level_thresholds):
        self._trace_link_creator = FileLevelTraceLinkCreator(trace_link_provider)
        self._file_level_filter = TraceLinkFilter(similarity_filter)
        self._file_level_thresholds = file_level_thresholds
        
    def run(self) -> dict[float, [TraceLink]]:
        file_level_trace_links = self._trace_link_creator.process()
        return self._file_level_filter.run(file_level_trace_links, self._file_level_thresholds)


class MajProcessor:

    def __init__(self, trace_link_provider, similarity_filter, req_reduce_func, code_reduce_function, file_level_thresholds, maj_thresholds, callgraph_aggregator=None):
        self._maj_decision = MajorityDecisionTraceLinkCreator(trace_link_provider, similarity_filter, req_reduce_func, code_reduce_function, callgraph_aggregator)
        self._file_level_filter = TraceLinkFilter(similarity_filter)
        self._maj_thresholds = maj_thresholds
        self._file_level_thresholds = file_level_thresholds
    
    def run(self) -> dict[dict[float, [TraceLink]]]:
        processed_trace_links = {}
        for maj_threshold in self._maj_thresholds:
            file_level_trace_links = self._maj_decision.process(maj_threshold)
            processed_trace_links[maj_threshold] = self._file_level_filter.run(file_level_trace_links, self._file_level_thresholds)
        return processed_trace_links
