package nosqlRDF.datas;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

/**
 * Class that represent a SPARQLEngine dictionary
 * It contains two associative table that maps entity describing string and a corresponding integer index, in both directions
 */
public class Dictionary {
    private Map<String, BigInteger> dictionary = new HashMap<>();
    private Map<BigInteger, String> reverseDictionary = new HashMap<>();
    private BigInteger counter = BigInteger.valueOf(0);

    /**
     * Returns true if an entity is in the dictionary, false otherwise
     *
     * @param entity The name of the entity
     */
    public boolean contains(String entity) {
	return dictionary.containsKey(entity);
    }

    /**
     * Insert a new entity in the dictionary
     *
     * @param entity The name of the entity
     *
     * @return boolean True if the insertion succeeded, false otherwise (e.g. the entity is already present in the dictionary)
     */
    public boolean insert(String entity) {
	if (dictionary.containsKey(entity)) {
	    return false;
	}
	
	dictionary.put(entity, counter);
	reverseDictionary.put(counter, entity);
	counter = counter.add(BigInteger.valueOf(1));

	return true;
    }

    public BigInteger getKey(String entity) {
	return dictionary.get(entity);
    }

    public int entityCount() {
	return dictionary.size();
    }

    @Override
    public String toString() {
	String result = "Dictionary : {\n";
	
	for (Map.Entry<String, BigInteger> entry : dictionary.entrySet()) {
	    result += " - " + entry.getKey() + " : " + entry.getValue() + "\n";
	}

	return result + "}";
    }
}
