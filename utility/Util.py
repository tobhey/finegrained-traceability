from _functools import reduce
from collections import Counter
from datetime import datetime
import copy
import logging, numpy

from sklearn.metrics.pairwise import cosine_similarity

log = logging.getLogger(__name__)


def map_value_range(min_value, max_value, value):
    """ normalize value to [0,1]"""
    assert max_value > min_value
    return (value - min_value) / (max_value - min_value)


def create_averaged_vector(vectors):
    if not vectors:
        log.error("No vectors to average!")
        return None
    if len(vectors) == 1:
        return vectors[0]
    
    vector_sum = reduce(lambda a, b : a + b, vectors)
    return vector_sum / len(vectors)


def get_range_array(a, b, delta):
    """
    Returns an array of values between a and b (inclusive) with delta intervals
    Don't use this function if the delta is smaller than 0.00000000001
    """
    if  a == b:
        return [a]
    return [round(elem, 3) for elem in numpy.arange(a, b + 0.00000000001, delta)]


def calculate_cos_sim(vector_1, vector_2):
    sim = cosine_similarity([vector_1], [vector_2])  # function expects 2D array
    sim = sim[0][0].item()  # unpack numpy 2D array and convert to python float
    return sim


def majority_count(vote_array):
    majority_ranked_list = Counter(vote_array)
    max_vote_count = majority_ranked_list.most_common(1)[0][1]
    return majority_ranked_list, max_vote_count


def log_curr_time():
    log.info(curr_time())

    
def curr_time():
    return datetime.now().strftime("%H:%M:%S Uhr")


def deep_copy(obj):
    return copy.deepcopy(obj)


def random_numpy_array(low, high, dim):
    return numpy.random.default_rng().uniform(low, high, dim)


def numpy_array(l):
    return numpy.array(l)

