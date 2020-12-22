package nosqlRDF;

import nosqlRDF.requests.Request;
import nosqlRDF.requests.Result;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.utils.Arguments;
import nosqlRDF.utils.BenchmarkEngine;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionLocal;
import org.apache.jena.sparql.core.DatasetOne;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class Runner {
    private final String dataPath;
    private final String queryFile;
    private List<Request> queries = new ArrayList<>();
    private SPARQLEngine engine;
    private final Logger logger = Logger.getLogger(Runner.class.getName());
    // private WorkloadOutputWriter workloadOutputWriter;
    private final boolean verbose;
    private final boolean check;



    // logging time
    private final ArrayList<Long> executionDurations = new ArrayList<>();
    private long requestParsingTime;
    private long dictionaryCreationTime;
    private long workloadExecutionDuration;

    private FileWriter outputFile;

    private Arguments arguments;

    private RDFConnection connection;

    public Runner(String dataPath, String queryFile, String outputPath, boolean verbose, boolean check) throws IOException {
        this.dataPath = dataPath;
        this.queryFile = queryFile;
        this.queries = new SPARQLRequestParser(queryFile).loadQueries();
        // this.outputPath = outputPath;
        this.verbose = verbose;
        this.check = check;

        this.arguments = null;

        if (check) {
            Model model = ModelFactory.createDefaultModel();
            model.read(new File(dataPath).toURL().toString(), "RDF/XML");
            DatasetOne ds = new DatasetOne(model);
            connection = new RDFConnectionLocal(ds);
        }
    }

    public Runner(String queryFile, Arguments arguments) throws IOException {
        this(arguments.getDataPath(), queryFile, arguments.getOutputPath(), arguments.isVerbose(), arguments.isJena());
        this.arguments = arguments;
    }

    public void run() throws IOException {
        Thread thread;

        if(arguments.logMemoryUsage) {
            thread = new Thread(){
                public void run(){


                    while(true) {
                        try {

                            String line = String.format("%s\n", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
                            FileWriter csvWriter = new FileWriter(arguments.getOutputPath() + "/output-memory-usage.txt", true);

                            csvWriter.append(line);

                            csvWriter.flush();
                            csvWriter.close();

                            Thread.sleep(200);

                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                }
            };

            thread.start();
        }



        writeTrace("Started the execution of query list '" + queryFile + "' with dataset '" + dataPath + "')");
        writeTrace("Started : Data file parsing and indexes construction :");
        engine = new SPARQLEngine();
        parseData();

        BenchmarkEngine createDictionaryBenchmark = new BenchmarkEngine("create dict benchmark");
        createDictionaryBenchmark.begin();
        engine.initDictionaryAndIndexes();

        createDictionaryBenchmark.end();
        dictionaryCreationTime = createDictionaryBenchmark.getDuration();

        writeTrace("Data file parsing and indexes construction : Done !");
        writeTrace("Started : requests execution");

        if(arguments.isShuffle()) {
            Collections.shuffle(queries);
        }

        if(arguments.getWarm() != -1) {
            writeTrace("Warm engine");
            Collections.shuffle(queries);
            List<Request> warmupQueries = queries.subList(0, (int)(queries.size() * (arguments.getWarm() * queries.size())));

            for (Request query : warmupQueries) {
                executeQuery(query);
            }
            writeTrace("Warm engine: done !");
        }

        BenchmarkEngine requestBenchmarkEngine = new BenchmarkEngine("Request benchmark");
        requestBenchmarkEngine.begin();

        for (Request query : queries) {
            executionDurations.add(executeQuery(query));
        }

        requestBenchmarkEngine.end();
        workloadExecutionDuration = requestBenchmarkEngine.getDuration();

        if(arguments.getOutputPath() != null) {
            exportOutput();
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
        boolean resultIsValid = false;

        // jena optin
        if (check) {
            resultIsValid = validateResult(query.getText(), result);
        }

        if(arguments.isExportQueryStats()) {
            exportQueryStats(requestBenchmarkEngine.getDuration(), result.count(), query.isStarQuery());
        }

        if(arguments.isExportQueryResults()) {
            exportQueryResults(query.getText(), result, resultIsValid);
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
        requestParsingTime = initEngine.getDuration();
    }

    public double meanExecutionDuration() {
        long sum = 0;

        for (long duration : executionDurations) {
            sum += duration;
        }

        return ((double) sum) / executionDurations.size();
    }

    public void writeTrace(String message) {
        if (verbose) {
            logger.info(message);
        }
    }

    public boolean validateResult(String query, Result results) {
        Result expectedResults = new Result();

        connection.querySelect(query, (qs) -> {
            Iterator<String> iterator = qs.varNames();

            while (iterator.hasNext()) {
                String variable = iterator.next();

                expectedResults.add(variable, qs.get(variable).toString());
            }
        });

        return expectedResults.equals(results);
    }



    private void exportOutput() {
        String line = String.format("%s,%s,%d,%d,%d,%d,%d\n", dataPath, queryFile, engine.entityCount(),
                queries.size(), requestParsingTime, dictionaryCreationTime,
                6,  workloadExecutionDuration  );

        try {
            FileWriter csvWriter = new FileWriter(arguments.getOutputPath() + "/output.csv", true);

            csvWriter.append(line);

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportQueryStats(long duration, long count, boolean isStarQuery) {
        String line = String.format("%d,%d,%b\n",  duration, count, isStarQuery);


        try {
            FileWriter csvWriter = new FileWriter(arguments.getOutputPath() + "/output_query_stats.csv", true);

            csvWriter.append(line);

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportQueryResults(String query, Result result, boolean resultIsValid) {
        String line = query + ",";
        Set<String> variables = result.variables();

        if (variables.isEmpty()) {
            line = line.substring(0, line.length() - 1) + ",";
        } else {
            for (String variable : variables) {
                line += variable + ",";

                for (String value : result.values(variable)) {
                    line += value + ",";
                }
            }
        }

        if (! check) {
            line = line.substring(0, line.length() - 1);
        } else {
            line += resultIsValid;
        }

        line += "\n";

        try {
            FileWriter csvWriter = new FileWriter(arguments.getOutputPath() + "/output_query_results.csv", true);

            csvWriter.append(line);

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
