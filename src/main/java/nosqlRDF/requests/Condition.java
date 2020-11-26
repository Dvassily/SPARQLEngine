package nosqlRDF.requests;

public class Condition {
    private String subject;
    private String predicate;
    private String object;
    private boolean subjectIsVariable;
    private boolean predicateIsVariable;
    private boolean objectIsVariable;

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public boolean subjectIsVariable() {
        return subjectIsVariable;
    }

    public boolean predicateIsVariable() {
        return predicateIsVariable;
    }

    public boolean objectIsVariable() {
        return objectIsVariable;
    }

    public void setSubject(String value, boolean variable) {
        subject = value;
        subjectIsVariable = variable;
    }

    public void setPredicate(String value, boolean variable) {
        predicate = value;
        predicateIsVariable = variable;
    }

    public void setObject(String value, boolean variable) {
        object = value;
        objectIsVariable = variable;
    }

}
