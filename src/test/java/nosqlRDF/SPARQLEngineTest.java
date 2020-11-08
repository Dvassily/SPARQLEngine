package nosqlRDF;

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
    
    private SPARQLEngine engine;
    
    @Before
    public void intialize() {
	engine = new SPARQLEngine();
	
	engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE, ABRAHAM_LINCOLN_NAME_ENTITY);
	engine.insertTriple(ABRAHAM_LINCOLN_NAME_ENTITY, BORN_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY);
	engine.insertTriple(ABRAHAM_LINCOLN_NAME_ENTITY, DIED_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_DEATH_DATE_ENTITY);
	System.out.println("foo : " + engine);

	engine.initDictionaryAndIndexes();
    }

    @Test
    public void testInsertTriple() {
	assertEquals(7, engine.resourcesCount());
    }

    @Test
    public void testFindObject() {
	RDFTriple triple = engine.findObject(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE).iterator().next();

	assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY, triple.getObject());
    }
}
