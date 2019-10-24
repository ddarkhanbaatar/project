package infs7410.project1.core;

import infs7410.project1.TrecResult;
import infs7410.project1.TrecResults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class TopicParser {
    public static ArrayList<Topic> parse(String path, String queryType, String queryPath) {
        ArrayList<Topic> result = new ArrayList<>();
        File folder = new File(path);
        long start = System.currentTimeMillis();
        int q = 0, d = 0;
        HashMap<String, String> booleanQueries = new HashMap<>();

        // Combine boolean queries
        if (queryType.equals("B")) {
            try (Scanner scanner = new Scanner(Paths.get(queryPath))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split("=");
                    booleanQueries.put(values[0], values[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (File file : folder.listFiles()) {
            Topic topic = new Topic(file);
            d += topic.getDocs().length;
            q += topic.getQueries().length;
            if (queryType.equals("B"))
                topic.mergeQuery(booleanQueries.get(topic.getTopicId()).split(" "));
            result.add(topic);
        }
        long second = (System.currentTimeMillis() - start) / 1000;
//        System.out.printf("Topics No:%d,  Total Queries: %d, Total Pids: %d, Time %d:%d\n", result.size(), q, d, second / 60, second % 60);

        return result;
    }

    public static List<Topic> parseQrel(String qrelPath, TrecResults baseline, List<Topic> topics) {

        HashMap<String, Qrel> qrelsSet = new HashMap<>();
        HashMap<String, List<TrecResult>> topicSet = new HashMap<>();

        ArrayList<Topic> topicResult=new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(qrelPath));) {
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] values = line.split("\\s+");
                String topic = values[0];
                String docid = values[2];
                Boolean relevant = Integer.parseInt(values[3]) == 1;

                if (!qrelsSet.containsKey(topic)) {
                    qrelsSet.put(topic, new Qrel());
                }

                if (relevant)
                    qrelsSet.get(topic).relevant.add(docid);
                else
                    qrelsSet.get(topic).nonRelevant.add(docid);
            }
//            System.out.println("Qrels file has been read:"+qrelsSet.size());

            // Convert treclist into topic based hashmap
            for(TrecResult trecResult:baseline.getTrecResults()){
                if(!topicSet.containsKey(trecResult.getTopic()))
                    topicSet.put(trecResult.getTopic(), new ArrayList<>());
                topicSet.get(trecResult.getTopic()).add(trecResult);
            }
            // Assign values
            for(Topic topic:topics){
                topic.setQrels(qrelsSet.get(topic.getTopicId()));
                topic.setBaseline(topicSet.get(topic.getTopicId()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return topics;
    }
}
