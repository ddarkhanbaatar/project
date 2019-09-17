package infs7410.project1.core;


import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;

public interface Normaliser {
    void init(TrecResults items);

    double normalise(TrecResult result);
}
