package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgumentException;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;

import java.util.Set;
import java.math.BigInteger;


public class SPOIndex extends AbstractHexastoreIndex {
    public SPOIndex(Dictionary dictionary) {
	super(dictionary);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     *
     * @param subject The input subject
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findObject(String subject, String predicate) throws InvalidQueryArgumentException {
	return findZ(subject, predicate);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject
     *
     * @param subject The input subject
     */
    public Set<RDFTriple> findPredicateObject(String subject) throws InvalidQueryArgumentException {
	return findYZ(subject);
    }

    /**
     * 
     *
     * @param subject The input subject
     */
    @Override
    protected BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey) {
	return composeKeyInternal(subjectKey, predicateKey, objectKey);
    }

}
