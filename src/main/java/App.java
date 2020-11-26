import com.beust.jcommander.ParameterException;
import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.utils.Arguments;
import nosqlRDF.utils.BenchmarkEngine;
import nosqlRDF.Runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class App {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();

        try {
            arguments.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }

        if (arguments.getDataPath() == null) {
            return;
        }

        List<String> queryFiles = findQueryFiles(arguments.getRequestPath());

        // WorkloadOutputWriter workloadOutputWriter = new WorkloadOutputWriter("workload-stats.csv"));
        // workloadoutputwriter.insertHeader();

        for (String queryFile : queryFiles) {
            try {
                Runner runner = new Runner(arguments.getDataPath(), queryFile, arguments.getOutputPath(), arguments.isVerbose(), true);

                try {
                    runner.run();
                } catch (IOException e) {
                    System.err.println("Failed to load request file : " + e.getMessage());
                }
            } catch (IOException e) {
                System.err.println("Failed to load data file : '" + e.getMessage());
            }
        }

        // workloadOutputWriter.close();
    }

    private static List<String> findQueryFiles(String queryDirectory) {
        List<String> filesPath = new ArrayList<>();

        for (final File fileEntry : new File(queryDirectory).listFiles()) {
            if (! fileEntry.isDirectory()) {
                filesPath.add(fileEntry.getAbsolutePath());
            }
        }

        return filesPath;
    }


}
