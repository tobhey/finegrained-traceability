import abc
from abc import ABC

import pandas as pd

from datasets.Dataset import Dataset
from precalculating.TraceLinkDataStructure import ElementLevelTraceLinkDataStructure
from utility import FileUtil


class ElementFilter(ABC):

    def filter(self, trace_link_data_structure: ElementLevelTraceLinkDataStructure, dataset: Dataset):
        if dataset.classification_file_path() is not None and FileUtil.file_exists(dataset.classification_file_path()):
            df = pd.read_csv(dataset.classification_file_path(), delimiter=',', encoding='utf8', header=0,
                             names=['file', 'ID', 'line', 'functional', 'Function', 'Behavior', 'Data', 'OnlyF', 'F',
                                    'OnlyQ', 'Q', 'UserRelated'])
            for file in df.file.unique():
                idxs = df.index[df['file'] == file].tolist()
                for i in idxs:
                        trace_link_data_structure = self._filter(trace_link_data_structure, df, file, i)
        return trace_link_data_structure

    @abc.abstractmethod
    def _filter(self, trace_link_data_structure: ElementLevelTraceLinkDataStructure, df, file, idx):
        pass

    @staticmethod
    def check_and_remove(trace_link_data_structure: ElementLevelTraceLinkDataStructure, df, file, idx, columns):
        result = False
        for column,value in columns:
            result = result or (df.loc[idx, column] == value)
        if result:
            trace_link_data_structure.remove_req_file_element(file, df.loc[idx].ID)
        return trace_link_data_structure


class NFRElementFilter(ElementFilter):
    def _filter(self, trace_link_data_structure: ElementLevelTraceLinkDataStructure, df, file, idx):
        columns = [("F", '0'), ("F", 0)]
        return self.check_and_remove(trace_link_data_structure,df,file,idx,columns)


class UserRelatedElementFilter(ElementFilter):
    def _filter(self, trace_link_data_structure: ElementLevelTraceLinkDataStructure, df, file, idx):
        columns = [("UserRelated", '1'), ("UserRelated", 1)]
        return self.check_and_remove(trace_link_data_structure, df, file, idx, columns)



class UserRelatedNFRElementFilter(ElementFilter):
    def _filter(self, trace_link_data_structure: ElementLevelTraceLinkDataStructure, df, file, idx):
        nfr = NFRElementFilter()
        ur = UserRelatedElementFilter()
        tds = nfr._filter(trace_link_data_structure, df, file, idx)
        return ur._filter(tds, df, file, idx)
