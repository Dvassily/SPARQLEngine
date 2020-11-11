package nosqlRDF.datas;

/**
 * Class that represent an RDF triple
 * Its objects each contains a subject, a predicate and an object
 */
public class RDFTriple {
    private String subject;
    private String predicate;
    private String object;

    public RDFTriple(String subject, String predicate, String object) {
	this.subject = subject;
	this.predicate = predicate;
	this.object = object;

    }

    public String getSubject() {
	return subject;
    }

    public String getPredicate() {
	return predicate;
    }

    public String getObject() {
	return object;
    }

    @Override
    public String toString() {
	return "{" + subject + ", " + predicate + ", " + object + "}";
    }
}
