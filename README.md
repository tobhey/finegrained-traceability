# Improving Traceability Link Recovery Using Fine-grained Requirements-to-Code Relations

This repository contains the code base and the datasets for the paper **Improving Traceability Link Recovery Using Fine-grained Requirements-to-Code Relations**. The paper improves the traceability between code and requirement files by deriving file level trace links from finegrained relations between code elements (e.g. methods) and requirement elements (e.g. requirement sentences). 

## Code Base
* See [INSTALL.md](./INSTALL.md#Running-the-code)


## Datasets

This repository contains four datasets:

<table style="border-collapse: collapse; margin-left:15px; ">
    <tr style="border-bottom: 1px solid gray">
        <th style="padding-right:15px">dataset</th><th style="padding-right:15px">language</th>
    </tr>
    <tr><td>eTour</td><td>english</td></tr>
    <tr><td>iTrust</td><td>english</td></tr>
    <tr><td>SMOS</td><td>italian</td></tr>
    <tr><td>eAnci</td><td>italian</td></tr>
</table>

Originally, eTour and SMOS were bilingual datasets. We translated them into their prevailing language. All datasets have a gold standard with solution trace links created by their original authors.

The datasets are in [datasets](./datasets/).