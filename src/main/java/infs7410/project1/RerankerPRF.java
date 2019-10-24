package infs7410.project1;

import infs7410.project1.core.Topic;
import infs7410.project1.part3.BM25_RSJ;
import it.unimi.dsi.fastutil.Hash;
import org.terrier.matching.models.BM25;
import org.terrier.matching.models.Tf;
import org.terrier.matching.models.WeightingModel;
import org.terrier.structures.*;
import org.terrier.structures.postings.IterablePosting;

import java.io.IOException;
import java.util.*;

public class RerankerPRF {

    public class RelevanceValue {
        private int n;
        private int r;

        public RelevanceValue() {
            this.n = 0;
            this.r = 0;
        }

        public void increaseN() {
            n++;
        }

        public void increaseR() {
            r++;
        }

        public int getNi() {
            return n;
        }

        public int getRi() {
            return r;
        }
    }

    public class DocInfo {
        private int documentFrequency;
        private int documentLength;
        private boolean relevant;

        public DocInfo(int documentFrequency, int documentLength, boolean relevant) {
            this.documentFrequency = documentFrequency;
            this.documentLength = documentLength;
            this.relevant = relevant;
        }

        public int getDocumentFrequency() {
            return documentFrequency;
        }

        public int getDocumentLength() {
            return documentLength;
        }

        public boolean isRelevant() {
            return relevant;
        }
    }

    private long startTime = 0;
    public static boolean isLog = false;

    public RerankerPRF() {
    }


    public TrecResults rerank(
            String header, Topic topic, BM25_RSJ wm,
            HashMap<String, HashMap<String, Integer>> termsDocFreq,
            HashMap<String, Integer> docLen,
            int f, double avgDocLen) throws IOException {

        HashSet<String> docIdSet = new HashSet<>();
        HashMap<String, Integer> termsQueryFreq = new HashMap<>();
        long start = System.currentTimeMillis();
        TrecResults results = new TrecResults(); // Create a results list from the scored documents.
        List<TrecResult> baseline=new ArrayList<>(topic.getBaseline());

        System.out.println("baseline N:" + baseline.size());

        // Set document set
        for (TrecResult trecResult : baseline)
            docIdSet.add(trecResult.getDocID());

        // 1. Calculation of Term Frequency in the query
        {
            startTime();
            System.out.println("------------------------------------------------------------");
            for (String term : topic.getQueries()) {
                if (termsQueryFreq.containsKey(term)) continue;
                int tf = 0;
                for (String otherTerm : topic.getQueries())
                    if (otherTerm.equals(term))
                        tf++;
                termsQueryFreq.put(term, tf);
            }
        }


        int N = 1; // The number of documents in the topic
        int R = 0; // The number of relevant documents in the topic
        int i = 1; // Rank Index

        // First item is relevant -------------------
        TrecResult first = baseline.get(0);
        first.setRunName(wm.getInfo());

        results.getTrecResults().add(first); // First is first

        if (topic.getQrels().relevant.contains(first.getDocID()))
            R++;

        baseline.remove(0); // Remove first item

        // Until all items are ranked
        do {
            RerankerPRF.isLog = (topic.getTopicId().equals("CD007394")) && (i < 5);
            // Calculate small r and n for all terms in order to reduce iteration cost
            HashMap<String, RelevanceValue> termRelevance = new HashMap<>();
            for (String queryTerm : topic.getQueries()) {

                if (!termsDocFreq.containsKey(queryTerm)) continue; // if term

                RelevanceValue termValue = new RelevanceValue();
                HashMap<String, Integer> docFreq = termsDocFreq.get(queryTerm);
                // iterate ranked docs
                for (TrecResult doc : results.getTrecResults()) {
                    if (docFreq.containsKey(doc.getDocID())) {
                        termValue.increaseN();
                        if (topic.getQrels().relevant.contains(doc.getDocID())) {
                            termValue.increaseR();
                        }
                    }
                }
//                if (RerankerPRF.isLog)
//                    System.out.println("Term[" + queryTerm + "] - n=" + termValue.getNi() + ", r=" + termValue.getRi());
                termRelevance.put(queryTerm, termValue);
            }


            // Iterate until f to use RF
            TrecResults rfDocs = new TrecResults();
            if (RerankerPRF.isLog)
                System.out.println("________" + topic.getTopicId() + "________ 4." + (i + 1) + "(N=" + N + ",R=" + R + ")");

            for (int a = 0; a < f && a < baseline.size(); a++) {
                TrecResult doc = baseline.get(a);
                // Calculate total scores
                double score = wm.totalScore(doc.getDocID(), topic.getQueries(), termsQueryFreq, termsDocFreq, docLen, termRelevance, avgDocLen, N, R, f);

                rfDocs.getTrecResults().add(new TrecResult(doc.getTopic(), doc.getDocID(), score, a));
                if (RerankerPRF.isLog)
                    System.out.println("              [" + (a + 1) + "] DocId:" + doc.getDocID() + ", Score:" + score + ", Relevant:" + (topic.getQrels().relevant.contains(doc.getDocID())));
            }

            double max = rfDocs.getTrecResults().get(0).getScore();
            int high = 0;

            // Choose an item which has the highest score.
            for (int k = 1; k < rfDocs.getTrecResults().size(); k++) {
                if (rfDocs.getTrecResults().get(k).getScore() > max) {
                    max = rfDocs.getTrecResults().get(k).getScore();
                    high = k;
                }
            }
            if (RerankerPRF.isLog)
                System.out.println("Choose:" + rfDocs.get(high).getDocID() + " | " + rfDocs.get(high).getScore() + " | " + (rfDocs.get(high).getOriginalIndex() + 1) + "->" + (i + 1));
            // Highest value is selected as next ranking element
            TrecResult selectedDoc = rfDocs.get(high);
            results.getTrecResults().add(new TrecResult(
                    selectedDoc.getTopic(),
                    selectedDoc.getDocID(),
                    ++i, // i+1
                    selectedDoc.getScore(), // score
                    wm.getInfo()
            ));
            N++;
            if (topic.getQrels().relevant.contains(selectedDoc.getDocID()))
                R++;

            baseline.remove(selectedDoc.getOriginalIndex());
        } while (!baseline.isEmpty());
        long second = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("[%s] %s - { %d docs, %d queries } Time - (%02d:%02d)\n", header, topic.getTopicId(), topic.getDocs().length, topic.getQueries().length, second / 60, second % 60);
        return results;
    }

    private void startTime() {
        startTime = System.currentTimeMillis();
    }

    private String getDuration() {
        long second = (System.currentTimeMillis() - startTime) / 1000;
        return String.format("%02d:%02d", second / 60, second % 60);
    }
}
