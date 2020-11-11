package nosqlRDF;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest
{
    // private static final File 100K=new File("../Datasets_100K_500K_1M/100K.rdfxml");
    // private static final File 500K=new File("../Datasets_100K_500K_1M/500K.rdfxml");
    // private static final File 1M=new File("../Datasets_100K_500K_1M/1M.rdfxml");

    private static ArrayList<SPARQLRequest> requests=new ArrayList<SPARQLRequest>();

    private SPARQLEngine engine;
    private SPARQLRequestParser requestparser;

   @Before
   requests.add("SELECT ?v0 WHERE {
	?v0 <http://schema.org/eligibleRegion> <http://db.uwaterloo.ca/~galuc/wsdbm/Country48>}");
   requests.add("SELECT ?v0 WHERE {
	?v0 <http://schema.org/eligibleRegion> <http://db.uwaterloo.ca/~galuc/wsdbm/Country165>}"); 
   requests.add("SELECT ?v0 WHERE {
	?v0 <http://schema.org/eligibleRegion> <http://db.uwaterloo.ca/~galuc/wsdbm/Country105>}");  

   @Test
    
} 

