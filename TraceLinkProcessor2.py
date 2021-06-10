from test.mp_preload import proc

from TraceLink import normalize_value_range
from TraceLinkProcessingStep import MajorityDecision, FileLevelTraceLinkCreator
from precalculating.AllTraceLinkCombinations import TraceLinkProvider


class TraceLinkProcessor2:
    
    def __init__(self, file_level_trace_links, normalize=False):
        if normalize:
            self._file_level_trace_links = normalize_value_range(file_level_trace_links)
        else:
            self._file_level_trace_links = file_level_trace_links
    
    def run(self, file_level_thresholds):
        """
        Returns a dictionary. File level threshold as keys, filtered trace link list as value
        """
        filtered_file_level_trace_links = {}
        for file_level_thresh in file_level_thresholds:
            filtered_file_level_trace_links[file_level_thresh] = self._apply_file_level_threshold(self._file_level_trace_links, file_level_thresh)
        return filtered_file_level_trace_links
            
    def _apply_file_level_threshold(self, trace_links, drop_thresh):
        if self.is_reverse_compare():
            return [trace_link for trace_link in trace_links if trace_link.similarity <= drop_thresh]
        else:
            return [trace_link for trace_link in trace_links if trace_link.similarity > drop_thresh]

            
class FileLevelProcessor:

    def __init__(self, trace_link_provider, file_level_thresholds):
        self._maj_decision = FileLevelTraceLinkCreator(trace_link_provider)
        self._file_level_thresholds = file_level_thresholds
        
    def run(self):
        filtered_file_level_trace_links = self.trace_processor.run(file_level_thresholds)
        valid_trace_links = self._evaluate_tracelinks(filtered_file_level_trace_links)


class MajProcessor:

    def __init__(self, trace_link_provider, similarity_filter, code_reduce_function, file_level_thresholds, maj_thresholds):
        self._maj_decision = MajorityDecision(trace_link_provider, similarity_filter, code_reduce_function)
        self._maj_thresholds = maj_thresholds
        self._file_level_thresholds = file_level_thresholds
    
    def run(self):
        processed_trace_links = {}
        for maj_threshold in self._maj_thresholds:
            file_level_trace_links = self._maj_decision.process(maj_threshold)
            processed_trace_links[maj_threshold] = TraceLinkProcessor2(file_level_trace_links, self._normalize).run(self._file_level_thresholds)
            # eval auslagern?
            # valid_trace_links = self._evaluate_tracelinks(filtered_file_level_trace_links)
        return processed_trace_links
