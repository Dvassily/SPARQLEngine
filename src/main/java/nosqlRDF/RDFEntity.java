package nosqlRDF;

public class RDFEntity {
    private String value;
    private RDFEntityType type;

    public RDFEntity(String value, RDFEntityType type) {
	this.value = value;
	this.type = type;
    }

    public String getValue() {
	return value;
    }
    
    public RDFEntityType getType() {
	return type;
    }
    
    public enum RDFEntityType {
	RDFSubject, RDFPredicate, RDFObject
    }
}

