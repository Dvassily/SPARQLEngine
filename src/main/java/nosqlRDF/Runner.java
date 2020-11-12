package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.requests.Request;
import nosqlRDF.utils.BenchmarkEngine;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Runner
{
    private String dataPath;
    private String queryFile;
    private List<Request> queries = new ArrayList<>();
    private SPARQLEngine engine;
    private Logger logger = Logger.getLogger(App.class.getName());
    private ArrayList<Long> executionDurations = new ArrayList<>();

    public Runner(String dataPath, String queryFile, String outputPath) throws IOException {
        this.dataPath = dataPath;
        this.queryFile = queryFile;
        this.queries = new SPARQLRequestParser(queryFile).loadQueries();

        FileHandler handler = new FileHandler(outputPath);
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
    }

    public void run() {
        logger.info("Started the execution of query list '" + queryFile + "' with dataset '" + dataPath + "')");
        logger.info("Started : Data file parsing and indexes construction : Done !");
        engine = new SPARQLEngine();
        parseData();
        engine.initDictionaryAndIndexes();
        logger.info("Data file parsing and indexes construction : Done !");
        logger.info("Started : requests execution");
        for (Request query : queries) {
            executionDurations.add(executeQuery(query));
        }
        logger.info("request execution : Done !");

    }

    private long executeQuery(Request query) {
        logger.info("Execution of request : ");
        logger.info(query.toString());
        BenchmarkEngine requestBenchmarkEngine = new BenchmarkEngine("Request benchmark");
        requestBenchmarkEngine.begin();
        Set<RDFTriple> triples = engine.query(query);
        requestBenchmarkEngine.end();
        logger.info("Number of results : " + triples.size());
        logger.info("Execution duration : " + requestBenchmarkEngine.getDuration() + "ms");

        return requestBenchmarkEngine.getDuration();
    }

    private void parseData() {
        BenchmarkEngine initEngine = new BenchmarkEngine("Request parsing and execution");
        initEngine.begin();

        try {
            engine.parseData(dataPath);
        } catch (FileNotFoundException e) {
            logger.warning("Error : data file not found");
            return;
        }

        initEngine.end();
    }

    public double meanExecutionDuration() {
        long sum = 0;

        for(long duration : executionDurations) {
            sum += duration;
        }

        return ((double) sum) / executionDurations.size();
    }
}
