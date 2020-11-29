package nosqlRDF.requests;

import java.util.List;

public class Request {
    private List<Condition> conditions;
    private List<String> projection;
    private String text;

    public Request(List<String> projection, List<Condition> conditions, String text) {
        this.projection = projection;
        this.conditions = conditions;
        this.text = text;
    }

    public List<String> getProjection() {
        return projection;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        String projectionStr = "";

        for (String p : projection) {
            projectionStr += p + " ";
        }

        String result =  "SELECT ?" + projectionStr + "WHERE {\n";

        for (Condition condition : conditions) {
            result += ((condition.subjectIsVariable())? "?" : "") + condition.getSubject() + " ";
            result += ((condition.predicateIsVariable())? "?" : "") + condition.getPredicate() + " ";
            result += ((condition.objectIsVariable())? "?" : "") + condition.getObject();
            result += " . \n";
        }
	
        return result + "}\n";
    }
}
