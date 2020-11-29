package nosqlRDF;

import nosqlRDF.requests.Result;
import java.lang.RuntimeException;

public class CheckAgainstOracleFailureException extends RuntimeException {
    public CheckAgainstOracleFailureException(String query, Result expected, Result result) {
        super("Check against oracle failed for query : " + query
              + "\nExpected was : " + expected
              + "\nResult is : " + result);
    }
}
