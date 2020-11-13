package nosqlRDF.utils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Arguments {
    @Parameter(names = {"-data" , "-d"},
               description = "Provides path containing data",
               required = true)
               private String dataPath = null;

    @Parameter(names = {"-queries" , "-q"},
               description = "Provides path towards the folder containing queries",
               required = true)
               private String requestPath = null;

    @Parameter(names = {"-output" , "-o"},
               description = "Provides path towards the output directory")
               private String outputPath = null;

	@Parameter(names = {"-verbose" , "-v"},
               description = "verbose option to show execution log")
               private boolean verbose = false;

    public void parse(String[] args) {
	JCommander.newBuilder()
	    .addObject(this)
	    .build()
	    .parse(args);
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public boolean isVerbose() {
        return verbose;
    }
}
