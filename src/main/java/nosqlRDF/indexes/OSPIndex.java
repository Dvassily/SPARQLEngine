package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgumentException;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;

public class OSPIndex extends AbstractHexastoreIndex {
    public OSPIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findSubjectPredicate(String object) throws InvalidQueryArgumentException {
	return findYZ(object);
    }

    public Set<RDFTriple> findPredicate(String object, String subject) throws InvalidQueryArgumentException {
	return findZ(object, subject);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(objectKey, subjectKey, predicateKey );
    }

}
