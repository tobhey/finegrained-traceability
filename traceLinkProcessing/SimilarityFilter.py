

class SimilarityFilter:
    
    def __init__(self, bigger_is_more_similar=True):
        if bigger_is_more_similar:
            self._compare_func = lambda a, b : a > b
        else:
            self._compare_func = lambda a, b : a < b
    
    def is_more_similar(self, data, drop_threshold):
        return self._compare_func(data, drop_threshold)
    
