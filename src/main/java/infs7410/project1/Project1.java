package infs7410.project1;

import infs7410.project1.core.*;
import infs7410.project1.ranking.Borda;
import infs7410.project1.ranking.CombMNZ;
import infs7410.project1.ranking.CombSUM;
import infs7410.project1.ranking.Fusion;
import infs7410.project1.reduction.IDFReduction;
import infs7410.project1.reduction.KLIReduction;
import org.terrier.matching.models.BM25;
import org.terrier.matching.models.TF_IDF;
import org.terrier.matching.models.WeightingModel;
import org.terrier.querying.*;
import org.terrier.structures.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;


public class Project1 {
    private int i;

    public static void main(String[] args) throws IOException {
        // ------------------------------------------------------------- //
        // The following code shows how you could use the code provided. //
        // It is not commented here because it should be fairly obvious. //
        // The classes themselves, however have detailed comments which  //
        // should provide some insights into the inner workings of what  //
        // is going on.                                                  //
        // Feel free to edit these classes to complete this project.     //
        // You are not marked on the beauty of your code, just that it   //
        // produces the expected results in a reasonable amount of time! //
        // ------------------------------------------------------------- //

        // Define important paths
        HashMap<String, String> path = new HashMap<>();
        path.put("runs", "./input/runs/");
        path.put("tar", "./input/tar/");
        path.put("output", "./output/");
        path.put("fusion", "fusion/");
        path.put("1", "testing/");   // testing folder
        path.put("2", "training/");  // training folder
        path.put("3", "tunning/");   // tuning folder
        path.put("7", "2017/");      // 2017 folder
        path.put("8", "2018/");      // 2018 folder
        path.put("B", "boolean/");   // Boolean query folder
        path.put("T", "title/");     // Title query folder
        path.put("topics", "topics");


        // Main variables
        Index index = Index.createIndex("./var/index", "pubmed");
        Reranker reranker = new Reranker(index);
        WeightingModel model;

        String topicPath = "";
        ArrayList<Topic> topics = null;

        // Initialize re-ranker variables once -------------------------------------------------------
        Lexicon lex = index.getLexicon();
        PostingIndex invertedIndex = index.getInvertedIndex();
        MetaIndex meta = index.getMetaIndex();


        int method = 0;
        String parameter = "";
        String queryType = "";
        String dataType = "";
        String dataYear = "";
        Scanner in = new Scanner(System.in);
        // Program menu ---------------------------------------
        {
            System.out.println("*************************************************************************");
            System.out.println("Topics are parsed into memory");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("Please select method:");
            System.out.println("\t1. TF_IDF");
            System.out.println("\t2. BM25");
            System.out.println("\t3. Borda");
            System.out.println("\t4. CombSUM");
            System.out.println("\t5. CombMNZ");
            System.out.println("\t6. IDF reduction");
            System.out.println("\t7. KLI reduction");
            System.out.println("\t8. Parsing Boolean query");
            System.out.println("Value:");

            method = in.nextInt();
            if (method > 8 || method < 1) {
                System.out.println("The method is invalid !!!");
                return;
            }

            if (method < 6) {
                System.out.println("------------------------------------------------------------------------");
                System.out.println("Please input the string which contains 3 chars:");
                System.out.println("\tChar[1] : T - Title based query, B - Boolean based query");
                System.out.println("\tChar[2] : 1 - Testing, 2 - Training, 3 - Tuning");
                System.out.println("\tChar[3] : 7 - 2017 data, 8 - 2018 data");
                System.out.println("\tFor example: T17 - Analysing Title based query on 2017 testing data");
                System.out.println("Parameter:");
                parameter = in.next();

                // parameter validation
                if (parameter.length() != 3) {
                    System.out.println("The parameter is incorrect.");
                    return;
                } else {
                    queryType = parameter.charAt(0) + "";
                    dataType = parameter.charAt(1) + "";
                    dataYear = parameter.charAt(2) + "";
                    if (!queryType.equals("T") && !queryType.equals("B")) {
                        System.out.println("Query type is invalid !!! - " + queryType);
                        return;
                    }
                    if (!dataType.equals("1") && !dataType.equals("3") && !dataType.equals("2")) {
                        System.out.println("Data type is invalid !!!");
                        return;
                    }

                    if (!dataYear.equals("7") && !dataYear.equals("8")) {
                        System.out.println("Data year is invalid !!!");
                        return;
                    }

                }
            } else {
                System.out.println("------------------------------------------------------------------------");
                System.out.println("Please input the string which contains 2 chars:");
                System.out.println("\tChar[2] : 1 - Testing, 2 - Training ");
                System.out.println("\tChar[3] : 7 - 2017 data, 8 - 2018 data");
                System.out.println("\tFor example: 17 - Analysing on 2017 testing data");
                System.out.println("Parameter:");
                parameter = in.next();
                // parameter validation
                if (parameter.length() != 2) {
                    System.out.println("The parameter is incorrect.");
                    return;
                } else {
                    dataType = parameter.charAt(0) + "";
                    dataYear = parameter.charAt(1) + "";

                    if (!dataType.equals("1") && !dataType.equals("2")) {
                        System.out.println("Data type is invalid !!!");
                        return;
                    }
                    if (!dataYear.equals("7") && !dataYear.equals("8")) {
                        System.out.println("Data year is invalid !!!");
                        return;
                    }

                }
            }
        }


        // Tuned value for BM25
        double parameterB = 0.75;
        if (dataYear.equals("7") && queryType.equals("T")) // 2017 Title query
            parameterB = 0.00; // After tuning, no normalization
        else if (dataYear.equals("7") && queryType.equals("B")) // 2017 Boolean query
            parameterB = 0.00; // After tuning
        else if (dataYear.equals("8") && queryType.equals("T")) // 2018 Title query
            parameterB = 0.00; // After tuning, no normalization
        else // 2018 Boolean query
            parameterB = 0.00; // After tuning

        // For using to write or read
        String queryPath = path.get("output") + path.get(dataYear) + path.get("B") + path.get(dataType) + "boolean_terms.txt";

        switch (method) {
            case 2: //BM25
            case 1: //TF_IDF
            {
                if (method == 1) {
                    model = new TF_IDF();
                    model.setCollectionStatistics(index.getCollectionStatistics());
                } else {
                    System.out.println("Optimal b=" + parameterB);
                    model = new BM25();
                    model.setCollectionStatistics(index.getCollectionStatistics());
                    model.setParameter(parameterB);
                }


                // Build path and read topics
                topicPath = path.get("tar") + path.get(dataYear) + path.get(dataType.equals("3") ? "2" : dataType) + path.get("topics");
                topics = TopicParser.parse(topicPath, queryType, queryPath);


                if (dataType.equals("3")) // Tunning for only first topic and for only BM25
                {
                    // 0 <= b <= 1
                    // b=0 No normalization

                    System.out.println("------------------------------------------------------------------------");
                    System.out.println("Start tuning:");
                    double[] b = new double[]{
                            0.00, 0.25, 0.50, 0.75, 1.00
                    };

                    for (int i = 0; i < b.length; i++) {
                        TrecResults finalResults = new TrecResults();
                        finalResults.setRunName(model.getInfo());

                        for (int t = 0; t < topics.size(); t++) {
                            model.setParameter(b[i]);
                            Topic topic = topics.get(t);
                            TrecResults results = reranker.rerank("b=" + b[i] + ", topic: " + (t + 1) + "/" + topics.size(), topic, model, lex, invertedIndex, meta);
                            finalResults.getTrecResults().addAll(results.getTrecResults());
                        }
                        finalResults.write(path.get("output") + path.get(dataYear) + path.get(queryType) + path.get(dataType) + "run-" + model.getInfo() + ".res");
                    }
                } else { //Testing and training data
                    System.out.println("------------------------------------------------------------------------");
                    System.out.println("Start ranking [" + model.getInfo() + "] ");

                    TrecResults finalResults = new TrecResults();
                    finalResults.setRunName(model.getInfo());

                    for (int i = 0; i < topics.size(); i++) {
                        Topic topic = topics.get(i);
                        TrecResults results = reranker.rerank((i + 1) + "/" + topics.size(), topic, model, lex, invertedIndex, meta);
                        finalResults.getTrecResults().addAll(results.getTrecResults());
                    }
                    finalResults.write(path.get("output") + path.get(dataYear) + path.get(queryType) + path.get(dataType) + "run-" + model.getInfo() + ".res");
                }
                break;
            }
            case 3: // Borda
            case 4: // CombSUM
            case 5: // CombMNZ
            {
                Fusion fusion;
                if (method == 3)
                    fusion = new Borda();
                else if (method == 4)
                    fusion = new CombSUM();
                else
                    fusion = new CombMNZ();


                String externalFiles = path.get("runs") + path.get(dataYear);
                String internalFiles = path.get("output") + path.get(dataYear) + path.get(queryType) + path.get("1");
                List<TrecResults> docs = new ArrayList<>();
                Normaliser norm = new MinMax();

                // Only files
                FileFilter filter = new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return (pathname.isFile() && pathname.getName().toLowerCase().endsWith(".res"));
                    }
                };
                // Run files are reading
                File[] externalList = (new File(externalFiles)).listFiles(filter);
                int f = 1;
                if (externalList != null)
                    for (File file : externalList) {
                        System.out.println(f++ + ". File: " + file.getPath());
                        docs.add(new TrecResults(file.getPath()));
                    }

                File[] internalList = (new File(internalFiles)).listFiles(filter);
                f = 1;
                if (internalList != null)
                    // Result files are reading
                    for (File file : internalList) {
                        System.out.println(f++ + ". File: " + file.getPath());
                        docs.add(new TrecResults(file.getPath()));
                    }

                // Normalise the scores of each run.
                for (TrecResults doc : docs) {
                    norm.init(doc);
                    for (int j = 0; j < doc.getTrecResults().size(); j++) {
                        double normScore = norm.normalise(doc.getTrecResults().get(j));
                        doc.getTrecResults().get(j).setScore(normScore);
                    }
                }

                TrecResults finalResults = new TrecResults();
                finalResults.setRunName(fusion.toString());

                Set<String> topicList = docs.get(0).getTopics();
                for (String topic : topicList) {
                    List<TrecResults> topicResults = new ArrayList<>();
                    for (TrecResults r : docs) {
                        // Add results based on topic name
                        topicResults.add(new TrecResults(r.getTrecResults(topic)));
                    }

                    // Fuse the results together and write the new results list to disk.
                    finalResults.getTrecResults().addAll(fusion.Fuse(topicResults).getTrecResults());
                }
                finalResults.write(path.get("output") + path.get(dataYear) + path.get(queryType) + path.get("1") + path.get("fusion") + "run-" + fusion.toString() + ".res");
            }
            break;
            case 6: // IDF reduction
            case 7: // KLI reduction
            {
                String text = (method == 6 ? "IDF" : "KLI");
                System.out.println("------------------------------------------------------------------------");
                System.out.println("Start ranking by using reduction [" + text + "] ");

                // Set tuning value in BM25
                System.out.println("Optimal b=" + parameterB);
                model = new BM25();
                model.setCollectionStatistics(index.getCollectionStatistics());
                model.setParameter(parameterB);

                // Build path and read topics (only for testing data)
                topicPath = path.get("tar") + path.get(dataYear) + path.get("1") + path.get("topics");
                topics = TopicParser.parse(topicPath, queryType, queryPath);


                int[] rValues = new int[]{30, 50, 80};
                for (int r : rValues) {
                    TrecResults finalResults = new TrecResults();
                    finalResults.setRunName(model.getInfo());
                    for (int i = 0; i < topics.size(); i++) {
                        Topic topic = topics.get(i);
                        String[] reducedQueries;

                        if (method == 6) {
                            IDFReduction reduction = new IDFReduction();
                            reducedQueries = reduction.reduce(topic.getQueries(), r, index.getIndexRef());
                        } else {
                            KLIReduction reduction = new KLIReduction();
                            reducedQueries = reduction.reduce(topic.getQueries(), topic.getDocs(), r, index.getIndexRef());
                        }

                        String header = String.format("%d percent - %d/%d . Queries %d -> %d", r, i + 1, topics.size(), topic.getQueries().length, reducedQueries.length);
                        // Rank by using reduced queries
                        topic.setReductionQuery(reducedQueries);
                        TrecResults results = reranker.rerank(header, topic, model, lex, invertedIndex, meta);
                        finalResults.getTrecResults().addAll(results.getTrecResults());
                    }
                    finalResults.write(path.get("output") + path.get(dataYear) + path.get("T") + path.get(dataType) + "run-" + text + "-reduction-" + r + ".res");
                }


            }
            break;
            case 8: // Parse Boolean Queries
            {
                // Build path and read topics
                topicPath = path.get("tar") + path.get(dataYear) + path.get(dataType.equals("3") ? "2" : dataType) + path.get("topics");
                topics = TopicParser.parse(topicPath, queryType, queryPath);
                System.out.println("------------------------------------------------------------------------");
                System.out.println("Start parsing Boolean query ");
                BooleanQueryParser parser = new BooleanQueryParser();
                parser.parseBooleanQuery(topics, index.getIndexRef(), queryPath);
            }
            break;

        }

    }


}
