package infs7410.project1.core;

import java.util.HashMap;

public class Qrel {
    public HashMap<String, Boolean> relevant;
    public HashMap<String, Boolean> nonRelevant;

    public Qrel() {
        relevant=new HashMap<>();
        nonRelevant=new HashMap<>();
    }


}
