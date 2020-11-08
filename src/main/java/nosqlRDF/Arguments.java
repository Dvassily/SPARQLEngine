package nosqlRDF;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

public class Arguments {
    @Parameter(names = {"--data" , "-d"},
	       description = "Provide path towards the .rdfxml data file",
	       validateWith = SourceValidator.class,
	       required = true)
    private String data = null;

    public void parse(String[] args) {
	JCommander.newBuilder()
	    .addObject(this)
	    .build()
	    .parse(args);
    }

    public String getDataPath() {
	return data;
    }
}

