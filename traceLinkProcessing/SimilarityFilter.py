

class SimilarityFilter:
    

    def _bigger_is_more_sim(self, a, b):
        return a > b
    

    def _smaller_is_more_sim(self, a, b):
        return a < b
    
    def __init__(self, bigger_is_more_similar=True):
        if bigger_is_more_similar:
            self._compare_func = self._bigger_is_more_sim
        else:
            self._compare_func = self._smaller_is_more_sim
    

    
    def is_more_similar(self, data, drop_threshold):
        return self._compare_func(data, drop_threshold)
    
