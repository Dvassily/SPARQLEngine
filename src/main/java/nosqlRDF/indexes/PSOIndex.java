package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgument;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;

public class PSOIndex extends AbstractHexastoreIndex {
    public PSOIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findSubjectObject(String predicate) throws InvalidQueryArgument {
	return findYZ(predicate);
    }

    public Set<RDFTriple> findObject(String predicate, String subject) throws InvalidQueryArgument {
	return findZ(predicate, subject);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(predicateKey, subjectKey, objectKey);
    }

}
