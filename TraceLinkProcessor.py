from typing import Dict, List
from TraceLink import TraceLink
from TraceLinkCreator import MajorityDecision, FileLevelTraceLinkCreator, \
    MajorityDecisionTraceLinkCreator


class TraceLinkFilter:
    
    def __init__(self, similarity_filter, normalize=False):
        self._similarity_filter = similarity_filter
        # Hier würde normalisiert werden
        pass
    
    def run(self, trace_links, drop_thresholds) -> Dict[float, List[TraceLink]]:
        """
        Returns a dictionary. File level threshold as keys, filtered trace link list as value
        """
        filtered_trace_links = {}
        for thresh in drop_thresholds:
            filtered_trace_links[thresh] = self._apply_threshold(trace_links, thresh)
        return filtered_trace_links
            
    def _apply_threshold(self, trace_links, drop_thresh):
        return [trace_link for trace_link in trace_links if self._similarity_filter.is_more_similar(trace_link.similarity, drop_thresh)]

            
class FileLevelProcessor:

    def __init__(self, trace_link_data_structure, similarity_filter, file_level_thresholds):
        self._trace_link_creator = FileLevelTraceLinkCreator(trace_link_data_structure)
        self._file_level_filter = TraceLinkFilter(similarity_filter)
        self._file_level_thresholds = file_level_thresholds
        
    def run(self) -> Dict[float, List[TraceLink]]:
        file_level_trace_links = self._trace_link_creator.process()
        return self._file_level_filter.run(file_level_trace_links, self._file_level_thresholds)


class MajProcessor:

    def __init__(self, trace_link_data_structure, similarity_filter, req_reduce_func, code_reduce_function, file_level_thresholds, maj_thresholds, callgraph_aggregator=None):
        self._maj_decision = MajorityDecisionTraceLinkCreator(trace_link_data_structure, similarity_filter, req_reduce_func, code_reduce_function, callgraph_aggregator)
        self._file_level_filter = TraceLinkFilter(similarity_filter)
        self._maj_thresholds = maj_thresholds
        self._file_level_thresholds = file_level_thresholds
        self._similarity_filter = similarity_filter
    
    def run(self) -> Dict[float, Dict[float, List[TraceLink]]]:
        processed_trace_links = {}
        for maj_threshold in self._maj_thresholds:
            file_level_trace_links = self._maj_decision.process(maj_threshold)
            
            # Optimization: Skip calculation if file level threshold is more similar than majority threshold
            file_threholds_to_process, file_thresholds_to_skip = self.determine_relevant_thresholds(maj_threshold)
            file_level_trace_links = self._file_level_filter.run(file_level_trace_links, file_threholds_to_process)
            for thresh in file_thresholds_to_skip:
                file_level_trace_links[thresh] = "Skipped"
            processed_trace_links[maj_threshold] = file_level_trace_links
        return processed_trace_links
    
    def determine_relevant_thresholds(self, maj_threshold):
        file_threholds_to_process, file_thresholds_to_skip = [], []
        for thresh in self._file_level_thresholds:
            file_thresholds_to_skip.append(thresh) if self._similarity_filter.is_more_similar(maj_threshold, thresh) else file_threholds_to_process.append(thresh)
        return file_threholds_to_process, file_thresholds_to_skip
