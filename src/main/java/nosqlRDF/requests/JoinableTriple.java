package nosqlRDF.requests;

import java.util.HashMap;
import java.util.Map;

import nosqlRDF.datas.RDFTriple;

public class JoinableTriple extends RDFTriple
{
    private String subjectVariable = null;
    private String predicateVariable = null;
    private String objectVariable = null;

	public JoinableTriple(RDFTriple triple) {
		super(triple.getSubject(), triple.getPredicate(), triple.getObject());
	}

    public void setSubjectVariable(String variableName) {
        this.subjectVariable = variableName;
    }

    public void setPredicateVariable(String variableName) {
        this.predicateVariable = variableName;
    }

    public void setObjectVariable(String variableName) {
        this.objectVariable = variableName;
    }

    public String getSubjectVariable() {
        return subjectVariable;
    }

    public String getPredicateVariable() {
        return predicateVariable;
    }

    public String getObjectVariable() {
        return objectVariable;
    }

    public Map<String, String> variables() {
        Map<String, String> result = new HashMap<>();

        if (subjectVariable != null) {
            result.put(subjectVariable, getSubject());
        }

        if (predicateVariable != null) {
            result.put(predicateVariable, getPredicate());
        }

        if (objectVariable != null) {
            result.put(objectVariable, getObject());
        }

        return result;
    }

    public String toString() {
        String result = "(";

        if (subjectVariable != null) {
            result += subjectVariable + "=";
        }

        result += getSubject() + ", ";

        if (predicateVariable != null) {
            result += predicateVariable + "=";
        }

        result += getPredicate() + ", ";

        if (objectVariable != null) {
            result += objectVariable + "=";
        }

        result += getObject();

        return result + ")";
    }
}
