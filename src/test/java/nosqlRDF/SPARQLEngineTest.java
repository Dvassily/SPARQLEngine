
package nosqlRDF;

import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SPARQLEngineTest 
{
    private static final String ABRAHAM_LINCOLN_ENTITY = "Abraham_Lincoln";
    private static final String ABRAHAM_LINCOLN_NAME_ENTITY = "Abraham Lincoln";
    private static final String HAS_NAME_PREDICATE = "hasName";
    private static final String BORN_ON_DATE_PREDICATE = "BornOnDate";
    private static final String DIED_ON_DATE_PREDICATE = "DiedOnDate";
    private static final String ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY = "1809-02-12";
    private static final String ABRAHAM_LINCOLN_DEATH_DATE_ENTITY = "1865-04-15";
    private static final String ALICE_ENTITY = "Alice";
    private static final String LIVES_IN_PREDICATE = "livesIn";
    private static final String WORKS_FOR_PREDICATE = "worksFor";
    private static final String PARIS_ENTITY = "Paris";
    private static final String EDF_ENTITY = "EDF";
    
    private SPARQLEngine engine;
    
    @Before
    public void intialize() {
	engine = new SPARQLEngine();
	
	engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE, ABRAHAM_LINCOLN_NAME_ENTITY);
	engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, BORN_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY);
	engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, DIED_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_DEATH_DATE_ENTITY);
	engine.insertTriple(ALICE_ENTITY, LIVES_IN_PREDICATE, PARIS_ENTITY);
	engine.insertTriple(ALICE_ENTITY, WORKS_FOR_PREDICATE, EDF_ENTITY);

	engine.initDictionaryAndIndexes();
    }

    @Test
    public void testInsertTriple() {
	assertEquals(12, engine.entityCount());
    }

    @Test
    public void testFindObject() {
	RDFTriple triple = engine.findObject(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE).iterator().next();

	assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY, triple.getObject());

    }

    @Test
    public void testFindPredicateObject() {
	Set<RDFTriple> triples = engine.findPredicateObject(ABRAHAM_LINCOLN_ENTITY);

	assertEquals(3, triples.size());
	for (RDFTriple triple : triples) {
	    if (triple.getPredicate().equals(HAS_NAME_PREDICATE)) {
		assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY, triple.getObject());
	    } else if (triple.getPredicate().equals(BORN_ON_DATE_PREDICATE)) {
		assertEquals(ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY, triple.getObject());
	    } else if (triple.getPredicate().equals(DIED_ON_DATE_PREDICATE)) {
		assertEquals(ABRAHAM_LINCOLN_DEATH_DATE_ENTITY, triple.getObject());
	    } else {
		assertTrue(false);
	    }
	}
    }
}
