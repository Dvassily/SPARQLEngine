package nosqlRDF;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.FileNotFoundException;
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

	System.out.println("Benchmarking création Dictionnaire et Index");
	long debut = System.currentTimeMillis();

	try {
	    engine.parseData(arguments.getDataPath());
	} catch (FileNotFoundException e) {
	    System.err.println("Path " + arguments.getDataPath() + " does not points towards an existing file.");
	}
	
	engine.initDictionaryAndIndexes();

	long fin = System.currentTimeMillis();
	long benchmarkDicIndex= fin - debut;
	System.out.println("temps d'exécution: "+benchmarkDicIndex+"ms");
    }
}
