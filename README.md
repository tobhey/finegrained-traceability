# Improving Traceability Link Recovery Using Fine-grained Requirements-to-Code Relations

This repository contains the code base and the datasets for the paper **Improving Traceability Link Recovery Using Fine-grained Requirements-to-Code Relations**. The paper improves the traceability between code and requirement files by deriving file level trace links from finegrained relations between code elements (e.g. methods) and requirement elements (e.g. requirement sentences). 

## Running the code

The code can be run locally or in Google Colab.

### Google Colab

Open the jupyter notebook in Google Colab and follow the instructions.

### Local

See [INSTALL](./INSTALL.md) for the local installation.

Since instantiating the fastText model and the spacy lemmatizer is expensive, the trace link recovery process is split into two phases: precalculation and trace link processing.
The following images shows a simplifies architecture of the process.

![Simplified architecture](./diagrams/simplified_architecture.svg)

### Precalculation phase
The precalculation phase consists of the following steps:
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


### Trace Link Processing phase

This phase consists of the following activities:

* Load the correct precalculated files
* Decide if the call graph should be used
* Do the majority decision
* Derive/aggregate file level trace links
* Apply similarity threshold filters
* Do the evaluation: calculate F1 or Mean Average Precision (MAP)

There is a corresponding [TraceabilityRunner](./TraceabilityRunner.py) for each configuration of the paper which automatically does this setup. Since the precalculated files are also included in the repository, you only have to instantiate a `TraceabilityRunner` and call the `calculate_f1` or the `calculate_map` method. See [App.py](./App.py) for an example.

**Note 1:** Both `calculate_f1` and `calculate_map` have the optional parameters `matrix_file_path` and `artifact_map_file_path` to locate the precalculated files. The default value is `None` which means that the precalculated files are loaded from their default locations.

**Note 2:** Itrust doesn't have use case templates. Therefore, running Itrust with any `BaseLineUCT*Runner` will not work.

## Datasets

This repository contains four datasets:

<table style="border-collapse: collapse; margin-left:15px; ">
    <tr style="border-bottom: 1px solid gray">
        <th style="padding-right:15px">dataset</th><th style="padding-right:15px">dataset class name</th><th style="padding-right:15px">language</th>
    </tr>
    <tr><td>eTour</td><td>Etour</td><td>english</td></tr>
    <tr><td>iTrust</td><td>Itrust</td><td>english</td></tr>
    <tr><td>SMOS</td><td>Smos</td><td>italian</td></tr>
    <tr><td>eAnci</td><td>Eanci</td><td>italian</td></tr>
</table>

Originally, eTour and SMOS were bilingual datasets. We translated them into their prevailing language. All datasets have a gold standard with solution trace links created by their original authors.

The datasets are in [datasets](./datasets/).