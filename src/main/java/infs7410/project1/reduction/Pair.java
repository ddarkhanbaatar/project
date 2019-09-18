package infs7410.project1.reduction;

public class Pair implements Comparable<Pair> {
    private String term;
    private double score;

    public Pair(String term, double score) {
        this.term = term;
        this.score = score;
    }

    @Override
    public int compareTo(Pair o) {
        return Double.compare(score, o.score);
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}