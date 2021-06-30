from enum import Enum
import logging
from Preprocessing import CallGraphUtil

log = logging.getLogger(__name__)


class NeighborStrategy(Enum):
        both = 1 
        up = 2  # Only neighbors that call this method
        down = 3  # Only neighbors that are called by this method


class NeighborHandler:
    
    def __init__(self, neighbor_strategy, method_call_graph_dict):
        self._neighbor_strategy = neighbor_strategy
        self._method_call_graph_dict = method_call_graph_dict
    
    def get_neighbor_method_keys_of(self, method_key):
        method_callgraph_entry = self._method_call_graph_dict[method_key]
        all_neighbor_keys = None
        if self._neighbor_strategy == NeighborStrategy.both:
            all_neighbor_keys = method_callgraph_entry[CallGraphUtil.CALLED_BY] + method_callgraph_entry[CallGraphUtil.CALLS]
        elif self._neighbor_strategy == NeighborStrategy.up:
            all_neighbor_keys = method_callgraph_entry[CallGraphUtil.CALLED_BY]
        elif self._neighbor_strategy == self.NeighborStrategy.down:
            all_neighbor_keys = method_callgraph_entry[CallGraphUtil.CALLS]
        else:
            log.error("Unknown neighbor strategy: " + str(self._neighbor_strategy))
        return [elem[0] for elem in all_neighbor_keys]

