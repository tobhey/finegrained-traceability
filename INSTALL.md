# Install dependencies

The following steps describe the setup to run the code locally.

Run these lines one after another in your command line:
```
pip install fasttext~=0.9.2 javalang~=0.13.0 pycparser~=2.21 esprima~=4.0.1 XlsxWriter~=3.0.1 spacy~=3.1.1 nltk~=3.2.5 numpy~=1.22.3 sklearn~=0.0 scikit-learn~=1.1.1 pandas~=1.1.5 joblib~=1.1.0 autograd~=1.3 torch~=1.13.1 transformers~=4.26.1 scipy~=1.8.1 pyemd~=0.5.1 gensim~=3.6.0
python -m spacy download it_core_news_lg
python -m spacy download en_core_web_lg
python -m nltk.downloader stopwords
python -m nltk.downloader punkt
python -m nltk.downloader wordnet
```    
* `fasttext` is the dependency for the fasttext word embedding library
* `javalang` is the java AST parser
* `pycparser` is the C AST parser
* `esprima` is used for JSP parsing
* `XlsxWriter` is needed to create excel files (to write evaluation results)
* `spacy` is needed for its lemmatizer
* `nltk` is needed for its stopwords, tokenizer and lemmatizer/stemmer
* the others are common scientific python libs that are compatible with the before mentioned

The fastText model files (for english and italian) are not included in this repository. The model files can be found on the [website of fastText](https://fasttext.cc/docs/en/crawl-vectors.html). For the paper we used `cc.en.300.bin` and `cc.it.300.bin`.
