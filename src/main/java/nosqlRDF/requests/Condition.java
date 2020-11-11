package nosqlRDF.requests;

public class Condition {

    private String subject;
    private String predicate;
    private String object;

    public String getSubject() {
        return subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public String getObject() {
        return object;
    }

    public boolean isSubjectVariable() {
        return SubjectVariable;
    }

    public boolean isPredicateVariable() {
        return PredicatVariable;
    }

    public boolean isObjectVariable() {
        return ObjectVariable;
    }

    private boolean SubjectVariable;
    private boolean PredicateVariable;
    private boolean ObjectVariable;

    public Condition(String subjet, String predicate, String object,
    boolean subjectVariable,boolean predicateVariable, boolean objectVariable) {
        this.subject = subjet;
        this.predicate = predicate;
        this.object = object;
        this.SubjectVariable=subjectVariable;
        this.PredicateVariable=predicateVariable;
        this.ObjectVariable=objectVariable;
    }


}
