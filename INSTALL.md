# Install dependencies
Run these lines one after another in your command line:

    pip install fasttext javalang XlsxWriter spacy
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

In the paper, we used the spacy lemmatizer for both english and italian. It is possible to use the `nltk` lemmatizer instead by setting the appropriate `LemmatizerType` in the [Lemmatizer class](./preprocessing/Preprocessor.py). However, this is not recommended since `nltk` doesn't provide an italian lemmatizer, but a stemmer.

The fastText model files (for english and italian) are not included in this repository. You only need these if you wish to re-precalculate the similarity data (See [Precalculation phase](#Precalculation-phase)). The model files can be found on the [website of fastText](https://fasttext.cc/docs/en/crawl-vectors.html). For the paper we used `cc.en.300.bin` and `cc.it.300.bin`.

# Running the code

Since instantiating the fastText model and the spacy lemmatizer is expensive, the trace link recovery process is split into two phases: precalculation and trace link processing:

![Simplified architecture](./diagrams/simplified_architecture.svg)

## Precalculation phase
The precalculation phase consists of the following step:
1. Choose which elements of the requirement artifacts (sentences or use case template parts) and code artifacts (method signature, class name with/without method comments) are used to calculate the similarity (word mover's distance)
2. Calculate the similarity between all possible combinations of the chosen requirement and code artifacts
3. Save the resulting similarity matrix in a `*_matrix.csv` and the mapping between requirement/code artifacts to their elements in an artifact to element map (`*_a2eMap.json`)

The paper presents eight traceability configurations (see table II in the paper). This repository already includes the precalculated files for these configuration. To run the trace link processing phase of the eight configurations of the paper *you can skip the precalculation phase*.

If you wish to re-precalculate the files, instantiate the appropriate class in [TraceabilityRunner.py](./TraceabilityRunner.py) and call the `precalculate` method.
See [App.py](./App.py) for an example.

Each TraceabilityRunner automatically configures and sets up one of the eight configurations of the paper including the three precalculation steps from above. For example, the `BaseLineRunner` sets up the *FTLR* configuration of the paper and the `BaseLineUCTCDRunner` sets up *FTLR +uct +cd*.

**Note 1:** The `precalculate` method has two optional input parameters: `matrix_file_path`  and `artifact_map_file_path` as output path for the precalculated files. If set to `None`, the default output path will be used. Since the current precalculated files of the eight configurations are located at their default path, these files will be overwritten if you don't set these two parameters. The default output path patterns can be found in [TraceabilityRunner.py](./TraceabilityRunner.py).

**Note 2:** As depicted in the simplified architecture diagram above, the inclusion of the call graph happens *after* the precalculation. Therefore, the corresponding configurations with and without the call graph use the same precalculated files. For example, `BaseLineUCTMCCDRunner` uses the same precalculated files as `BaseLineUCTMCRunner`.

**Note 3:** All english and italian spacy word lemmas are precalculated, too. The files are in [preprocessing/resources](./preprocessing/resources). The files are simple csv files with all words of all datasets and their corresponding lemmas. We're using precalculated lemma files because the free google colab version does not seem to have enough RAM to run spacy and fastText at once. To re-precalculate the lemmas, see [Lemmatizer class](./preprocessing/Preprocessor.py).


## Trace Link Processing phase

This phase consists of the following activities:

* Load the correct precalculated files
* Decide if the call graph should be used
* Do the majority decision
* Derive/aggregate file level trace links
* Apply similarity threshold filters
* Do the evaluation: calculate F1 or Mean Average Precision (MAP)

There is a corresponding [TraceabilityRunner](./TraceabilityRunner.py) for each configuration of the paper which automatically does this setup. Since the precalculated files are also included in the repository, you only have to instantiate a `TraceabilityRunner` and call the `calculate_f1` or the `calculate_map` method. See [App.py](./App.py) for an example.

To check the correct setup, run [App.py](./App.py).
