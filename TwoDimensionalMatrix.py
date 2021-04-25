import PandasUtil

class TwoDimensionalMatrix:
    
    def __init__(self):
        self._dataframe = None
        
    def set_value(self, row_name, column_name, value):
        self._dataframe.at[row_name, column_name] = value
        
    def get_value(self, row_name, column_name):
        return self._dataframe.at[row_name, column_name]
    
    def write_to_csv(self, output_csv_path):
        PandasUtil.write_dataframe_to_csv(self._dataframe, output_csv_path)
        
    @classmethod
    def create_empty(cls, row_names, column_names):
        instance = cls()
        instance._dataframe = PandasUtil.create_dataframe(row_names, column_names)
        return instance
    
    @classmethod
    def read_from_csv(cls, csv_path):
        instance = cls()
        instance._dataframe = PandasUtil.read_csv_to_dataframe(csv_path)
        return instance