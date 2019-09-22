# To illustrate gain and loss
import sys

import matplotlib.pyplot as plt
import numpy as np
from trectools import TrecRes

if __name__ == "__main__":
    if len(sys.argv) < 5:
        print("not enough arguments specified")
        sys.exit(1)
    fname1 = sys.argv[1]
    fname2 = sys.argv[2]
    measure = sys.argv[3]
    output = sys.argv[4]

    ev1 = TrecRes(fname1)
    ev2 = TrecRes(fname2)

    r1 = ev1.get_results_for_metric(measure)
    r2 = ev2.get_results_for_metric(measure)

    ind = np.arange(len(r1))
    width = 0.35
    
    figure, axes = plt.subplots(figsize = (12,6))
    pos1 = [i for i, _ in enumerate(r1)]
    pos2 = [i+width for i, _ in enumerate(r2)]
   
    rects1 = plt.bar(pos1,r1.values(),width,label=fname1, color='blue')
    rects1 = plt.bar(pos2,r2.values(),width,label=fname2, color='red')

    fn1=fname1.replace(".eval","").replace("../evals/","")
    fn2=fname2.replace(".eval","").replace("../evals/","")
    l1=fn1.split('-')
    l2=fn2.split('-')

    plt.xticks(ind, list(r1.keys()), rotation="vertical")
    plt.ylim(-1, 1)
    plt.title('{} and {} ({} {})'.format(l1[2],l2[2],l1[0],l1[1]))
    plt.ylabel(measure)
    plt.tight_layout()
    plt.savefig(output)
