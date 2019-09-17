package infs7410.project1.core;

import java.io.File;
import java.util.ArrayList;

public class TopicParser {
    public static ArrayList<Topic> parse(String path) {
        ArrayList<Topic> result = new ArrayList<>();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            Topic topic = new Topic(file);
            result.add(topic);
        }

        return result;
    }
}
