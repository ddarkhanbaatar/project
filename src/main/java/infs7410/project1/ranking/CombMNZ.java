package infs7410.project1.ranking;

import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;

import java.util.HashMap;
import java.util.List;

public class CombMNZ extends Fusion {

    @Override
    public TrecResults Fuse(List<TrecResults> topicResults) {
        HashMap<String, TrecResult> seen = new HashMap<>();
        HashMap<String, Integer> occur = new HashMap<>();

        for (TrecResults doc : topicResults) {
            for (TrecResult result : doc.getTrecResults()) {
                System.out.println("Topic:" + result.getTopic() + ", DocID:" + result.getDocID() + ", score:" + result.getScore());
                if (!seen.containsKey(result.getDocID())) {
                    seen.put(result.getDocID(), result);
                } else {
                    // Sum the scores
                    double score = seen.get(result.getDocID()).getScore();
                    // TODO: IMPLEMENT ME.
                    result.setScore(result.getScore() + score);

                    seen.put(result.getDocID(), result);
                }
                if (result.getScore() >= 0) {
                    if (!occur.containsKey(result.getDocID()))
                        occur.put(result.getDocID(), 1);
                    else
                        occur.put(result.getDocID(), occur.get(result.getDocID()) + 1);
                }

                System.out.println("Seen["  + result.getDocID() + "] = " +seen.get(result.getDocID()).getScore());
            }
        }
        // Multiple by score
        for (String key : seen.keySet()){
            double score=seen.get(key).getScore()*occur.get(key);
            System.out.println("Key:"+key+", Sum:"+seen.get(key).getScore()+", occur:"+occur.get(key));
            seen.get(key).setScore(score);
        }

        return flatten(seen);
    }

    @Override
    public String toString() {
        return "CombMNZ";
    }
}
