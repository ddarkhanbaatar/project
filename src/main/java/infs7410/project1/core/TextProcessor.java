package infs7410.project1.core;

import org.terrier.terms.PorterStemmer;
import org.terrier.terms.Stopwords;

import java.util.ArrayList;

public class TextProcessor {
    public static String[] doStemAndStopwords(String[] words) {
        PorterStemmer stemmer = new PorterStemmer();
        Stopwords stopwords = new Stopwords(stemmer);
        ArrayList<String> wordList = new ArrayList<String>(words.length);
        // I have add "patient word in \terrier-project\share\stopword-list.txt"
        for (int i = 0; i < words.length; i++) {
            if (!stopwords.isStopword(words[i])) {
                String word = stemmer.stem(words[i]);
                //if (!wordList.contains(word))
                wordList.add(word);
            }
        }
        return (String[]) wordList.stream().toArray(String[]::new);
    }
}
