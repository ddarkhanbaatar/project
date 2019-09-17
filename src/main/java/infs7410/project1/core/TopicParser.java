package infs7410.project1.core;

import java.io.File;
import java.util.ArrayList;

public class TopicParser {
    public static ArrayList<Topic> parse(String path) {
        ArrayList<Topic> result = new ArrayList<>();
        File folder = new File(path);
        long start = System.currentTimeMillis();
        int q = 0, d = 0;
        for (File file : folder.listFiles()) {
            Topic topic = new Topic(file);
            d += topic.getDocs().length;
            q += topic.getQueries().length;
            result.add(topic);
        }
        long second = (System.currentTimeMillis() - start) / 1000;
        System.out.printf("Topics No:%d,  Total Queries: %d, Total Pids: %d, Time %d:%d\n", result.size(), q, d, second / 60, second % 60);

        return result;
    }
}
