package nosqlRDF.requests;

public class Condition {
    private String subject;
    private String predicat;
    private String object;

    public String getSubject() {
        return subject;
    }

    public String getPredicat() {
        return predicat;
    }

    public String getObject() {
        return object;
    }

    public boolean isIssubject() {
        return Issubject;
    }

    public boolean isIspredicat() {
        return Ispredicat;
    }

    public boolean isIsobject() {
        return Isobject;
    }

    private boolean Issubject;
    private boolean Ispredicat;
    private boolean Isobject;

    public Condition(String subjet, String predicat, String object) {
        this.subject = subjet;
        this.predicat = predicat;
        this.object = object;
    }


}
