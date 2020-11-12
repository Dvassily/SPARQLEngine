package nosqlRDF.utils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Arguments {
    @Parameter(names = {"--data" , "-d"},
	       description = "Provide path containing data",
	       validateWith = SourceValidator.class,
	       required = true)
	       private String dataPath = null;

    @Parameter(names = {"--queries" , "-q"},
	       description = "Provide path towards the folder containing queries",
	       validateWith = SourceValidator.class,
	       required = true)
	       private String requestPath = null;

	@Parameter(names = {"--verbose" , "-v"},
			description = "verbose option to show execution log",
			validateWith = SourceValidator.class,
			required = true)
	private String verbose = null;

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
}

