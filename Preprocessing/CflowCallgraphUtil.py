from Dataset import Dataset, Libest
from Preprocessing.CallGraphUtil import CALLS, CALLED_BY, CLASS_NAME,\
    METHOD_NAME, PARAMS, build_class_method_param_dict_key,\
    build_method_param_dict_key

import logging
import FileUtil

log = logging.getLogger(__name__)

def read_raw_callgraph_txt(dataset: Dataset):
    raw_txt_path = dataset.raw_call_graph_path()
    output_method_callgraph = dataset.method_callgraph_path()
    text_rows = []
    try:
        file = open(raw_txt_path, 'r', encoding='utf8')
        text_rows = file.readlines()
    except IOError:
        log.error("Unable to read " + str(raw_txt_path))
        
    method_call_graph = dict()
    
    def insert_entry(dict_key, class_name, method_name, param_list, called_by=set(), calls=set()):
        if dict_key in method_call_graph:
            method_call_graph[dict_key][CALLS] |= calls
            method_call_graph[dict_key][CALLED_BY] |= called_by
            if class_name and method_call_graph[dict_key][CLASS_NAME]:
                log.info(f"Replacing {method_call_graph[dict_key][CLASS_NAME]} with {class_name} for {dict_key}")
                method_call_graph[dict_key][CLASS_NAME] = class_name
        else:
            method_dict = dict()
            method_dict[CALLS] = calls
            method_dict[CALLED_BY] = called_by
            method_dict[CLASS_NAME] = class_name
            method_dict[METHOD_NAME] = method_name
            method_dict[PARAMS] = param_list
                    
            method_call_graph[dict_key] = method_dict
    def insert_callee(dict_key, callee):
        assert dict_key in method_call_graph, dict_key
        method_call_graph[dict_key][CALLS] |= callee
           
    def remove_calls_with_no_classname():
        for dict_key in list(method_call_graph.keys()):
            if not method_call_graph[dict_key][CLASS_NAME]:
                del  method_call_graph[dict_key]
                
    def remove_external_calls():
        for dict_key in list(method_call_graph.keys()):
            method_call_graph[dict_key][CALLS] = [callee for callee in method_call_graph[dict_key][CALLS] if callee[0] in method_call_graph]
            method_call_graph[dict_key][CALLED_BY] = [caller for caller in method_call_graph[dict_key][CALLED_BY] if caller[0] in method_call_graph]
            
            if len(method_call_graph[dict_key][CALLED_BY]) == 0  and len(method_call_graph[dict_key][CALLS]) == 0:
                del  method_call_graph[dict_key]
            

    last_parent_dict = {}
    for row in text_rows:
        row_no_indent = row.lstrip() # remove indentation
        indentation = len(row) - len(row_no_indent)
        indentation_normalized = indentation / 4
        key, file_name, method_name = _extract_name_key(row_no_indent)
        called_by = set()
        if indentation == 0:
            last_parent_dict = {}
            #assert row_no_indent.endswith(":\n"), row_no_indent # top level row must be a caller
            assert "<" in row_no_indent and ">" in row_no_indent, row_no_indent # contains method signature
        elif indentation_normalized > 0: # add me as callee
            caller = (last_parent_dict[indentation_normalized - 1], "")
            insert_callee(caller[0], set([(key, "")]))
            called_by = set([caller])
        insert_entry(key, file_name, method_name, [], called_by, set())
        last_parent_dict[indentation_normalized] = key
        
    remove_calls_with_no_classname()
    remove_external_calls()
    for entry in method_call_graph:
        method_call_graph[entry][CALLS] = list(method_call_graph[entry][CALLS])
        method_call_graph[entry][CALLED_BY] = list(method_call_graph[entry][CALLED_BY])
           
    FileUtil.write_dict_to_json(output_method_callgraph, method_call_graph)
            
            
            

def _extract_name_key(row_no_indent):
    method_name = row_no_indent[0:row_no_indent.find("(")]
    file_name = None
    if ">" in row_no_indent:
        file_name = row_no_indent[row_no_indent.find(" at ") + 4:row_no_indent.find(":")]
    key = build_method_param_dict_key(method_name, [])
    
    return key, file_name, method_name
    
#read_raw_callgraph_txt(Libest())