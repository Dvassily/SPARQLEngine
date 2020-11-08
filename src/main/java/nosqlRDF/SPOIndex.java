package nosqlRDF;

import java.util.Set;
import java.math.BigInteger;

public class SPOIndex extends AbstractHexastoreIndex {
    public SPOIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findPredicateObject(String subject) {
	return findYZ(subject);
    }

    public Set<RDFTriple> findObject(String subject, String predicate) {
	return findZ(subject, predicate);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(subjectKey, predicateKey, objectKey);
    }

}
