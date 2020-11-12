package nosqlRDF;

import java.lang.Exception;

public class InvalidQueryArgument extends Exception {
    public InvalidQueryArgument(String argument) {
	super("Argument " + argument + " is unknown");
    }
}
