import logging, csv, json, errno
import shutil, os, ntpath, xlsxwriter
import pandas as pd
from  xml.dom.minidom import parse
from pathlib import Path
from Paths import RECALL_PREC_CSV_HEADER, CSV_DELIM

log = logging.getLogger(__name__)


def write_recall_precision_csv(recall_prec_dict, output_file_name):
    rows = [RECALL_PREC_CSV_HEADER]
    for rec in sorted(recall_prec_dict):
        rows.append([rec, recall_prec_dict[rec]])
        
    write_rows_to_csv_file(output_file_name, rows)
        
def read_csv_to_list(path):
    with open(path, 'r') as file:
        reader = csv.reader(file, delimiter = CSV_DELIM)
        return list(reader)

def read_csv_to_dataframe_with_header(path, header):
    return pd.read_csv(path, delimiter=";", header=0, encoding="utf8", names=header)

def read_csv_to_dataframe(path):
    return pd.read_csv(path, delimiter=";", header=0, encoding="utf8", index_col=0)

def set_extension(file_path, extension):
    if "." in file_path:
        file_path =  file_path.rpartition(".")[0]
    return file_path + "." + extension

def write_dataframe_to_csv(dataframe, output_filename):
    create_missing_dirs(output_filename)
    dataframe.to_csv(output_filename, sep = ";")
    log.info("Wrote csv file: " + str(output_filename))

def read_textfile_into_string(file_path, encoding='utf-8-sig'):
    text_as_string = ""
    log.debug("Attempt to read file: " + str(file_path))
    try:
        file = open(file_path, 'r', encoding=encoding)
        text_as_string = file.read()
    except IOError:
        log.error("Unable to read " + str(file_path))
        
    return text_as_string

def read_textfile_into_lines_list(file_path, encoding='utf-8-sig'):
    text_lines = ""
    log.debug("Attempt to read file: " + str(file_path))
    try:
        file = open(file_path, 'r', encoding=encoding)
        text_lines = file.readlines()
    except IOError:
        log.error("Unable to read " + str(file_path))
        
    return text_lines

def combine_csv_files(csv_filenames, output_file):
    combined_csv = read_csv_to_list(csv_filenames[0])
    for i in range(1, len(csv_filenames)):
        next_csv = read_csv_to_list(csv_filenames[i])[1:] # Omit header
        combined_csv += next_csv
    write_rows_to_csv_file(output_file, combined_csv)
    return combined_csv


def write_dict_to_json(file_path, content):
    create_missing_dirs(file_path)
    
    def set_default(obj):# convert sets to lists
        if isinstance(obj, set):
            return list(obj)
        raise TypeError
    
    with open(str(file_path), 'w') as file:
        json.dump(content, file, default=set_default)
    log.info("Wrote json file: " + str(file_path))
    
def read_dict_from_json(file_path):
    if not file_exists(file_path):
        log.error("File does not exist: " + str(file_path))
    with open(file_path, 'r') as file:
        return json.load(file)

def write_eval_to_excel(data, path):
    workbook = xlsxwriter.Workbook(path)
    worksheet = workbook.add_worksheet()
    worksheet.set_column(0, len(data[1]) - 1, 35)
    wrap_format = workbook.add_format({'text_wrap': True})
    for i, _ in enumerate(data):
        for j, _ in enumerate(data[i]):
            worksheet.write(i, j, data[i][j], wrap_format)
         
    workbook.close()
    log.info("Wrote excel file: " + str(path))
            
def write_file(file_path, content):
    create_missing_dirs(file_path)
    with open(file_path, "w+") as file:
        file.write(str(content))
    log.debug("Wrote file: " + str(file_path))

def write_rows_to_csv_file(file_path, rows: [[str]]):
    create_missing_dirs(file_path)
    with open(file_path, mode='w') as file:
        writer = csv.writer(file, delimiter=CSV_DELIM, quotechar="\"", quoting=csv.QUOTE_MINIMAL, lineterminator="\n")
        for row in rows:
            writer.writerow(row)
    log.info("Wrote csv file: " + str(file_path))
            

def setup_clear_dir(directory):
    if not os.path.exists(directory):
        os.mkdir(directory)
    else:
        shutil.rmtree(directory)
        os.mkdir(directory)
        
def file_exists(path):
    return os.path.isfile(path)

def create_missing_dirs(filepath):
    dir = os.path.dirname(os.path.abspath(filepath))
    try:
        os.makedirs(dir)
    except OSError as exc: # Python >2.5
        if exc.errno == errno.EEXIST and os.path.isdir(dir):
            pass
        else: raise
def get_files_in_directory(directory: Path, return_as_pathstrings=True):
    """
    Returns all files in the directory, including subdirectories
    
    return_as_pathstrings=True: Returns the file paths as strings
    return_as_pathstrings=False: Returns the file paths as pathlib.Path objects
    """
    if isinstance(directory, str):
        directory = Path(directory)
    if not os.path.exists(directory):
            log.error("{} is not a valid directory".format(directory))
            return []
    elif os.path.isfile(directory):
        if return_as_pathstrings:
            return [str(directory)]
        return [directory]
    elif os.path.isdir(directory):
        files = []
        for filename in os.listdir(directory):
            files += get_files_in_directory(directory / filename)
        return files
    else:
        log.error("Unable to process " + str(directory))
        return []
    
def get_filename_from_path(file_path):
    """ a/b/c/x.txt -> x.txt"""
    return ntpath.basename(file_path)

def get_filename_without_extension__from_path(file_path):
    """ a/b/c/x.txt -> x"""
    if isinstance(file_path, str):
        return Path(file_path).stem
    elif isinstance(file_path, Path):
        return file_path.stem
    else:
        log.error(file_path + " is neither a string nor a Path")
        return None
    
def count_files(directory):
    return len([name for name in os.listdir(directory) if os.path.isfile(directory / name)])
    
def unique_filenames(path, with_ext=False):
    files = get_files_in_directory(path, True)
    if with_ext:
        files = [get_filename_from_path(f) for f in files]
    else:
        files = [get_filename_without_extension__from_path(f) for f in files]
    files = set(files)
    print(str(len(files)) + " unique files")
    return files