package nosqlRDF;

import java.util.Set;
import java.math.BigInteger;

public class OPSIndex extends AbstractHexastoreIndex {
    public OPSIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findPredicateSubject(String object) {
	return findYZ(object);
    }

    public Set<RDFTriple> findSubject(String object, String predicate) {
	return findZ(object, predicate);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(objectKey, predicateKey ,subjectKey);
    }

}