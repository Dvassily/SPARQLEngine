
package nosqlRDF;

import nosqlRDF.RDFEntity.RDFEntityType;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

import static nosqlRDF.RDFEntity.RDFEntityType.*;

public class Dictionary {
    private Map<String, BigInteger> dictionary = new HashMap<>();
    private Map<BigInteger, String> reverseDictionary = new HashMap<>();
    private static BigInteger counter = BigInteger.valueOf(0);

    public boolean contains(String entity) {
	return dictionary.containsKey(entity);
    }

    public void insert(String entity) {
	dictionary.put(entity, counter);
	reverseDictionary.put(counter, entity);
	counter = counter.add(BigInteger.valueOf(1));
    }

    public BigInteger getKey(String entity) {
	return dictionary.get(entity);
    }

    @Override
    public String toString() {
	String result = "Dictionary : {\n";
	
	for (Map.Entry<String, BigInteger> entry : dictionary.entrySet()) {
	    result += " - " + entry.getKey() + " : " + entry.getValue() + "\n";
	}

	return result + "}";
    }

    public int resourcesCount() {
	return dictionary.size();
    }
}
