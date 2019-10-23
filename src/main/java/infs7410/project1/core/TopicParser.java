package infs7410.project1.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
        System.out.printf("Topics No:%d,  Total Queries: %d, Total Pids: %d, Time %d:%d\n", result.size(), q, d, second / 60, second % 60);

        return result;
    }

    public static ArrayList<Topic> parseQrel(String qrelPath, ArrayList<Topic> topics) {

        HashMap<String, Qrel> result = new HashMap<>();
        try (Scanner scanner = new Scanner(new File(qrelPath));) {
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                String line = scanner.next();
                String[] values = line.split("\\s+");
                String topic = values[0];
                String docid = values[2];
                Boolean relevant = Integer.parseInt(values[3]) == 1;

                if (!result.containsKey(topic)) {
                    result.put(topic, new Qrel());
                }

                if (relevant)
                    result.get(topic).relevant.put(docid, true);
                else
                    result.get(topic).nonRelevant.put(docid, false);
            }
            System.out.println("Qrels file has been read:"+result.size());
            // Assign values
            for(int i=0;i<topics.size();i++){
                topics.get(i).setQrels(result.get(topics.get(i).getTopicId()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return topics;
    }
}
