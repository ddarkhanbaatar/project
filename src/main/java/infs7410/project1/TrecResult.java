package infs7410.project1;

public class TrecResult implements Comparable<TrecResult> {
    private String topic;
    private String docID;
    private int rank;
    private double score;
    private int originalIndex;

    private String runName;

    public TrecResult(String topic, String docID, int rank, double score, String runName) {
        this.topic = topic;
        this.docID = docID;
        this.rank = rank;
        this.score = score;
        this.runName = runName;
    }

    public TrecResult(String topic, String docID, double score, int originalIndex) {
        this.topic = topic;
        this.docID = docID;
        this.rank = 0;
        this.score = score;
        this.runName = "";
        this.originalIndex = originalIndex;
    }

    public String getTopic() {
        return topic;
    }

    public String getDocID() {
        return docID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRunName() {
        return runName;
    }

    public void setRunName(String runName) {
        this.runName = runName;
    }

    @Override
    public int compareTo(TrecResult o) {
        return Double.compare(getScore(), o.getScore());
    }

    public String toString() {
        return String.format("%s 0 %s %d %f %s", topic, docID, rank, score, runName);
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }
}
