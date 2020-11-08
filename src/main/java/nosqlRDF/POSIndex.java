package nosqlRDF;

import java.util.Set;
import java.math.BigInteger;

public class POSIndex extends AbstractHexastoreIndex {
    public POSIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findObjectSubject(String predicate) {
	return findYZ(predicate);
    }

    public Set<RDFTriple> findSubject(String predicate, String object) {
	return findZ(predicate, object);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(predicateKey, objectKey, subjectKey);
    }

}
