package nosqlRDF;

import nosqlRDF.requests.Result;
import java.lang.RuntimeException;
import java.util.Set;

public class CheckAgainstOracleFailureException extends RuntimeException {
    public CheckAgainstOracleFailureException(String query, Set<Result> expected, Set<Result> result) {
        super("Check against oracle failed for query : " + query
              + "\nExpected was : " + expected + "(" + expected.size() + ")"
              + "\nResult is : " + result + "(" + result.size() + ")");
    }
}
