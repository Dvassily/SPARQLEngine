package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.datas.Dictionary;
import nosqlRDF.indexes.*;
import nosqlRDF.indexes.AbstractHexastoreIndex.HexastoreIndexType;
import nosqlRDF.requests.Result;
import static nosqlRDF.indexes.AbstractHexastoreIndex.HexastoreIndexType.*;

import java.util.*;
import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.stream.Collectors;
import nosqlRDF.requests.Condition;
import nosqlRDF.requests.JoinOperation;
import nosqlRDF.requests.JoinableSet;
import nosqlRDF.requests.JoinableTriple;
import nosqlRDF.requests.Request;
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
    private Map<HexastoreIndexType, AbstractHexastoreIndex> indexes = new HashMap<>();

    /**
     * Feed engine datas with an .rdfxml
     *
     * @param dataPath The path towards the RDF file
     */
    public void parseData(String dataPath) throws FileNotFoundException{
        
        ArrayList<Atom> atoms = new ArrayList<Atom>();
        RDFParser parser = new RDFParser(new File(dataPath), RDFFormat.RDFXML);

        while (parser.hasNext()) {
            Object element = parser.next();

            if (element instanceof Atom) {
                Atom dataTriple = (Atom) element;

                String subject = dataTriple.getTerm(0).getLabel();
                String predicate = dataTriple.getPredicate().getIdentifier().toString();
                String object = dataTriple.getTerm(1).getLabel();

                insertTriple(subject, predicate, object);
            }
        }

        parser.close();
    }

    /**
     * Insert an RDF triple
     *
     * @param subject    The triple subject
     * @param predicate, The triple predicate
     * @param object,    The triple object
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
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param predicate The input subject
     * @param object    The input predicate
     */
    public Set<RDFTriple> findSubject(String predicate, String object) throws InvalidQueryArgumentException {
        return ((POSIndex) indexes.get(POS)).findSubject(predicate, object);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param subject   The input subject
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findObject(String subject, String predicate) throws InvalidQueryArgumentException {
        return ((SPOIndex) indexes.get(SPO)).findObject(subject, predicate);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param subject The input subject
     * @param object  The input predicate
     */
    public Set<RDFTriple> findPredicate(String subject, String object) throws InvalidQueryArgumentException {
        return ((OSPIndex) indexes.get(OSP)).findPredicate(subject, object);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific subject
     * Complexity is O(log(n))
     * *
     *
     * @param subject The input subject
     */
    public Set<RDFTriple> findPredicateObject(String subject) throws InvalidQueryArgumentException {
        return ((SPOIndex) indexes.get(SPO)).findPredicateObject(subject);
    }


    /**
     * Find the set of RDF triple that corresponds to a specific subject and a specific predicate
     * Complexity is O(log(n))
     *
     * @param object The input subject
     */
    public Set<RDFTriple> findSubjectPredicate(String object) throws InvalidQueryArgumentException {
        return ((OSPIndex) indexes.get(OSP)).findSubjectPredicate(object);
    }

    /**
     * Find the set of RDF triple that corresponds to a specific predicate
     * Complexity is O(log(n))
     * *
     *
     * @param predicate The input predicate
     */
    public Set<RDFTriple> findSubjectObject(String predicate) throws InvalidQueryArgumentException {
        return ((PSOIndex) indexes.get(PSO)).findSubjectObject(predicate);
    }

    public Result query(Request request) {
        Set<JoinableTriple> tripleSet = new HashSet<>();

        try {
            if (request.getConditions().size() == 1) {
                tripleSet.addAll(request.getConditions().get(0).elements(this));
            } else if (request.getConditions().size() > 1) {
                JoinOperation join = buildJoinNode(request.getConditions().iterator(), null);
                tripleSet.addAll(join.perform(this));
            }
        } catch (InvalidQueryArgumentException e) {
            System.out.println("Argument '" + e.getArgument() + "' wasn't found");

            return new Result();
        }

        Result result = new Result();
        for (JoinableTriple triple : tripleSet) {
            for (String projection : request.getProjection()) {
                Map<String, String> variables = triple.variables();

                if (variables.containsKey(projection)) {
                    result.add(projection, variables.get(projection));
                }
            }
        }

        return result;
    }

    private JoinOperation buildJoinNode(Iterator<Condition> iterator, JoinOperation previousJoin) {
        JoinableSet set1 = null;
        JoinableSet set2 = null;

        if (previousJoin == null) {
            set1 = iterator.next();
            set2 = iterator.next();
        } else {
            if (! iterator.hasNext()) {
                return null;
            }

            set1 = previousJoin;
            set2 = iterator.next();
        }

        JoinOperation joinOperation = new JoinOperation(set1, set2);
        JoinOperation parentJoinOperation = buildJoinNode(iterator, joinOperation);

        if (parentJoinOperation == null) {
            return joinOperation;
        }

		return parentJoinOperation;
    }

    // private Set<RDFTriple> getResultFromCondition(Condition condition) throws InvalidQueryArgumentException {


    // }

    /**
     * Find the number of entities loaded in the engine
     */
    public int entityCount() {
        return dictionary.entityCount();
    }

    /** Writes the engine triples in a file */
    public void WriteEngine() throws Exception{
        File engineFile = new File("engineFile.txt");
        FileWriter engineWriter= new FileWriter(engineFile);
        engineWriter.write("triples de la base de données:\n");
        engineWriter.flush();
        for(RDFTriple triple: this.triples){
            engineWriter.write("Subject: "+triple.getSubject()+" Predicate: "+triple.getPredicate()+" Object: "+triple.getObject()+"\n");
            
        } 
        engineWriter.flush();
        engineWriter.close();
    }

    public int getRowsCount() {
        return triples.size();
    }

    public Map<HexastoreIndexType, AbstractHexastoreIndex> getIndexes() {
        return indexes;
    }

    private void initDictionary() {
        for (RDFTriple triple : triples) {
            String subject = triple.getSubject();
            String predicate = triple.getPredicate();
            String object = triple.getObject();

            if (!dictionary.contains(subject)) {
                dictionary.insert(subject, true);
            }

            if (!dictionary.contains(predicate)) {
                dictionary.insert(predicate, false);
            }

            if (!dictionary.contains(object)) {
                dictionary.insert(object, true);
            }
        }

        try {
			Files.write(Paths.get("dico.txt"), dictionary.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void initIndexes() {
        indexes.put(SPO, new SPOIndex(dictionary));
        indexes.put(PSO, new PSOIndex(dictionary));
        indexes.put(OSP, new OSPIndex(dictionary));
        indexes.put(SOP, new SOPIndex(dictionary));
        indexes.put(POS, new POSIndex(dictionary));
        indexes.put(OPS, new OPSIndex(dictionary));

        indexes.get(SPO).build(triples);
        indexes.get(OSP).build(triples);
        indexes.get(PSO).build(triples);
        indexes.get(SOP).build(triples);
        indexes.get(POS).build(triples);
        indexes.get(OPS).build(triples);
    }
}
