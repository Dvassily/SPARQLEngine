package nosqlRDF.indexes;

import nosqlRDF.InvalidQueryArgument;

import nosqlRDF.datas.Dictionary;
import nosqlRDF.datas.RDFTriple;
import nosqlRDF.InvalidQueryArgument;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.math.BigInteger;

/**
 * Abstract class that represent an abstract RDF hexastore index (SPO, PSO, OPS, etc.)
 *
 * It contains a sorted dictionary (TreeMap), in which the value is a RDF triple and the key is a positive integer key computed with 
 * the triple values for its three field (subject, predicate, object)
 * The key is obtained by concatenating the corresponding values in the engine dictionary that matches the three fields. 
 * The order of the concatenation depends of the implementation
 */
public abstract class AbstractHexastoreIndex {
    public static final int entityKeySize = 16;

    private Dictionary dictionary;
    private TreeMap<BigInteger, RDFTriple> content = new TreeMap<>();

    public AbstractHexastoreIndex(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * Buids the index
     *
     * @param triples The set of triple stored in the index
     */
    public void build(Set<RDFTriple> triples) {
        for (RDFTriple triple : triples) {
            content.put(hash(triple), triple);
        }
    }

    /**
     * Finds the set of triple in which the {entityKeySize} most significants bits of the key are given
     *
     * @param x the identifier of the entity whose value from dictionary correponds to the {entityKeySize} most significant bits
     */
    protected Set<RDFTriple> findYZ(String x) throws InvalidQueryArgument {
        if (! dictionary.contains(x)) {
            throw new InvalidQueryArgument(x);
        }
	
        BigInteger xKey = dictionary.getKey(x);
        BigInteger keyLowerBound = composeKeyInternal(xKey, BigInteger.valueOf(0), BigInteger.valueOf(0));
        BigInteger keyUpperBound = composeKeyInternal(xKey.add(BigInteger.valueOf(1)), BigInteger.valueOf(0), BigInteger.valueOf(0)).subtract(BigInteger.valueOf(1));
	
        return valuesBetween(keyLowerBound, keyUpperBound);
    }

    /**
     * Finds the set of triple in which the 2*{entityKeySize} most significants bits of the key are given
     *
     * @param x the identifier of the entity whose value from dictionary correponds to the {entityKeySize} most significant bits of the resultings triple
     * @param y the identifier of the entity whose value from dictionary correponds to the {entityKeySize} bits situated just after the {entityKeySize} most significant bits of the resulting triple
     */
    protected Set<RDFTriple> findZ(String x, String y) throws InvalidQueryArgument {
        if (! dictionary.contains(x)) {
            throw new InvalidQueryArgument(x);
        }

        if (! dictionary.contains(y)) {
            throw new InvalidQueryArgument(y);
        }

        BigInteger xKey = dictionary.getKey(x);
        BigInteger yKey = dictionary.getKey(y);

        BigInteger keyLowerBound = composeKeyInternal(xKey, yKey, BigInteger.valueOf(0));
        BigInteger keyUpperBound = composeKeyInternal(xKey, yKey.add(BigInteger.valueOf(1)), BigInteger.valueOf(0)).subtract(BigInteger.valueOf(1));


        return valuesBetween(keyLowerBound, keyUpperBound);
    }

    /**
     * @param keyLowerBound the computed key lower bound of the resulting triples
     * @param keyUpperBound the computed key upper bound of the resulting triples
     * @return the set of triple in which keys are in the given range
     */
    private Set<RDFTriple> valuesBetween(BigInteger keyLowerBound, BigInteger keyUpperBound) {
        BigInteger lowerBound =  content.ceilingKey(keyLowerBound);
        BigInteger upperBound =  content.floorKey(keyUpperBound);
        if (lowerBound.compareTo(upperBound) > 0) {
            return new HashSet<>();
        }

        return new HashSet<>(content.subMap(lowerBound, true, upperBound, true).values());
    }

    /**
     * Computes the key value of a given RDF triple
     *
     * @param triple the RDF triple
     * @return the key balue
     */
    private BigInteger hash(RDFTriple triple) {
        BigInteger subjectKey = dictionary.getKey(triple.getSubject());
        BigInteger predicateKey = dictionary.getKey(triple.getPredicate());
        BigInteger objectKey = dictionary.getKey(triple.getObject());

        return composeKey(subjectKey, predicateKey, objectKey);
    }

    /**
     * Sends a message to the index implementation that responds with a composeKeyInternal message with the right parameter order
     *
     * @param subjectKey the dictionary key of the subject
     * @param predicateKey the dictionary key of the predicate
     * @param objectKey the dictionary key of the object
     *
     * @return the value of the key returned by composeKeyInternal
     */
    protected abstract BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey);

    /**
     * Concatenante the 3 dictionary key given in parameters
     *
     * @param subjectKey the first dictionary key
     * @param predicateKey the second dictionary key
     * @param objectKey the third dictionary key of
     *
     * @return the value of the key returned by composeKeyInternal
     */
    protected BigInteger composeKeyInternal(BigInteger x, BigInteger y, BigInteger z) {
        return x.shiftLeft(2 * entityKeySize).or(y.shiftLeft(entityKeySize).or(z));
    }

    public enum HexastoreIndexType {
        SPO, PSO, OSP, SOP, POS, OPS
    }
}
