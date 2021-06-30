from _functools import reduce
from collections import Counter
import copy
from datetime import datetime
import logging, numpy, torch, random, math

from sklearn.metrics.pairwise import cosine_similarity

log = logging.getLogger(__name__)


def calc_prec_recall_f1(true_positives: int, num_found_links: int, num_solution_links: int):
    if not num_found_links or not num_solution_links:
        return 0, 0, 0
    
    precision = true_positives / num_found_links
    recall = true_positives / num_solution_links
    f_1 = 0
    if (recall + precision) > 0:
        f_1 = 2 * precision * recall / (precision + recall)
    return precision, recall, f_1


def calculate_discrete_integral_average_precision(recall_prec_dict):
    """
    Input format:
    recall_prec_dict[recall] = precision
    """
    avg_prec = 0
    prev_recall = 0
    for rec in sorted(recall_prec_dict.keys()):  # sort by recall
        precision = recall_prec_dict[rec]
        avg_prec += (rec - prev_recall) * precision
        prev_recall = rec
    return avg_prec


def calculate_mean_average_precision_and_recall(req_dict, k, num_reqs, num_solution_links, reverse_compare=False):
    """
    req_dict["req_name"] = [(sim_to_code_1: float, relevant: bool), (sim_to_code_2, relevant), ...]
    """
    precision_sum = 0
    num_relevant_links = 0
    for req in req_dict:
        first_k_links = sorted(list(req_dict[req]), key=lambda sim_rel_tuple: sim_rel_tuple[0], reverse=not reverse_compare)  # most similar first
        if k is not None:
            first_k_links = first_k_links[:k]
            assert len(first_k_links) == k
        num_relevant_links += sum([1 if elem[1] else 0 for elem in first_k_links])
        precision_sum += calculate_query_average_precision(first_k_links, reverse_compare)[0]
        
    return  precision_sum / num_reqs, num_relevant_links / num_solution_links


def build_prec_recall_f1_print_str(precision, recall, f_1, true_positives, num_found_links):
    print_str = (f"Precision: {precision}\n"
                f"Recall: {recall}\n"
                f"F1: {f_1}\n"
                f"True Positives: {true_positives}\n"
                f"Total number of found trace links: {num_found_links}\n")
    return print_str


def sample_random_percent(data, p, seed):
    if p <= 0 or p > 1:
        log.error("Invalid percent: " + str(p))
        return None
    num_elems = math.ceil(p * len(data))
    if seed is not None:
        random.seed(seed)
    random.shuffle(data)
    return (data[:num_elems], data[num_elems:])


def map_value_range(min_value, max_value, value):
    """ normalize value to [0,1]"""
    assert max_value > min_value
    return (value - min_value) / (max_value - min_value)


def complement(percent):
    """ complement(0.85) = 0.15 """
    if percent <= 0 or percent > 1:
        log.error("Invalid percent: " + str(percent))
        return None
    return round(1 - percent, 2)


def create_averaged_vector(vectors):
    if not vectors:
        log.error("No vectors to average!")
        return None
    if len(vectors) == 1:
        return vectors[0]
    
    vector_sum = reduce(lambda a, b : a + b, vectors)
    return vector_sum / len(vectors)


def get_grouped_dict_list(dataframe, column_name_to_group, column_name_to_be_grouped) -> [dict]:
    g = dataframe.groupby(column_name_to_group)
    files_dict = dict()
    for name, group in g:
        files_dict[name] = group[column_name_to_be_grouped].tolist()
    return files_dict


def _init_colab_gpu():
    if torch.cuda.is_available(): 
        device = torch.device("cuda")
        print("Current device: " + torch.cuda.get_device_name(torch.cuda.current_device()))
        return device
    else:
        raise SystemError("GPU device not found")

    
def get_range_array(a, b, delta):
    """
    Returns an array of values between a and b (inclusive) with delta intervals
    Don't use this function if the delta is smaller than 0.00000000001
    """
    if  a == b:
        return [a]
    return [round(elem, 3) for elem in numpy.arange(a, b + 0.00000000001, delta)]


def transpose_eval_array(eval_array):
    return list(map(list, zip(*eval_array)))


def calculate_cos_sim(vector_1, vector_2):
    sim = cosine_similarity([vector_1], [vector_2])  # function expects 2D array
    sim = sim[0][0].item()  # unpack numpy 2D array and convert to python float
    # if sim < 0:
    #    print(sim)
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


def top_percent(num_files, percent):
    assert percent > 0 and percent <= 1, str(percent) + " is not in (0,1]"
    return int(round(percent * num_files))

