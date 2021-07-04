import logging

from utility.FileUtil import create_missing_dirs
import pandas as pd

log = logging.getLogger(__name__)

CSV_DELIMITER = ";"


def create_dataframe(row_names, column_names):
    return pd.DataFrame(None, index=row_names, columns=column_names)


def read_csv_to_dataframe_with_header(path, header):
    return pd.read_csv(path, delimiter=CSV_DELIMITER, header=0, encoding="utf8", names=header)


def read_csv_to_dataframe(path):
    # float_precision="round_trip" is needed otherwise pandas cuts off after the 16th decimal place
    return pd.read_csv(path, delimiter=CSV_DELIMITER, header=0, encoding="utf8", index_col=0, float_precision="round_trip")


def write_dataframe_to_csv(dataframe, output_filename):
    create_missing_dirs(output_filename)
    dataframe.to_csv(output_filename, sep=CSV_DELIMITER)
    log.info(f"Wrote csv file: {output_filename} ")
