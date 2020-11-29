package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgumentException;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;

public class POSIndex extends AbstractHexastoreIndex {
    public POSIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findObjectSubject(String predicate) throws InvalidQueryArgumentException {
	return findYZ(predicate);
    }

    public Set<RDFTriple> findSubject(String predicate, String object) throws InvalidQueryArgumentException {
	return findZ(predicate, object);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(predicateKey, objectKey, subjectKey);
    }

}
