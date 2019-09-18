package infs7410.project1.reduction;

import org.terrier.querying.IndexRef;
import org.terrier.querying.Manager;
import org.terrier.querying.ManagerFactory;
import org.terrier.querying.SearchRequest;
import org.terrier.structures.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IDFReduction {

    public String[] reduce(String[] terms, int r, IndexRef ref) throws IOException {
        int K = (terms.length * r) / 100;
        Index index = IndexFactory.of(ref);
        PostingIndex inverted = index.getInvertedIndex();
        Lexicon<String> lexicon = index.getLexicon();
        double N = index.getCollectionStatistics().getNumberOfDocuments();

        List<Pair> scoredTerms = new ArrayList<>(terms.length);

        for (String term : terms) {
            LexiconEntry entry = lexicon.getLexiconEntry(term);
            if (entry == null) {
                scoredTerms.add(new Pair(term, 0));
                continue;
            }

            double docFreq = entry.getDocumentFrequency();

            double idf = Math.log(N / (docFreq + 1));
            scoredTerms.add(new Pair(term, idf));
        }
        Collections.sort(scoredTerms);
        Collections.reverse(scoredTerms);
        String[] result = new String[K < scoredTerms.size() ? K : scoredTerms.size()];


        for (int i = 0; i < K && i < scoredTerms.size(); i++) {
            result[i] = scoredTerms.get(i).getTerm();
        }
        return result;
    }

    public String[] reduceK(String[] terms, int K, IndexRef ref) throws IOException {
        Index index = IndexFactory.of(ref);
        PostingIndex inverted = index.getInvertedIndex();
        Lexicon<String> lexicon = index.getLexicon();
        double N = index.getCollectionStatistics().getNumberOfDocuments();

        List<Pair> scoredTerms = new ArrayList<>(terms.length);

        for (String term : terms) {
            LexiconEntry entry = lexicon.getLexiconEntry(term);
            if (entry == null) {
                scoredTerms.add(new Pair(term, 0));
                continue;
            }

            double docFreq = entry.getDocumentFrequency();

            double idf = Math.log(N / (docFreq + 1));
            scoredTerms.add(new Pair(term, idf));
        }
        Collections.sort(scoredTerms);
        Collections.reverse(scoredTerms);
        String[] result = new String[K < scoredTerms.size() ? K : scoredTerms.size()];


        for (int i = 0; i < K && i < scoredTerms.size(); i++) {
            result[i] = scoredTerms.get(i).getTerm();
        }
        return result;
    }


}
