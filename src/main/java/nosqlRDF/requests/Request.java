package nosqlRDF.requests;

import java.util.List;

public class Request {
    public String getProjection() {
        return projection;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    String projection;
    List<Condition> conditions;

    public Request(String projection, List<Condition> conditions) {
        this.projection = projection;
        this.conditions = conditions;
    }
}
