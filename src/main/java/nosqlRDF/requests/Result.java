package nosqlRDF.requests;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class Result {
    private Map<String, Set<String>> content = new HashMap<>();

    public void add(String variable, String value) {
        if (! content.containsKey(variable)) {
            content.put(variable, new HashSet<>());
        }

        content.get(variable).add(value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Result other = (Result) obj;
        if (content == null) {
            if (other.content != null)
                return false;
        } else if (! content.equals(other.content))
            return false;
        return true;
    }

    @Override
    public String toString() {
        String result = "{";

        int i = 0;
        for (Map.Entry<String, Set<String>> entry : content.entrySet()) {
            result += entry.getKey() + " : [ ";

            int j = 0;
            for (String value : entry.getValue()) {
                result += value;

                if (j < entry.getValue().size() - 1) {
                    result += ", ";
                }

                ++j;
            }

            result += "]";

            if (i < content.keySet().size() - 1) {
                result += "; ";
            }

            ++i;
        }

        result += "}";

        return result;
    }

}
