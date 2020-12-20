package nosqlRDF.requests;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public boolean hasSameStructureAs(JoinableTriple triple) {
        boolean result = true;

        if (subjectVariable != null) {
            if (triple.subjectVariable != null) {
                result &= (subjectVariable.equals(triple.subjectVariable));
            }
        } else if (triple.subjectVariable == null) {
            result &= getSubject().equals(triple.getSubject());
        }

        if (predicateVariable != null) {
            if (triple.predicateVariable != null) {
                result &= (predicateVariable.equals(triple.predicateVariable));
            }
        } else if (triple.predicateVariable == null) {
            result &= getPredicate().equals(triple.getPredicate());
        }

        if (objectVariable != null) {
            if (triple.objectVariable != null) {
                result &= (objectVariable.equals(triple.objectVariable));
            }
        } else if (triple.objectVariable == null) {
            result &= getObject().equals(triple.getObject());
        }

        return result;
    }

    public boolean hasCommonVariablesWith(JoinableTriple triple) {
        return ! (commonVariables(triple)).isEmpty();
    }

    public boolean allCommonVariablesEquals(JoinableTriple triple) {
        Set<String> commonVariables = commonVariables(triple);

        int i = 0;

        for (Map.Entry<String, Boolean> entry : equalityTable(triple).entrySet()) {
            if (entry.getValue()) {
                ++i;
            }
        }

        return i == commonVariables.size();
    }

    public boolean hasAtLeastOneCommonValueWith(JoinableTriple triple) {
        int i = 0;

        for (Map.Entry<String, Boolean> entry : equalityTable(triple).entrySet()) {
            if (entry.getValue()) {
                ++i;
            }
        }

        return i >= 1;
    }

    public Set<String> commonVariables(JoinableTriple triple) {
        Map<String, String> variables = variables();
        Map<String, String> tripleVariables = triple.variables();
        Set<String> commonVariables = new HashSet<>(variables.keySet());
        commonVariables.retainAll(tripleVariables.keySet());

        return commonVariables;
    }

    private Map<String, Boolean> equalityTable(JoinableTriple triple) {
        Map<String, Boolean> equalityTable = new HashMap<>();
        Map<String, String> variables = variables();
        Map<String, String> tripleVariables = triple.variables();
        Set<String> commonVariables = new HashSet<>(variables.keySet());

        for (String variable : commonVariables) {
            if (variables.get(variable).equals(tripleVariables.get(variable))) {
                equalityTable.put(variable, true);
            } else {
                equalityTable.put(variable, false);
            }
        }

        return equalityTable;
    }

    public boolean containsValue(String variable, String value) {
        Map<String, String> variables = variables();

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            if (entry.getKey().equals(variable) && entry.getValue().equals(value)) {
                return true;
            }
        }

        return false;
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
