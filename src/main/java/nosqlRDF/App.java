package nosqlRDF;

import com.beust.jcommander.ParameterException;
import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.utils.Arguments;
import nosqlRDF.utils.BenchmarkEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class App {

    public static List<String> listFilesForFolder(final File folder) {
        List<String> filesPath = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                filesPath.add(fileEntry.getAbsolutePath());
            }
        }

        return filesPath;
    }

    public static void main(String[] args) {
        Arguments arguments = new Arguments();
        Logger logger = Logger.getLogger(App.class.getName());

        try {
            arguments.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
        }

        if (arguments.getDataPath() == null) {
            return;
        }

        SPARQLEngine engine = new SPARQLEngine();

        BenchmarkEngine benchmarkEngine = new BenchmarkEngine("Dictionary and hexastore indexes construction");
        benchmarkEngine.begin();

        logger.info("Started Parsing data File");

        try {
            engine.parseData(arguments.getDataPath());
        } catch (FileNotFoundException e) {
            logger.warning("Error data file not found");
            return;
        }

        benchmarkEngine.end();

        engine.initDictionaryAndIndexes();
        logger.info("End parsing data file and contructing indexes");

        BenchmarkEngine requestsBenchEngine = new BenchmarkEngine("Request parsing and execution");
        requestsBenchEngine.begin();

        ArrayList<Long> meanExecutionTime = new ArrayList<>();

        try {

            logger.info("Started executing requests : ");

            for (String filePath : listFilesForFolder(new File(arguments.getRequestPath()))) {

                logger.info("================================   Started executing request file : " + filePath + "================================ ");

                List<Request> requests = new SPARQLRequestParser(filePath).loadQueries();

                for (Request request : requests) {
                    BenchmarkEngine requestBenchEngine = new BenchmarkEngine("Request benchmark");

                    logger.info("================ Execution of request : " + "================");
                    logger.info(request.toString());
                    Set<RDFTriple> triples = engine.query(request);

                    requestBenchEngine.begin();

                    logger.info("Nombre de résultats : " + triples.size() + "\n\n");

                    requestBenchEngine.end();
                    logger.info("Temp d'execution de la requete : " + requestBenchEngine.getDuration() + "ms");
                    meanExecutionTime.add(requestBenchEngine.getDuration());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to open request file : '" + arguments.getDataPath() + "' : " + e.getMessage());
        } finally {
            requestsBenchEngine.end();
        }


        System.out.println("Benchmarks : ");
        logger.info(" * " + benchmarkEngine);
        logger.info(" * " + requestsBenchEngine);

        long sumExecutionTime = 0;
        for(Long executionTime: meanExecutionTime) {
            sumExecutionTime += executionTime;
        }

        logger.info(" Moyenne d'exeuction des requetes : " + (double) sumExecutionTime / meanExecutionTime.size()  + "ms");
    }
}
