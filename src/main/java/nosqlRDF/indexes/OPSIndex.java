package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgumentException;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;

public class OPSIndex extends AbstractHexastoreIndex {
    public OPSIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findPredicateSubject(String object, String projection) throws InvalidQueryArgumentException {
	return findYZ(object);
    }

    public Set<RDFTriple> findSubject(String object, String predicate, String projection) throws InvalidQueryArgumentException {
	return findZ(object, predicate);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(objectKey, predicateKey, subjectKey);
    }

}
