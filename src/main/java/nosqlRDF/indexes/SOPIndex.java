package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgument;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;

public class SOPIndex extends AbstractHexastoreIndex {
    public SOPIndex(Dictionary dictionary) {
	super(dictionary);
    }
    
    public Set<RDFTriple> findObjectPredicate(String subject) throws InvalidQueryArgument {
	return findYZ(subject);
    }

    public Set<RDFTriple> findPredicate(String subject, String object) throws InvalidQueryArgument {
	return findZ(subject, object);
    }

    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(subjectKey,objectKey, predicateKey);
    }

}
