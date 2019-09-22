package infs7410.project1.core;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Topic {
    private String topicId;
    private String title;
    private String[] queries;
    private String[] orgQueries;
    private String query;
    private String booleanQueries;
    private String[] docs;
    private String fileName;

    public Topic() {

    }

    public Topic(File topicFile) {
        parseFile(topicFile);
    }

    private void parseFile(File topicFile) {
        this.fileName = topicFile.getName();
        try (Scanner scanner = new Scanner(topicFile);) {
            scanner.useDelimiter("Topic: |Title: |Query: |Pids: ");
            int index = 1;
            while (scanner.hasNext()) {
                String line = scanner.next();
                switch (index) {
                    case 1: // Topic
                        this.topicId = line.split("\n")[0].trim();
                        break;
                    case 2: // Title
                        this.title = line.split("\n")[0];
                        // Convert to query list
                        String[] words = this.title.split(" ");
                        this.queries = TextProcessor.doStemAndStopwords(words);

                        break;
                    case 3: // Boolean Queries
                        this.booleanQueries = line;
                        break;
                    case 4: // Docs
                        line = line.replace(" ", "");
                        String[] p = line.split("\n");
                        ArrayList<String> pids = new ArrayList<>();
                        for (int i = 1; i < p.length; i++)
                            if (p[i].trim().length() > 0) // No Empty document number
                                pids.add(p[i].trim());
                        this.docs = pids.stream().toArray(String[]::new);
                        break;
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Combine two queries
    public void mergeQuery(String[] booleanQueries) {
        String[] combinedQuery = new String[this.queries.length + booleanQueries.length];
        System.arraycopy(this.queries, 0, combinedQuery, 0, this.queries.length);
        System.arraycopy(booleanQueries, 0, combinedQuery, this.queries.length, booleanQueries.length);

        System.out.println(String.format("[%s] Q(t)=%d, Q(b)=%d Total=%d", this.getTopicId(), this.queries.length, booleanQueries.length, combinedQuery.length));
        this.queries = combinedQuery;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getQueries() {
        return queries;
    }

    public void setQueries(String[] queries) {
        this.queries = queries;
    }

    public String[] getDocs() {
        return docs;
    }

    public void setDocs(String[] docs) {
        this.docs = docs;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBooleanQueries() {
        return booleanQueries;
    }

    public void setBooleanQueries(String booleanQueries) {
        this.booleanQueries = booleanQueries;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setReductionQuery(String[] query) {
        this.orgQueries = this.queries;
        this.queries = query;
    }

    public void resetReducedQuery() {
        if (this.orgQueries != null)
            this.queries = orgQueries;
    }
}
