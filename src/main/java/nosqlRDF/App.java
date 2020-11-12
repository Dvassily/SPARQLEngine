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
    public static void main(String[] args) {
        Arguments arguments = new Arguments();

        try {
            arguments.parse(args);
        } catch (ParameterException exception) {
            System.err.println(exception.getMessage());
        }

        if (arguments.getDataPath() == null) {
            return;
        }

        try {
        List<String> queryFiles = findQueryFiles(arguments.getRequestPath());

        for (String queryFile : queryFiles) {
            new Runner(arguments.getDataPath(), queryFile, "output.txt").run();
        }

        } catch (IOException e) {
            System.err.println("Failed to open request file : '" + arguments.getDataPath() + "' : " + e.getMessage());
        }
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
