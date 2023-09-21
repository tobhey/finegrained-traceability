# Hardware Requirements
Approximately 12GB RAM is required to load a fastText model. This is only necessary for the execution of the precalculation phase.

# Software Requirements
Python 3.9 or equal with following packages:
```
pip install fasttext~=0.9.2 javalang~=0.13.0 pycparser~=2.21 comment-parser~=1.2.4 esprima~=4.0.1 XlsxWriter~=3.0.1 spacy~=3.1.1 pydantic~=1.8.2 typing-extensions~=4.2.0 nltk~=3.2.5 numpy~=1.22.3 sklearn~=0.0 scikit-learn~=1.1.1 pandas~=1.1.5 joblib~=1.1.0 autograd~=1.3 torch~=1.13.1 transformers~=4.26.1 scipy~=1.8.1 pyemd~=0.5.1 gensim~=3.6.0
python -m spacy download it_core_news_lg
python -m spacy download en_core_web_lg
python -m nltk.downloader stopwords
python -m nltk.downloader punkt
python -m nltk.downloader wordnet
```
