#!/bin/bash

echo "Gain and Loss illustration"
echo "----------------------------------------------"


# 2017 - Boolean Map
python3.6 ../../python/gainloss_plot.py ../evals/2017-boolean-BM25.eval ../evals/2017-boolean-TF_IDF.eval map 2017-boolean-BM25-vs-TF_IDF.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-boolean-BM25.eval ../evals/2017-boolean-Borda.eval map 2017-boolean-BM25-vs-Borda.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-boolean-BM25.eval ../evals/2017-boolean-CombSUM.eval map 2017-boolean-BM25-vs-CombSUM.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-boolean-BM25.eval ../evals/2017-boolean-CombMNZ.eval map 2017-boolean-BM25-vs-CombMNZ.png
echo "1. 2017 - Boolean query : Completed"

# 2017 - Title Map
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-TF_IDF.eval map 2017-title-BM25-vs-TF_IDF.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-Borda.eval map 2017-title-BM25-vs-Borda.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-CombSUM.eval map 2017-title-BM25-vs-CombSUM.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-CombMNZ.eval map 2017-title-BM25-vs-CombMNZ.png
echo "2. 2017 - Title query : Completed"

# 2018 - Boolean Map
python3.6 ../../python/gainloss_plot.py ../evals/2018-boolean-BM25.eval ../evals/2018-boolean-TF_IDF.eval map 2018-boolean-BM25-vs-TF_IDF.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-boolean-BM25.eval ../evals/2018-boolean-Borda.eval map 2018-boolean-BM25-vs-Borda.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-boolean-BM25.eval ../evals/2018-boolean-CombSUM.eval map 2018-boolean-BM25-vs-CombSUM.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-boolean-BM25.eval ../evals/2018-boolean-CombMNZ.eval map 2018-boolean-BM25-vs-CombMNZ.png
echo "3. 2018 - Boolean query : Completed"


# 2018 - Title Map
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-TF_IDF.eval map 2018-title-BM25-vs-TF_IDF.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-Borda.eval map 2018-title-BM25-vs-Borda.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-CombSUM.eval map 2018-title-BM25-vs-CombSUM.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-CombMNZ.eval map 2018-title-BM25-vs-CombMNZ.png
echo "4. 2018 - Title query : Completed"
  
# 2017 - Title Rprec
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-TF_IDF.eval Rprec 2017-title-BM25-vs-TF_IDF-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-Borda.eval Rprec 2017-title-BM25-vs-Borda-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-CombSUM.eval Rprec 2017-title-BM25-vs-CombSUM-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2017-title-BM25.eval ../evals/2017-title-CombMNZ.eval Rprec 2017-title-BM25-vs-CombMNZ-rprec.png
echo "5. 2017 - Title query Rprec : Completed"


# 2018 - Title Rprec
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-TF_IDF.eval Rprec 2018-title-BM25-vs-TF_IDF-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-Borda.eval Rprec 2018-title-BM25-vs-Borda-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-CombSUM.eval Rprec 2018-title-BM25-vs-CombSUM-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-title-CombMNZ.eval Rprec 2018-title-BM25-vs-CombMNZ-rprec.png
echo "6. 2018 - Title query Rprec : Completed"

# Reduction - Rprec
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2017-IDF-reduction-80.eval Rprec 2017-title-BM25-vs-IDFr-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2017-KLI-reduction-80.eval Rprec 2017-title-BM25-vs-KLI-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-IDF-reduction-80.eval Rprec 2018-title-BM25-vs-IDFr-rprec.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-KLI-reduction-80.eval Rprec 2018-title-BM25-vs-KLI-rprec.png
echo "7. Reduction forRprec : Completed"

# Reduction - map
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2017-IDF-reduction-80.eval map 2017-title-BM25-vs-IDFr.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2017-KLI-reduction-80.eval map 2017-title-BM25-vs-KLI.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-IDF-reduction-80.eval map 2018-title-BM25-vs-IDFr.png
python3.6 ../../python/gainloss_plot.py ../evals/2018-title-BM25.eval ../evals/2018-KLI-reduction-80.eval map 2018-title-BM25-vs-KLI.png
echo "8. Reduction forRprec : Completed"