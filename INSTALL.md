# Install dependencies

The following steps describe the setup to run the code locally.
Alternatively, the code can be executed in Google Colab (recommended if hardware requirements are not met).
All steps are included in the provided [Jupyter Notebook](./finegrained-traceability.ipynb)

Run these lines one after another in your command line:

    pip install -U fasttext javalang XlsxWriter spacy
    python -m spacy download it_core_news_lg
    python -m spacy download en_core_web_trf
    python
    import nltk
    nltk.download("stopwords")
    nltk.download("punkt")
    nltk.download("wordnet")
    
* `fasttext` is the dependency for the fasttext word embedding library
* `javalang` is the java AST parser
* `XlsxWriter` is needed to create excel files (to write evaluation results)
* `spacy` is needed for its lemmatizer
* `nltk` is needed for its stopwords, tokenizer and lemmatizer/stemmer

In the paper, we used the spacy lemmatizer for both english and italian. It is possible to use the `nltk` lemmatizer instead by configuring the TraceabilityRunner to choose the appropriate `LemmatizerType` for its Lemmatizer and re-precalculate the files. However, this is not recommended since `nltk` doesn't provide an italian lemmatizer, but only a stemmer.

The fastText model files (for english and italian) are not included in this repository. You only need these if you wish to re-precalculate the similarity data (See [Precalculation phase](./README.md#Precalculation-phase)). The model files can be found on the [website of fastText](https://fasttext.cc/docs/en/crawl-vectors.html). For the paper we used `cc.en.300.bin` and `cc.it.300.bin`.

To check the correct setup, run [App.py](./App.py) or setup with the [Jupyter Notebook](./finegrained-traceability.ipynb).
