from collections import Counter
import logging

log = logging.getLogger(__name__)
log.setLevel(logging.INFO)


class SolutionMatrix:
    
    def __init__(self):
        self._dictionary = dict()  # Requirement file name as key, corrresponding classes as value
        self._number_of_trace_links = 0
        self._code_count_dict = {}  # Counts number of occurences of a code file in self._dictionary 
        
    def number_of_trace_links(self):
        return self._number_of_trace_links
        
    def add_trace_pair(self, req_key: str, code_value: str):
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
    
    def remove_trace_pair(self, req_key: str, code_value: str):
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
    
    def contains_req_code_pair(self, req_key: str, code_value: str) -> bool:
        if req_key in self._dictionary and code_value in self._dictionary[req_key]:
            log.debug(f"{req_key} <-> {code_value} is in the trace matrix!")
            return True
        log.debug(f"{code_value} is not a trace link to {req_key}")
        return False
    
    def get_all_trace_links(self) -> [(str, str)]:
        """
        Returns alle trace links as tuple list [(req name, code file name), ...]
        """
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
        
    def print_str(self):
        all_trace_links_string = []
        for req_name, code_name in sorted(self.get_all_trace_links(), key=lambda tup: tup[0]):
            all_trace_links_string.append(f"{req_name}: {code_name}")
        return "\n".join(all_trace_links_string)

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
   
