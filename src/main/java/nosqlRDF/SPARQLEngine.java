package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.datas.Dictionary;
import nosqlRDF.indexes.*;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.Condition;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import org.eclipse.rdf4j.rio.RDFFormat;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.io.rdf.RDFParser;

/**
 * Main class for SPARQL engine
 * Its interface allows to insert triples, manually or through an .rdfxml file, intialize
 * the dictionary/indexes, and query the loaded datas
 */
public class SPARQLEngine {
    private Set<RDFTriple> triples = new HashSet<>();
    private Dictionary dictionary = new Dictionary();
    private SPOIndex spoIndex = null;
    private PSOIndex psoIndex = null;
    private OSPIndex ospIndex = null;
    private SOPIndex sopIndex = null;
    private POSIndex posIndex = null;
    private OPSIndex opsIndex = null;

    /**
     * Feed engine datas with an .rdfxml
     *
     * @param dataPath The path towards the RDF file
     */
    public void parseData(String dataPath) throws FileNotFoundException  {
	ArrayList<Atom> atoms = new ArrayList<Atom>();

	RDFParser parser = new RDFParser(new File(dataPath), RDFFormat.RDFXML);
	
	while(parser.hasNext()) {
	    Object element = parser.next();
	    
	    if(element instanceof Atom) {
		Atom dataTriple = (Atom) element;
		
		String subject = dataTriple.getTerm(0).toString();
		String predicate = dataTriple.getPredicate().getIdentifier().toString();
		String object = dataTriple.getTerm(1).toString();

		insertTriple(subject, predicate, object);
	    }
	}
	
	parser.close();

    }

    /**
     * Insert an RDF triple
     *
     * @param subject The triple subject
     * @param predicate, The triple predicate
     * @param object, The triple object
     */
    public void insertTriple(String subject, String predicate, String object) {
	triples.add(new RDFTriple(subject, predicate, object));	
    }

    /**
     * Intialize the dictionary and the 6 indexes
     */
    public void initDictionaryAndIndexes() {
	initDictionary();
	initIndexes();
    }

    /**
     * Find the set of RDF triple that corresponds to a specific  object and a specific predicate
     * Complexity is O(log(n))
     *
     * @param object The input object
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findSubject(String object, String predicate) {
	return opsIndex.findSubject(object, predicate);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param subject The input subject
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findObject(String subject, String predicate) {
	return spoIndex.findObject(subject, predicate);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param subject The input subject
     * @param object The input predicate
     */
    public Set<RDFTriple> findPredicate(String subject, String object) {
	return ospIndex.findPredicate(subject, object);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject
     * Complexity is O(log(n))
     *     *
     * @param subject The input subject
     */
    public Set<RDFTriple> findPredicateObject(String subject) {
	return spoIndex.findPredicateObject(subject);
    }


    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param object The input subject
     */
    public Set<RDFTriple> findSubjectPredicate(String object) {
	return ospIndex.findSubjectPredicate(object);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific predicate
     * Complexity is O(log(n))
     *     *
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findSubjectObject(String predicate) {
	return psoIndex.findSubjectObject(predicate);
    }

    // public List<RDFTriple> query(Request request)  {
	// Set<RDFTriple> resultSet = new HashSet<>();
	// Map<Condition, Set<RDFTriple>> intermediaryResults = new HashMap<>();
	
	// for (Condition condition : request.getConditions()) {
	//     resultSet.add(condition, findSubject(condition.getPredicate(), condition.getObject()));
	// }

	// Condition = null;
	// for (Condition condition : intermediaryResults.keySet()) {
	    
	// }
    // }

    /**
     * Find the number of entities loaded in the engine
     */
    public int entityCount() {
	return dictionary.entityCount();
    }

    private void initDictionary() {
	for (RDFTriple triple : triples) {
	    String subject = triple.getSubject();
	    String predicate = triple.getPredicate();
	    String object = triple.getObject();

	    if (! dictionary.contains(subject)) {
		dictionary.insert(subject);
	    }
	    
	    if (! dictionary.contains(predicate)) {
		dictionary.insert(predicate);
	    }

	    if (! dictionary.contains(object)) {
		dictionary.insert(object);
	    }
	}
    }

    private void initIndexes() {
	spoIndex = new SPOIndex(dictionary);
	psoIndex = new PSOIndex(dictionary);
	ospIndex = new OSPIndex(dictionary);
	sopIndex = new SOPIndex(dictionary);
	posIndex = new POSIndex(dictionary);
	opsIndex= new OPSIndex(dictionary);

	spoIndex.build(triples);
	ospIndex.build(triples);
	psoIndex.build(triples);
	sopIndex.build(triples);
	posIndex.build(triples);
	opsIndex.build(triples);
    }
}
