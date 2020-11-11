package nosqlRDF;

import nosqlRDF.requests.SPARQLRequestParser;
import nosqlRDF.requests.Request;
import nosqlRDF.datas.RDFTriple;

import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.FileNotFoundException;
import java.util.List;
import java.lang.*;

public class App 
{
    public static void main(String[] args)
    {
	Arguments arguments = new Arguments();

	try {
	    arguments.parse(args);
	} catch (ParameterException exception) {
	    System.err.println(exception.getMessage());
	}

	if (arguments.getDataPath() == null) {
	    return;
	}
	
	SPARQLEngine engine = new SPARQLEngine();

	BenchmarkEngine benchmarkEngine = new BenchmarkEngine("Dictionary and hexastore indexes construction");
	benchmarkEngine.begin();
	
	try {
	    engine.parseData(arguments.getDataPath());
	} catch (FileNotFoundException e) {
	    System.err.println("Path " + arguments.getDataPath() + " does not points towards an existing file.");
	    return;
	}
	
	engine.initDictionaryAndIndexes();
	
	try {
	    List<Request> requests = new SPARQLRequestParser(arguments.getRequestPath()).loadQueries();

	    for (Request request : requests) {
		System.out.println("Execution of request : \n" + request);

		//List<RDFTriple> triple = engine.query(request);

	    }
	    
	} catch (IOException e) {
	    System.err.println("Failed to open request file : '" + arguments.getDataPath() + "' : " + e.getMessage());	    
	}
	
	benchmarkEngine.end();

	System.out.println("Benchmarks : ");
	System.out.println(" * " + benchmarkEngine);
    }
}
