package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.Result;
import nosqlRDF.requests.Condition;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
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
    private static final String IS_LOCATED_IN_PREDICATE = "isLocatedIn";
    private static final String US_ENTITY = "US";
    private static final String NEW_YORK_ENTITY = "New York";
    private static final String EDF_ENTITY = "EDF";

//    private static final Condition cond1 = new Condition("v0",BORN_ON_DATE_PREDICATE,ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY);
//    private static final Condition cond2 = new Condition("v0",HAS_NAME_PREDICATE,ABRAHAM_LINCOLN_NAME_ENTITY);
//    private static final Condition cond3 = new Condition(ALICE_ENTITY,"v0",NEW_YORK_ENTITY);
//    private static final Condition cond4 = new Condition(ALICE_ENTITY,WORKS_FOR_PREDICATE,"v0");

    private List<Condition> conditions=new ArrayList<Condition>();

    private SPARQLEngine engine;

    @Before
    public void intialize() {
        engine = new SPARQLEngine();

        engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE, ABRAHAM_LINCOLN_NAME_ENTITY);
        engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, BORN_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY);
        engine.insertTriple(ABRAHAM_LINCOLN_ENTITY, DIED_ON_DATE_PREDICATE, ABRAHAM_LINCOLN_DEATH_DATE_ENTITY);
        engine.insertTriple(ALICE_ENTITY, LIVES_IN_PREDICATE, NEW_YORK_ENTITY);
        engine.insertTriple(ALICE_ENTITY, WORKS_FOR_PREDICATE, EDF_ENTITY);
        engine.insertTriple(NEW_YORK_ENTITY, IS_LOCATED_IN_PREDICATE, US_ENTITY);
        engine.insertTriple(EDF_ENTITY, IS_LOCATED_IN_PREDICATE, US_ENTITY);

        engine.initDictionaryAndIndexes();
    }

    @Test
    public void testInsertTriple() {
        assertEquals(14, engine.entityCount());
    }

    @Test
    public void testFindSubject() throws InvalidQueryArgumentException {
        Set<RDFTriple> triples = engine.findSubject(HAS_NAME_PREDICATE, ABRAHAM_LINCOLN_NAME_ENTITY);
        assertEquals(1, triples.size());
        RDFTriple triple = triples.iterator().next();

        assertEquals(ABRAHAM_LINCOLN_ENTITY, triple.getSubject());
    }

    @Test
    public void testFindObject() throws InvalidQueryArgumentException {
        Set<RDFTriple> triples = engine.findObject(ABRAHAM_LINCOLN_ENTITY, HAS_NAME_PREDICATE);
        assertEquals(1, triples.size());
        RDFTriple triple = triples.iterator().next();

        assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY, triple.getObject());
    }

    @Test
    public void testFindPredicate() throws InvalidQueryArgumentException {
        Set<RDFTriple> triples = engine.findPredicate(ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY, ABRAHAM_LINCOLN_ENTITY);
        assertEquals(1, triples.size());
        RDFTriple triple = triples.iterator().next();

        assertEquals(BORN_ON_DATE_PREDICATE, triple.getPredicate());
    }

    @Test
    public void testFindPredicateObject() throws InvalidQueryArgumentException {
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

    @Test
    public void testFindSubjectPredicate() throws InvalidQueryArgumentException {
        Set<RDFTriple> triples = engine.findSubjectPredicate(ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY);
        assertEquals(1, triples.size());
        RDFTriple triple = triples.iterator().next();

        assertEquals(BORN_ON_DATE_PREDICATE, triple.getPredicate());
        assertEquals(ABRAHAM_LINCOLN_ENTITY, triple.getSubject());
    }

    @Test
    public void testFindSubjectObject() throws InvalidQueryArgumentException {
        Set<RDFTriple> triples = engine.findSubjectObject(HAS_NAME_PREDICATE);
        assertEquals(1, triples.size());

        RDFTriple triple = triples.iterator().next();
        assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY, triple.getObject());
    }

    // @Test
    // public void testQuery() throws InvalidQueryArgumentException {
    //     List<Condition> conditions = new ArrayList<>();
    //     Condition c1 = new Condition();
    //     c1.setSubject("x", true);
    //     c1.setPredicate(LIVES_IN_PREDICATE, false);
    //     c1.setObject("y", true);

    //     Condition c2 = new Condition();
    //     c2.setSubject("x", true);
    //     c2.setPredicate(WORKS_FOR_PREDICATE, false);
    //     c2.setObject("z", true);

    //     Condition c3 = new Condition();
    //     c3.setSubject("y", true);
    //     c3.setPredicate(IS_LOCATED_IN_PREDICATE, false);
    //     c3.setObject(US_ENTITY, false);

    //     Condition c4 = new Condition();
    //     c4.setSubject("z", true);
    //     c4.setPredicate(IS_LOCATED_IN_PREDICATE, false);
    //     c4.setObject("US", false);

    //     conditions.add(c1);
    //     conditions.add(c2);
    //     conditions.add(c3);
    //     conditions.add(c4);

    //     List<String> projection = new ArrayList<>();
    //     projection.add("x");

    //     Request request = new Request(projection, conditions, "");

    //     Result result = engine.query(request);

    //     assertEquals(1, result.count());
    //     assertTrue(result.containsResult("x", ALICE_ENTITY));
    // }

//   @Test
//   public void testReqCond1() {
//   conditions.add(0,cond1);
//   Request req=new Request("v0",conditions);
//   RDFTriple res=engine.query(req).iterator().next();
//   assertEquals(ABRAHAM_LINCOLN_ENTITY,res.getSubject());
//   assertEquals(BORN_ON_DATE_PREDICATE,res.getPredicate());
//   assertEquals(ABRAHAM_LINCOLN_BIRTH_DATE_ENTITY,res.getObject());
//   }
//
//
//   @Test
//    public void testReqCond2() {
//    conditions.add(0,cond2);
//    Request req=new Request("v0",conditions);
//    RDFTriple res=engine.query(req).iterator().next();
//    assertEquals(ABRAHAM_LINCOLN_ENTITY,res.getSubject());
//    assertEquals(HAS_NAME_PREDICATE,res.getPredicate());
//    assertEquals(ABRAHAM_LINCOLN_NAME_ENTITY,res.getObject());
//    }
//
//    // @Test
    // public void testWithRealData() {
    //     engine = new SPARQLEngine();

    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer528", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer1604", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer879", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer3615", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer480", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer2294", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer4423", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer235", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer3771", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer4149", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");
    //     engine.insertTriple("http://db.uwaterloo.ca/~galuc/wsdbm/Offer138", "http://schema.org/eligibleRegion", "http://db.uwaterloo.ca/~galuc/wsdbm/Country137");

    //     engine.initDictionaryAndIndexes();

    // }


//    @Test
//    public void testReqCond3() {
//    conditions.add(0,cond3);
//    Request req=new Request("v0",conditions);
//    RDFTriple res=engine.query(req).iterator().next();
//    assertEquals(ALICE_ENTITY,res.getSubject());
//    assertEquals(LIVES_IN_PREDICATE,res.getPredicate());
//    assertEquals(NEW_YORK_ENTITY,res.getObject());
//    } 

//    @Test
//    public void testReqCond4() {
//    conditions.add(0,cond4);
//    Request req=new Request("v0",conditions);
//    RDFTriple res=engine.query(req).iterator().next();
//    assertEquals(ALICE_ENTITY,res.getSubject());
//    assertEquals(WORKS_FOR_PREDICATE,res.getPredicate());
//    assertEquals(EDF_ENTITY,res.getObject());   
//    } 
}
