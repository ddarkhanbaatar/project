package infs7410.project1.core;

import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Topic {
    private String topicId;
    private String title;
    private String[] queries;
    private String query;
    private String booleanQueries;
    private String[] docs;
    private String fileName;

    public Topic(){

    }
    public Topic(File topicFile){
        parseFile(topicFile);
    }

    private void parseFile(File topicFile){
        this.fileName= topicFile.getName();
        try (Scanner scanner = new Scanner(topicFile);) {
            scanner.useDelimiter("Topic: |Title: |Query: |Pids: ");
            int index=1;
            while (scanner.hasNext()) {
                String line = scanner.next();
                switch (index)
                {
                    case 1: // Topic
                        this.topicId=line.split("\n")[0];
                        break;
                    case 2: // Title
                        this.title=line.split("\n")[0];
                        // Convert to query list
                        String[] words=this.title.split(" ");
                        this.queries=TextProcessor.doStemAndStopwords(words);

                        break;
                    case 3: // Boolean Queries
                        this.booleanQueries=line;
                        break;
                    case 4: // Docs
                        line=line.replace(" ","");
                        String[] p=line.split("\n");
                        ArrayList<String> pids=new ArrayList<>();
                        for(int i=1;i<p.length;i++)
                            if(p[i].trim().length()>0) // No Empty document number
                                pids.add(p[i]);
                        this.docs=pids.stream().toArray(String[]::new);
                        break;
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        this.queries = query;
    }
}
