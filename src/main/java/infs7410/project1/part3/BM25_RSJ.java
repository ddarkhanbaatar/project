package infs7410.project1.part3;

import infs7410.project1.RerankerPRF;
import org.terrier.matching.models.WeightingModel;

import java.util.HashMap;

public class BM25_RSJ {
    private static final long serialVersionUID = 1L;
    private double k1 = 1.2D;
    private double b = 0.00D;
    private double k2 = 8.0D;

    public BM25_RSJ(double b, double k1, double k2) {
        this.k1 = k1;
        this.k2 = k2;
        this.b = b;
    }

    public String getInfo() {
        return "BM25-k1_" + this.k1 + "-k2_" + this.k2 + "-b_" + this.b + "";
    }

    public double score(double queryFeq, int docFeq, int docLen, double avgDocLen, double N, double n, double R, double r) {
        double B = (1.0D - this.b) + this.b * docLen / avgDocLen;

        double weight1 = (r + 0.5D) / (R - r + 0.5D);
        double weight2 = (n - r + 0.5D) / (N - n - R + r + 0.05D);
        double RJS_weight = Math.log(weight1 / weight2);
        double saturation = ((this.k1 + 1.0D) * docFeq / (k1 * B + docFeq));
        double within_query = ((this.k2 + 1.0D) * queryFeq / (this.k2 + queryFeq));
        System.out.println("N:" + N + ", R:" + R + ", queryReq:" + queryFeq + ", docFeq:" + docFeq + ", docLen:" + docLen + ", avgDocLen:" + avgDocLen);
        System.out.println(String.format("w1:%f, w2:%f, sat:%f, query_within:%f", weight1, weight2, saturation, within_query));

        return RJS_weight * saturation * within_query;
    }

    public double totalScore(String docId, String[] query,
                             HashMap<String, Integer> termsQueryFreq,
                             HashMap<String, HashMap<String, RerankerPRF.DocInfo>> termsDocInfo,
                             HashMap<String, RerankerPRF.RelevanceValue> termRelevance,
                             double avgDocLen, double N, double R) {
        double totalScore = 0.0D;

        for (String queryTerm : query) {
            HashMap<String, RerankerPRF.DocInfo> docList = termsDocInfo.get(queryTerm);
            if (!docList.containsKey(docId)) continue;

            RerankerPRF.DocInfo docInfo = docList.get(docId);
            int queryFeq = termsQueryFreq.get(queryTerm);
            int docFeq = docInfo.getDocumentFrequency();
            int docLen = docInfo.getDocumentLength();
            double score = score(queryFeq, docFeq, docLen, avgDocLen, N, termRelevance.get(queryTerm).getNi(), R, termRelevance.get(queryTerm).getRi());
            System.out.println("Term:" + queryTerm + ", Score:" + score);
            totalScore += score;
        }

        return totalScore;
    }

}
