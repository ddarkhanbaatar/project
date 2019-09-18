package infs7410.project1.core;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class BooleanQueryParser {
    private void parseBooleanQuery() {
        String baseUrl="https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?db=pubmed&term=";
        Scanner s = null;
        try {
            s = new Scanner(new URL(baseUrl+"diagnos*").openStream());
            System.out.println(s.findWithinHorizon("<QueryTranslation>\\s*(.*)\\s*<\\/QueryTranslation>", 0));

            //this.queries = TextProcessor.doStemAndStopwords(words);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
