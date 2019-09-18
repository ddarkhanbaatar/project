#!/bin/bash

echo "Evaluating result files"
echo "----------------------------------------------"
qrels="/mnt/c/apps/assess/project/input/tar/2018/training/qrels/2018-training.qrels"
res_file="/mnt/c/apps/assess/project/output/2018/title/tunning/"
eval_file="/mnt/c/apps/assess/project/output/2018/title/tunning/eval/"

for f in *.res; do
  cd /mnt/c/apps/Tools/trec_eval/
  ./trec_eval -q -m map -m ndcg -m ndcg_cut -m P -m set_P -m set_recall -m num_rel -m num_rel_ret $qrels "$res_file${f}" > "${eval_file}${f}.eval"
  echo "$f is completed"
done
