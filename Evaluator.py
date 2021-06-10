

class Evaluator:
    
    def __init__(self, solution_comparator, eval_strategy):
        self._solution_comparator = solution_comparator
        self._eval_strategy = eval_strategy
        
        
class FileLevelEvaluator(Evaluator):
    pass


class MajorityEvaluator(Evaluator):
    pass
