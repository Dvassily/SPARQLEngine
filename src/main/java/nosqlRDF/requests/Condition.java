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

    public boolean isIssubject() {
        return Issubject;
    }

    public boolean isIspredicate() {
        return Ispredicate;
    }

    public boolean isIsobject() {
        return Isobject;
    }

    private boolean Issubject;
    private boolean Ispredicate;
    private boolean Isobject;

    public Condition(String subjet, String predicate, String object) {
        this.subject = subjet;
        this.predicate = predicate;
        this.object = object;
    }
}
