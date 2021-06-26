from collections import Counter
import logging, copy, collections
import random

import FileUtil

log = logging.getLogger(__name__)
log.setLevel(logging.INFO)


class SolutionTraceMatrix:
    
    def __init__(self, req_ext=None, code_ext=None):
        self._dictionary = dict()  # Requirement file name as key, corrresponding classes as value
        self._number_of_trace_links = 0
        self._code_count_dict = {}  # Counts number of occurences of a code file in self._dictionary 
        self._req_ext = req_ext
        self._code_ext = code_ext
        
    def number_of_trace_links(self):
        return self._number_of_trace_links
        
    def add_trace_pair(self, req_key: str, code_value: str):
        req_key = self._convert_req_key(req_key)
        code_value = self._convert_code_key(code_value)
        if req_key in self._dictionary:
            if code_value not in self._dictionary[req_key]:
                self._dictionary[req_key].append(code_value)
                if code_value in self._code_count_dict: 
                    self._code_count_dict[code_value] += 1
                else: 
                    self._code_count_dict[code_value] = 1
                self._number_of_trace_links += 1
        else:
            self._dictionary[req_key] = [code_value]
            if code_value in self._code_count_dict: 
                self._code_count_dict[code_value] += 1
            else: 
                self._code_count_dict[code_value] = 1
            self._number_of_trace_links += 1
            
    def _convert_req_key(self, req_key):
        if self._req_ext is not None:
            return set_extension(req_key, self._req_ext)
        return req_key
    
    def _convert_code_key(self, code_key):
        if self._code_ext is not None:
            return set_extension(code_key, self._code_ext)
        return code_key
    
    def remove_trace_pair(self, req_key: str, code_value: str):
        req_key = self._convert_req_key(req_key)
        code_value = self._convert_code_key(code_value)
        if req_key in self._dictionary:
            if code_value in self._dictionary[req_key]:
                self._dictionary[req_key].remove(code_value)
                self._number_of_trace_links -= 1
                if not self._dictionary[req_key]:
                    del self._dictionary[req_key]
                self._code_count_dict[code_value] -= 1
                if self._code_count_dict[code_value] == 0:
                    del self._code_count_dict[code_value]
                return
        log.info("Req/code key not in dict for removal: " + req_key + "/" + code_value)
        
    def num_unique_reqs(self):
        return len(self._dictionary.keys())
    
    def num_unique_code(self):
        return len(self._code_count_dict.keys())
    
    def unique_reqs(self):
        return self._dictionary.keys()
    
    def unique_code(self):
        return self._code_count_dict.keys()
    
    def allows_duplicates(self):
        return False
    
    def contains_req_code_pair(self, req_key: str, code_value: str) -> bool:
        req_key = self._convert_req_key(req_key)
        code_value = self._convert_code_key(code_value)
        if req_key.lower() not in [key.lower() for key in self._dictionary]:
            log.debug(str(req_key) + " is not in the trace matrix")
            return False
        elif code_value.lower() in [key.lower() for key in self._dictionary[req_key]]:
            log.debug(str(req_key) + "<->" + str(code_value) + " is in the trace matrix!")
            return True
        else:
            log.debug(str(code_value) + " is not a trace link to " + str(req_key))
            return False

    def get_req_to_code_key(self, code_key) -> str: 
        code_key = self._convert_code_key(code_key)
        solution_reqs = []
        for req_key in self._dictionary:
            if code_key in self._dictionary[req_key]:
                solution_reqs.append(req_key)
        if solution_reqs:
            return ", ".join(solution_reqs)
        return "No matching reqs to " + code_key
    
    def get_all_trace_links(self):
        all_trace_links = []
        for req in self._dictionary:
            for cls in self._dictionary[req]:
                all_trace_links.append((req, cls))

        assert len(all_trace_links) == self._number_of_trace_links
        return all_trace_links
    
    def write_trace_matrix(self, output_file):
        with open(output_file, "w+") as file:
            file.write(str(self.print_str()))
        log.debug("Wrote file: " + str(output_file))
        
    def get_random_pair(self):
        req = random.choice(list(self._dictionary.keys()))
        return req, random.choice(self._dictionary[req])
                     
    def print_str(self):
        all_trace_links_string = []
        for req_name in sorted(self._dictionary.keys()):
            all_trace_links_string.append(req_name + ":" + " ".join([class_name for class_name in self._dictionary[req_name]]))
        return "\n".join(all_trace_links_string)

    def is_same(self, other_solution_matrix):
        if self._number_of_trace_links != other_solution_matrix._number_of_trace_links:
            return False
        for req_key, code_key in self.get_all_trace_links():
            if not other_solution_matrix.contains_req_code_pair(req_key, code_key):
                return False
        return True
    
    def __repr__(self):
        return self.print_str()
    
    def __hash__(self):
        return hash(self.__repr__())
    
    def difference_ignoring_duplicates(self, other_solution_matrix):
        """ number of trace links that is contained in this solution matrix but not in the other one """
        my_trace_links = set(self.get_all_trace_links())
        return [x for x in my_trace_links if not other_solution_matrix.contains_req_code_pair(x[0], x[1])]
    
    def print_links_statistic(self, all_reqs_file_names, all_codes_file_names):
        original_size_req, original_size_code = len(all_reqs_file_names), len(all_codes_file_names)
        req_frequency, code_frequency = [], []
        for req, code in self.get_all_trace_links():
            req_frequency += [req]
            code_frequency += [code]
        req_frequency = Counter(req_frequency)
        code_frequency = Counter(code_frequency)
        for req in sorted(req_frequency.keys()):
            print(f"{req}: {req_frequency[req]}")
            if req in all_reqs_file_names: all_reqs_file_names.remove(req)
        print("-----------------------------------")
        for code in sorted(code_frequency.keys()):
            print(f"{code}: {code_frequency[code]}")
            if code in all_codes_file_names: all_codes_file_names.remove(code)
        print("-----------------------------------")
        print(f"Not in solution: {len(all_reqs_file_names)} of {original_size_req} reqs, {len(all_codes_file_names)} of {original_size_code} code files")
        if all_reqs_file_names: print("\n" + "\n".join(all_reqs_file_names))
        if all_codes_file_names: print("\n" + "\n".join(all_codes_file_names))

        
class SolutionTraceMatrixWithDuplicates(SolutionTraceMatrix):
    
    @classmethod
    def convert_from_solution_matrix(cls, sol_matrix):
        """Convert from a Sol matrix without dup to one with duplicates"""
        sol_matrix_with_dups = cls()
        sol_matrix_with_dups._dictionary = sol_matrix._dictionary
        sol_matrix_with_dups._number_of_trace_links = sol_matrix._number_of_trace_links
        sol_matrix_with_dups._code_count_dict = sol_matrix._code_count_dict
        return sol_matrix_with_dups
    
    def add_trace_pair(self, req_key: str, code_value: str):
        req_key = self._convert_req_key(req_key)
        code_value = self._convert_code_key(code_value)
        if req_key in self._dictionary:
            self._dictionary[req_key].append(code_value)
            if code_value in self._code_count_dict: 
                self._code_count_dict[code_value] += 1
            else: 
                self._code_count_dict[code_value] = 1
            self._number_of_trace_links += 1
        else:
            self._dictionary[req_key] = [code_value]
            if code_value in self._code_count_dict: 
                self._code_count_dict[code_value] += 1
            else: 
                self._code_count_dict[code_value] = 1
            self._number_of_trace_links += 1
            
    def allows_duplicates(self):
        return True
    
    def is_same(self, other_solution_matrix):
        return collections.Counter(self.get_all_trace_links()) == collections.Counter(other_solution_matrix.get_all_trace_links())

    
def set_extension(file_path, extension):
    if "." in file_path:
        file_path = file_path.rpartition(".")[0]
    return file_path + "." + extension
