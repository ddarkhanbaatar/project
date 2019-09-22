#!/bin/bash

echo "Evaluating result files"
echo "----------------------------------------------"

# QRELS paths
qrels2017="/mnt/c/apps/assess/project/input/tar/2017/testing/qrels/2017-qrel_abs_test.qrels.txt"
qrels2018="/mnt/c/apps/assess/project/input/tar/2018/testing/qrels/2018-qrel_abs_test.qrels"
# Evaluating path
eval_path="/mnt/c/apps/assess/project/output/evals"

# 2017 path
title2017="/mnt/c/apps/assess/project/output/2017/title/testing"
boolean2017="/mnt/c/apps/assess/project/output/2017/boolean/testing"

# 2018 path
title2018="/mnt/c/apps/assess/project/output/2018/title/testing"
boolean2018="/mnt/c/apps/assess/project/output/2018/boolean/testing"

cd /mnt/c/apps/Tools/trec_eval/
# 2017 - Boolean query
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${boolean2017}/run-TF_IDF.res" > "${eval_path}/2017-boolean-TF_IDF.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${boolean2017}/run-BM25b0.0.res" > "${eval_path}/2017-boolean-BM25.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${boolean2017}/fusion/run-Borda.res" > "${eval_path}/2017-boolean-Borda.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${boolean2017}/fusion/run-CombSUM.res" > "${eval_path}/2017-boolean-CombSUM.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${boolean2017}/fusion/run-CombMNZ.res" > "${eval_path}/2017-boolean-CombMNZ.eval"
echo "2017 - Boolean query : Completed"
  
# 2017 - Title query
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${title2017}/run-TF_IDF.res" > "${eval_path}/2017-title-TF_IDF.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${title2017}/run-BM25b0.0.res" > "${eval_path}/2017-title-BM25.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${title2017}/fusion/run-Borda.res" > "${eval_path}/2017-title-Borda.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${title2017}/fusion/run-CombSUM.res" > "${eval_path}/2017-title-CombSUM.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2017 "${title2017}/fusion/run-CombMNZ.res" > "${eval_path}/2017-title-CombMNZ.eval"
echo "2017 - Title query : Completed"

# 2018 - Boolean query
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${boolean2018}/run-TF_IDF.res" > "${eval_path}/2018-boolean-TF_IDF.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${boolean2018}/run-BM25b0.0.res" > "${eval_path}/2018-boolean-BM25.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${boolean2018}/fusion/run-Borda.res" > "${eval_path}/2018-boolean-Borda.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${boolean2018}/fusion/run-CombSUM.res" > "${eval_path}/2018-boolean-CombSUM.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${boolean2018}/fusion/run-CombMNZ.res" > "${eval_path}/2018-boolean-CombMNZ.eval"
echo "2018 - Boolean query : Completed"

# 2018 - Title query
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${title2018}/run-TF_IDF.res" > "${eval_path}/2018-title-TF_IDF.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${title2018}/run-BM25b0.0.res" > "${eval_path}/2018-title-BM25.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${title2018}/fusion/run-Borda.res" > "${eval_path}/2018-title-Borda.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${title2018}/fusion/run-CombSUM.res" > "${eval_path}/2018-title-CombSUM.eval"
./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m Rprec $qrels2018 "${title2018}/fusion/run-CombMNZ.res" > "${eval_path}/2018-title-CombMNZ.eval" 
echo "2018 - Title query : Completed"