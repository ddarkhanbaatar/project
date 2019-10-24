package infs7410.project1.part3;

import infs7410.project1.RerankerPRF;
import org.terrier.matching.models.WeightingModelLibrary;

import java.util.HashMap;

public class BM25_RSJ {
    private double k1 = 1.2D;
    private double b = 0.00D;
    private double k2 = 8.0D;
    private int f = 0;

    public BM25_RSJ(double b, double k1, double k2) {
        this.k1 = k1;
        this.k2 = k2;
        this.b = b;

    }

    public String getInfo() {
        return "BM25-k1_" + this.k1 + "-k2_" + this.k2 + "-b_" + this.b + "-f" + f;
    }

    public double score(String queryTerm, double queryFeq, double docFeq, double docLen, double avgDocLen, double N, double n, double R, double r) {
        double B = (1.0D - this.b) + this.b * docLen / avgDocLen;

        double weight1 = (r + 0.5D) / (R - r + 0.5D);
        double weight2 = (n - r + 0.5D) / (N - n - R + r + 0.5D);
        double RJS_weight = WeightingModelLibrary.log(weight1 / weight2);
        double saturation = ((this.k1 + 1.0D) * docFeq / (k1 * B + docFeq));
        double within_query = ((this.k2 + 1.0D) * queryFeq / (this.k2 + queryFeq));

        if (RerankerPRF.isLog) {
            // System.out.println(String.format("(%f - %f + 0.5D) / (%f - %f - %f + %f + 0.05D) = %f / %f =%f", n, r, N, n, R, r, (n - r + 0.5D), (N - n - R + r + 0.05D), weight2));
            // System.out.printf("log(%.0f/%.0f)=%f    ", weight1, weight2, WeightingModelLibrary.log(weight1/weight2));
            System.out.println(String.format("[%s] N:%.0f,R:%.0f,n:%.0f,r:%.0f,w1:%f,w2:%f,w:%f,sat:%f, docFreq: %.0f,querFreq:%.0f,score:%f",
                    queryTerm, N, R, n, r, weight1, weight2, RJS_weight, saturation, docFeq, queryFeq, RJS_weight * saturation * within_query));
        }
        return RJS_weight * saturation * within_query;
    }

    public double totalScore(String docId, String[] query,
                             HashMap<String, Integer> termsQueryFreq,
                             HashMap<String, HashMap<String, Integer>> termsDocFreq,
                             HashMap<String, Integer> docLenSet,
                             HashMap<String, RerankerPRF.RelevanceValue> termRelevance,
                             double avgDocLen, double N, double R, int f) {
        double totalScore = 0.0D;
        this.f = f;
        int num = 0;
        for (String queryTerm : query) {
            if (!termsDocFreq.containsKey(queryTerm)) continue;
            double docFeq, docLen;
            if (!termsDocFreq.get(queryTerm).containsKey(docId)) {
                continue;
//                docFeq = -0.000001;
//                docLen = avgDocLen;
            } else {
                docFeq = termsDocFreq.get(queryTerm).get(docId);
                docLen = docLenSet.get(docId);
            }

            int queryFeq = termsQueryFreq.get(queryTerm);
            double score = score(queryTerm, queryFeq, docFeq, docLen, avgDocLen, N, termRelevance.get(queryTerm).getNi(), R, termRelevance.get(queryTerm).getRi());
            totalScore += score;
            num++;
        }

        return totalScore / num;
    }

}
