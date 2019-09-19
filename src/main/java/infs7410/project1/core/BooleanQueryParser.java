package infs7410.project1.core;

import infs7410.project1.reduction.IDFReduction;
import infs7410.project1.reduction.Pair;
import org.terrier.querying.IndexRef;
import org.terrier.structures.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BooleanQueryParser {
    public void parseBooleanQuery(ArrayList<Topic> topics, IndexRef ref, String path) {

        try (FileWriter writer = new FileWriter(path );
             BufferedWriter bw = new BufferedWriter(writer)) {
            for (int i = 0; i < topics.size(); i++) {
                long start = System.currentTimeMillis();
                // 1. Cleaning query
                String[] terms = clearQuery(topics.get(i).getBooleanQueries());

                // 2. Get extended term
                String[] extendedTerms = new String[terms.length];
                for (int t = 0; t < terms.length; t++) {
                    char last = terms[t].charAt(terms[t].length() - 1);
                    if (last == '*') {
                        String[] exts = getExtendedTerms(terms[t]);
                        String extTerm = getHighestTermByIDF(exts, ref);
                        extendedTerms[t] = extTerm;
                    } else
                        extendedTerms[t] = terms[t];

                }
                // 3. Stem and Stopwords on the query
                String[] optimizedQuery = TextProcessor.doStemAndStopwords(extendedTerms);

                // 4. IDFr K=5
                IDFReduction reduction = new IDFReduction();
                String[] finalQuery = reduction.reduceK(optimizedQuery, 5, ref);

                // 5. Store query for future merging

                long second = (System.currentTimeMillis() - start) / 1000;
                bw.write(String.format("%s=%s\n", topics.get(i).getTopicId(), String.join(" ", finalQuery)));
                System.out.println(String.format("[%d/%d] %s TermsNo:%d (%d:%d) ", i + 1, topics.size(), topics.get(i).getTopicId(), finalQuery.length, second / 60, second % 60));
            }


        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

    }

    private String[] clearQuery(String query) {
        String[] symbols = new String[]{
                "$", "/", "\"", ".", ",", "\r", "\n", "(", ")", "[", "]", "-", "/", ":", "=", "?"
        };
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(
                "exp",
                "adj1", "adj2", "adj3", "adj4", "adj5", "adj6", "adj7", "adj8", "adj",
                "mp", "tw", "sh", "ti", "mp", "ot", "tw", "ab","yr","au",
                "a", "b", "c", "t", "i",
                "or", "and", "not",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
                "blood","limit","tiab","mesh"
        ));

        for (String s : symbols) {
            query = query.replace(s, " ");
        }


        String[] words = query.split(" ");
        ArrayList<String> result = new ArrayList<>();
        for (String word : words) {
            if (word == null)
                continue;
            if (word.isEmpty())
                continue;
            if (!keywords.contains(word.toLowerCase()))
                result.add(word);
        }
        return result.stream().toArray(String[]::new);
    }

    private String[] getExtendedTerms(String term) {
        int error = 0;
        while (error < 2) {
            String baseUrl = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
            Scanner s = null;
            try {
                s = new Scanner(new URL(baseUrl + term).openStream());
                String result = s.findWithinHorizon("<QueryTranslation>\\s*(.*)\\s*<\\/QueryTranslation>", 0);
                result = result.replace("[All Fields]", "");
                result = result.replace(" OR ", " ");
                result = result.replace("<QueryTranslation>", "");
                result = result.replace("<\\/QueryTranslation>", "");
                TimeUnit.MICROSECONDS.sleep(100);
                return result.split(" ");

            } catch (Exception e) {
                error++;
                System.out.println("Network error:" + error);
            }
        }
        return null;
    }

    private String getHighestTermByIDF(String[] terms, IndexRef ref) throws IOException {
        Index index = IndexFactory.of(ref);
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
        return scoredTerms.get(0).getTerm();
    }
}
