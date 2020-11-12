package nosqlRDF;

import nosqlRDF.datas.RDFTriple;
import nosqlRDF.requests.Request;
import nosqlRDF.requests.Condition;
import nosqlRDF.InvalidQueryArgument;
import nosqlRDF.requests.SPARQLRequestParser;

import java.util.Set;
import java.util.List;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SPARQLRequestTest 
{
    private SPARQLRequestParser requestsParser;
    private SPARQLEngine engine=new SPARQLEngine();

   @Before
   public void initialize(){
       try{
           engine.parseData("/home/e20190009602/TPNoSQL/Projet/Datasets_100K_500K_1M/100K.rdfxml");
       } catch(FileNotFoundException f){
           System.out.println("File not found");
       } 
       
       engine.initDictionaryAndIndexes();
   }

  @Test
  public void testNationality(){
      try{
        requestsParser = new SPARQLRequestParser("/home/e20190009602/TPNoSQL/Projet/Requetes/STAR_Queries_100_1000_10000/100/Q_1_nationality_100.queryset");  
        List<Request> requests = requestsParser.loadQueries();
        for(Request request:requests){
            Set<RDFTriple> result=engine.query(request);
            System.out.println("Taille du résultat: "+result.size());
        } 
      } catch(Exception e){
          System.out.println("Requêtes non chargées: "+ e);
      }  

  } 

} 
