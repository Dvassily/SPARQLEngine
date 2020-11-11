package nosqlRDF.requests;

import java.util.List;

public class Request {
    public String getProjection() {
        return projection;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    private String projection;
    private List<Condition> conditions;
    @Override
    public String toString() {
	String result =  "SELECT ?" + projection + " WHERE {\n";

	for (Condition condition : conditions) {
	    result += ((condition.isIssubject())? "?" : "") + condition.getSubject();
	    result += ((condition.isIspredicate())? "?" : "") + condition.getPredicate();
	    result += ((condition.isIsobject())? "?" : "") + condition.getObject();
	    result += " . \n";
	}
	
	return result + "}\n";
    }

    String projection;
    List<Condition> conditions;

    public Request(String projection, List<Condition> conditions) {
        this.projection = projection;
        this.conditions = conditions;
    }
}
