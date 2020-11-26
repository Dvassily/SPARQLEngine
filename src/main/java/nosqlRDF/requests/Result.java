package nosqlRDF.requests;

import java.util.Map;
import java.util.HashMap;

public class Result {
    private Map<String, String> columns = new HashMap<>();

    public void addColumn(String identifier, String value) {
        columns.put(identifier, value);
    }

    public String getValue(String identifier) {
        return columns.get(identifier);
    }

    @Override
    public int hashCode() {
        return columns.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        final Result other = (Result) obj;
        return columns.equals(other.columns);
    }

    @Override
    public String toString() {
        return columns.toString();
    }
}
