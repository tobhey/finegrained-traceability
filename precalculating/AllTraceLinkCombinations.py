"""
Use load_data_from() to load a existing precalculated file or an AllTraceLinkCombinationsBuilder to create from embeddings
"""

from abc import abstractmethod
import abc

from TwoDimensionalMatrix import TwoDimensionalMatrix


class AllTraceLinkCombinations(abc.ABC):
    CSV_FILE_PATTERN = "{folder}/{filename}.csv"
    
    @abstractmethod
    @classmethod
    def load_data_from(cls, folder, file_name_without_extension):
        pass
    
    @abstractmethod
    def write_data(self, folder, file_name_without_extension):
        pass
    
    @abstractmethod
    def all_req_file_names(self):
        pass

    @abstractmethod
    def all_code_file_names(self):
        pass
    
    @abstractmethod
    def similarity_between(self, req, code):
        """
        req and code can be files or file elements (depending on sub class)
        """
        pass
    
    @classmethod
    def _build_csv_file_name(cls, folder, file_name_without_extension):
        return cls.CSV_FILE_PATTERN.format(folder=str(folder), filename=file_name_without_extension)
    
    
class AllFileLevelTraceLinkCombinations(AllTraceLinkCombinations):
    
    def __init__(self, similarity_matrix):
        """
        Don't use the constructor to instantiate
        
        Rows = req file names; columns = code file names; entry = similarity
        """
        self._similarity_matrix = similarity_matrix
    
    @classmethod
    def load_data_from(cls, folder, file_name_without_extension):
        instance = cls()
        instance._similarity_matrix = TwoDimensionalMatrix.read_from_csv(cls._build_csv_file_name(folder, file_name_without_extension))
        return instance
    
    def write_data(self, folder, file_name_without_extension):
        pass
    
    def all_req_file_names(self):
        return self._similarity_matrix.get_row_names()

    def all_code_file_names(self):
        return self._similarity_matrix.get_column_names()
    
    def similarity_between(self, req_file_name, code_file_name):
        return self._similarity_matrix.get_value(req_file_name, code_file_name)
