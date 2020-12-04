package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.Result;
import nosqlRDF.utils.BenchmarkEngine;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionLocal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.core.DatasetOne;

public class Runner
{
    private String dataPath;
    private String queryFile;
    private List<Request> queries = new ArrayList<>();
    private SPARQLEngine engine;
    private Logger logger = Logger.getLogger(Runner.class.getName());
    private ArrayList<Long> executionDurations = new ArrayList<>();
    // private WorkloadOutputWriter workloadOutputWriter;
    private boolean verbose;
    private boolean check;
    private RDFConnection connection;

    public Runner(String dataPath, String queryFile, String outputPath, boolean verbose, boolean check) throws IOException {
        this.dataPath = dataPath;
        this.queryFile = queryFile;
        this.queries = new SPARQLRequestParser(queryFile).loadQueries();
        // this.outputPath = outputPath;
        this.verbose = verbose;
        this.check = check;

        if (check) {
            Model model = ModelFactory.createDefaultModel();
            model.read(dataPath, "RDF/XML");
            DatasetOne ds = new DatasetOne(model);
            connection = new RDFConnectionLocal(ds);
        }
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
        writeTrace(query.getText());
        BenchmarkEngine requestBenchmarkEngine = new BenchmarkEngine("Request benchmark");
        requestBenchmarkEngine.begin();
        Result result = engine.query(query);
        requestBenchmarkEngine.end();
        writeTrace("Number of results : " + result.count());
        writeTrace("Execution duration : " + requestBenchmarkEngine.getDuration() + "ms");

        if (check) {
            validateResult(query.getText(), result);
        }

        System.out.println(result.toString());

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

    public void validateResult(String query, Result results) {
        Result expectedResults = new Result();

        connection.querySelect(query, (qs)-> {
                Iterator<String> iterator = qs.varNames();

                while (iterator.hasNext()) {
                    String variable = iterator.next();

                    expectedResults.add(variable, qs.get(variable).toString());
                }
        });

        if (! expectedResults.equals(results)) {
            throw new CheckAgainstOracleFailureException(query, expectedResults, results, engine);
        }
    }
}
