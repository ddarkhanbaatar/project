package infs7410.project1.reduction;

import org.terrier.querying.IndexRef;
import org.terrier.structures.*;
import org.terrier.structures.postings.IterablePosting;

import java.io.IOException;
import java.util.*;

public class KLIReduction {

    public String[] reduce(String[] terms, String[] docIds, int r, IndexRef ref) throws IOException {
        int K = (terms.length * r) / 100;
        Index index = IndexFactory.of(ref);
        PostingIndex inverted = index.getInvertedIndex();
        Lexicon<String> lexicon = index.getLexicon();
        MetaIndex meta = index.getMetaIndex();
        HashSet<String> docList = new HashSet<>(Arrays.asList(docIds));

        // Number of documents in the collection
        double N = index.getCollectionStatistics().getNumberOfDocuments();
        /*
         * TF - the number of times t appears in the collection. entry.getDocumentFrequency()
         * TT - the number of total  terms in the collection.
         * P(t|C) = TF/TT
         *
         * TO - total term feq in each doc. the sum of the number of occurrences of t in each document in D
         * DL - the sum of the document lengths for the documents in D
         * P(t|D) = TO/DL
         * */
        double TF = 0, TT = 0, TO = 0, DL = 0;
        // Number of terms in the collection
        TT = index.getCollectionStatistics().getNumberOfTokens();

        List<Pair> scoredTerms = new ArrayList<>(terms.length);

        for (String term : terms) {
            LexiconEntry entry = lexicon.getLexiconEntry(term);
            if (entry == null) {
                scoredTerms.add(new Pair(term, 0));
                continue;
            }
            // The frequency of t in the collection.
            TF = entry.getFrequency();
            IterablePosting ip = inverted.getPostings(entry);
            TO = 0.0;
            DL = 0.0;
            while (ip.next() != IterablePosting.EOL) {
                String docId = meta.getItem("docno", ip.getId());
                if (docList.contains(docId)) {
                    DL += ip.getDocumentLength();
                    TO += ip.getFrequency();
                }
            }

            double PtD=TO/DL;
            double PtC=TF/TT;
            double kli_t= PtD* Math.log(PtD/PtC);
            scoredTerms.add(new Pair(term, kli_t));
        }
        Collections.sort(scoredTerms);
        Collections.reverse(scoredTerms);
        String[] result = new String[K < scoredTerms.size() ? K : scoredTerms.size()];

        for (int i = 0; i < K && i < scoredTerms.size(); i++) {
            result[i] = scoredTerms.get(i).term;
        }
        return result;
    }

    static class Pair implements Comparable<Pair> {
        String term;
        double score;

        public Pair(String term, double score) {
            this.term = term;
            this.score = score;
        }

        @Override
        public int compareTo(Pair o) {
            return Double.compare(score, o.score);
        }
    }
}
