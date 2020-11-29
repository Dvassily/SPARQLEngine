package nosqlRDF;

import java.lang.Exception;

public class InvalidQueryArgumentException extends Exception {
    private String argument;

    public InvalidQueryArgumentException(String argument) {
        super("Argument " + argument + " is unknown");

        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
