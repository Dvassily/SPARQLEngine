package nosqlRDF.utils;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Arguments {
    @Parameter(names = {"--data" , "-d"},
	       description = "Provide path towards the .rdfxml data file",
	       validateWith = SourceValidator.class,
	       required = true)
	       private String dataPath = null;

    @Parameter(names = {"--requests" , "-r"},
	       description = "Provide path towards the .queryset request file",
	       validateWith = SourceValidator.class,
	       required = true)
	       private String requestPath = null;

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

