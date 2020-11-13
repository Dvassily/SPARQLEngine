package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.requests.Request;
import nosqlRDF.utils.BenchmarkEngine;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runner
{
    private String dataPath;
    private String queryFile;
    private String outputPath;
    private List<Request> queries = new ArrayList<>();
    private SPARQLEngine engine;
    private Logger logger = Logger.getLogger(Runner.class.getName());
    private ArrayList<Long> executionDurations = new ArrayList<>();
    private String workloadOutputFile;
    private String statsOutputFile;
    private String resultsOutputFile;
    // private WorkloadOutputWriter workloadOutputWriter;
    private boolean verbose;

    public Runner(String dataPath, String queryFile, String outputPath, boolean verbose) throws IOException {
        this.dataPath = dataPath;
        this.queryFile = queryFile;
        this.queries = new SPARQLRequestParser(queryFile).loadQueries();
        this.outputPath = outputPath;
        this.verbose = verbose;
    }

    public void run() throws IOException {
        writeTrace("Started the execution of query list '" + queryFile + "' with dataset '" + dataPath + "')");
        writeTrace("Started : Data file parsing and indexes construction : Done !");
        engine = new SPARQLEngine();
        parseData();
        engine.initDictionaryAndIndexes();
        writeTrace("Data file parsing and indexes construction : Done !");
        writeTrace("Started : requests execution");

        for (Request query : queries) {
            executionDurations.add(executeQuery(query));
        }

        writeTrace("request execution : Done !");
    }

    private long executeQuery(Request query) {
        writeTrace("Execution of request : ");
        writeTrace(query.toString());
        BenchmarkEngine requestBenchmarkEngine = new BenchmarkEngine("Request benchmark");
        requestBenchmarkEngine.begin();
        Set<RDFTriple> triples = engine.query(query);
        requestBenchmarkEngine.end();
        writeTrace("Number of results : " + triples.size());
        writeTrace("Execution duration : " + requestBenchmarkEngine.getDuration() + "ms");

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

    public void writeTrace(String message) {
        if (verbose) {
            logger.info(message);
        }
    }
}
