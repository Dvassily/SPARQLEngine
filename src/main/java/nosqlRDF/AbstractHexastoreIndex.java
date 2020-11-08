package nosqlRDF;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.math.BigInteger;

public abstract class AbstractHexastoreIndex {
    public static final int entityKeySize = 8;

    private Dictionary dictionary;
    private TreeMap<BigInteger, RDFTriple> content = new TreeMap<>();

    public AbstractHexastoreIndex(Dictionary dictionary) {
	this.dictionary = dictionary;
    }

    public void build(Set<RDFTriple> triples) {
	for (RDFTriple triple : triples) {
	    content.put(hash(triple), triple);
	}
    }

    protected Set<RDFTriple> findYZ(String x) {
	BigInteger xKey = dictionary.getKey(x);
	BigInteger keyLowerBound = composeKeyInternal(xKey, BigInteger.valueOf(0), BigInteger.valueOf(0));
	BigInteger keyUpperBound = composeKeyInternal(xKey.add(BigInteger.valueOf(1)), BigInteger.valueOf(0), BigInteger.valueOf(0)).subtract(BigInteger.valueOf(1));

	return valuesBetween(keyLowerBound, keyUpperBound);
    }

    protected Set<RDFTriple> findZ(String x, String y) {
	System.out.println(dictionary);

	BigInteger xKey = dictionary.getKey(x);
	BigInteger yKey = dictionary.getKey(y);
	System.out.println("x : " + xKey);
	System.out.println("y : " + yKey);

    	BigInteger keyLowerBound = composeKeyInternal(xKey, yKey, BigInteger.valueOf(0));
	System.out.println("lb : " + keyLowerBound);

	BigInteger keyUpperBound = composeKeyInternal(xKey, yKey.add(BigInteger.valueOf(1)), BigInteger.valueOf(0)).subtract(BigInteger.valueOf(1));
	System.out.println("ub : " + keyUpperBound);
	
	return valuesBetween(keyLowerBound, keyUpperBound);	
    }

    private Set<RDFTriple> valuesBetween(BigInteger keyLowerBound, BigInteger keyUpperBound) {
        BigInteger lowerBound =  content.floorKey(keyLowerBound);
	BigInteger upperBound =  content.floorKey(keyUpperBound);

	return new HashSet<>(content.subMap(lowerBound, upperBound).values());
    }

    private BigInteger hash(RDFTriple triple) {
	BigInteger subjectKey = dictionary.getKey(triple.getSubject());
	BigInteger predicateKey = dictionary.getKey(triple.getPredicate());
	BigInteger objectKey = dictionary.getKey(triple.getObject());

	return composeKey(subjectKey, predicateKey, objectKey);
    }

    protected abstract BigInteger composeKey(BigInteger subjectKey, BigInteger predicateKey, BigInteger objectKey);
    
    protected BigInteger composeKeyInternal(BigInteger x, BigInteger y, BigInteger z) {
	return x.shiftLeft(2 * entityKeySize).or(y.shiftLeft(entityKeySize).or(z));
    }
}
