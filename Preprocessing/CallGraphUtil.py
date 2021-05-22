"""
Contains util functions to work with the java call graph parser
    https://github.com/gousiosg/java-callgraph

"""

import logging, re

from Dataset import Dataset, Etour, Itrust, EANCI
import FileUtil
from Paths import *

log = logging.getLogger(__name__)

PARAMS = "params"
CALLED_BY = "called_by"
CALLS = "calls"
METHOD_NAME = "method_name"
CLASS_NAME = "class_name"


def read_raw_callgraph_txt(dataset: Dataset):
    """
    Extract class and method call graph from a raw call graph file generated by Georgios Gousios' java call graph tool
    Saves the call graphs as json files
    
    resulting class call graph:
    dict["classname"] = dict{
                            called_by=[str]
                            calls=[str]
                        }
    
    resulting method call graph:
    
    dict["classname.methodname(paramtyp1,paramtyp2)"] = dict{
                                        called_by=[[classname.methodname(paramtyp1,paramtyp2), calltype],[...]]
                                        calls=[[classname.methodname(paramtyp1,paramtyp2), calltype],[...]]
                                        class_name=str
                                        method_name=str
                                        params=[str]
                                        }
    }
        
    """
    raw_txt_path = dataset.raw_call_graph_path()
    output_class_callgraph = dataset.class_callgraph_path()
    output_method_callgraph = dataset.method_callgraph_path()
    text_rows = []
    try:
        file = open(raw_txt_path, 'r', encoding='utf8')
        text_rows = file.readlines()
    except IOError:
        log.error("Unable to read " + str(raw_txt_path))
        
    class_call_graph = dict()
    method_call_graph = dict()
    
    def insert_class(class_name, calls=set(), called_by=set()):
                
                if class_name in class_call_graph:
                    class_call_graph[class_name][CALLS] |= calls
                    class_call_graph[class_name][CALLED_BY] |= called_by
                else:
                    class_ref = dict()
                    class_ref[CALLED_BY] = called_by
                    class_ref[CALLS] = calls
                    class_call_graph[class_name] = class_ref
                    
    def insert_entry(dict_key, class_name, method_name, param_list, called_by=set(), calls=set()):
                if dict_key in method_call_graph:
                    method_call_graph[dict_key][CALLS] |= calls
                    method_call_graph[dict_key][CALLED_BY] |= called_by
                else:
                    method_dict = dict()
                    method_dict[CALLS] = calls
                    method_dict[CALLED_BY] = called_by
                    method_dict[CLASS_NAME] = class_name
                    method_dict[METHOD_NAME] = method_name
                    method_dict[PARAMS] = param_list
                    
                    method_call_graph[dict_key] = method_dict
                    
    def remove_external_calls():
        for dict_key in method_call_graph:
            method_call_graph[dict_key][CALLS] = [callee for callee in method_call_graph[dict_key][CALLS] if callee[0] in method_call_graph]
            method_call_graph[dict_key][CALLED_BY] = [caller for caller in method_call_graph[dict_key][CALLED_BY] if caller[0] in method_call_graph]
                    
    for row in text_rows:
        row_split = row.split(":")
        if row_split[0] == "C":  # Class level call
            classes = row_split[1].split(" ")
            class_1 = _clean(classes[0])
            class_2 = _clean(classes[1])
            if _is_external_class(dataset, class_1) or _is_external_class(dataset, class_2):
                continue
            caller_class_name = _extract_name(classes[0])
            callee_class_name = _extract_name(classes[1].replace('\r', '').replace('\n', ''))
            if caller_class_name == callee_class_name:
                continue
            if "$" in caller_class_name or "$" in callee_class_name:
                continue  # Leave out inner classes
            
            insert_class(caller_class_name, set([callee_class_name]), set())
            insert_class(callee_class_name, set(), set([caller_class_name]))
                    
        elif row_split[0] == "M":  # method level call
            # row_split[1] = Class of caller method
            # row_split[2] = caller method<whitespace>calltype and class of callee method
            # row_split[3] = callee method
            
            split_2 = row_split[2].split(" ")
            split_3 = split_2[1].split(")")
            if _is_external_class(dataset, row_split[1]) or _is_external_class(dataset, split_3[1]):
                continue
            caller_method = split_2[0]
            callee_method = row_split[3]
            if _is_constructor(caller_method) or _is_constructor(callee_method):
                continue
            if _is_access(caller_method) or _is_access(callee_method):
                continue
            caller_class = _extract_name(row_split[1])
            callee_class = _extract_name(split_3[1])
            if "$" in caller_class or "$" in callee_class:
                continue  # Leave out references to inner classes
            call_type = split_3[0][1]
            split_4 = caller_method.split("(")
            caller_name = split_4[0]
            caller_param = []
            if not split_4[1].startswith(")"):  # params existing
                caller_param = _split_param(split_4[1][:-1])  # Leave out last character, which is a )
            
            split_5 = callee_method.split("(")
            callee_name = split_5[0]
            callee_param = []
            if not split_5[1].startswith(")"):  # params existing
                callee_param = _split_param(split_5[1].replace('\r', '').replace('\n', '')[:-1])  # Leave out last character, which is )
            
            caller_dict_key = build_class_method_param_dict_key(caller_class, caller_name, caller_param)
            callee_dict_key = build_class_method_param_dict_key(callee_class, callee_name, callee_param)
            called_by = (caller_dict_key, call_type)
            calls = (callee_dict_key, call_type)
            
            insert_entry(caller_dict_key, caller_class, caller_name, caller_param, set(), set([calls]))
            insert_entry(callee_dict_key, callee_class, callee_name, callee_param, set([called_by]), set())
             
        else:
            log.error("Unknow start character: " + row_split[0])
            
    remove_external_calls()
    # convert all sets to lists since set is not json serializable
    for entry in class_call_graph:
        class_call_graph[entry][CALLS] = list(class_call_graph[entry][CALLS])
        class_call_graph[entry][CALLED_BY] = list(class_call_graph[entry][CALLED_BY])
        
    for entry in method_call_graph:
        method_call_graph[entry][CALLS] = list(method_call_graph[entry][CALLS])
        method_call_graph[entry][CALLED_BY] = list(method_call_graph[entry][CALLED_BY])
           
    FileUtil.write_to_json(output_class_callgraph, class_call_graph)
    FileUtil.write_to_json(output_method_callgraph, method_call_graph)

            
def _is_external_class(dataset, fully_qualified_classname):
    starts = dataset.packages()
    for elem in starts:
        if fully_qualified_classname.startswith(elem):
            return False
    return True


def _is_constructor(method):
    return "<init>" == method[:6]

        
def _is_access(method):
    if "access$0" in method or "$SWITCH_TABLE$" in method:
        return True
    return False


def _extract_name(fully_qualified_name):
    class_name = fully_qualified_name.split(".")[-1]
    class_name = re.sub("\$[0-9]", "", class_name)  # Delete $<number> references generated by the java call graph
    return class_name


def build_class_method_param_dict_key(class_name, method_name, param_type_list):
    return class_name + "." + build_method_param_dict_key(method_name, param_type_list)


def build_method_param_dict_key(method_name, param_type_list):
    param_string = "(" + ",".join(param_type_list) + ")"
    return method_name + param_string


def build_class_method_param_dict_key2(class_name, method_param_dict_key):
    return class_name + "." + method_param_dict_key

    
def _clean(class_name):
    return re.sub("^\[L", "", class_name)  # removes weird prefix generated by the java callgraph generator


def _split_param(params):
    param_list = [_extract_name(elem) for elem in params.split(",")]
    result_param_list = []
    for param in param_list:
        if "$" in param_list:  # param type is a nested type
            result_param_list += [param.split("$")[-1]]  # only use the nested type name
        else:
            result_param_list += [param]
    return result_param_list

# read_raw_callgraph_txt(EANCINoTrans())
