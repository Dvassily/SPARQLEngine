package nosqlRDF;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.io.FileNotFoundException;

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

	try {
	    engine.parseData(arguments.getDataPath());
	} catch (FileNotFoundException e) {
	    System.err.println("Path " + arguments.getDataPath() + " does not points towards an existing file.");
	}
	
	engine.initDictionaryAndIndexes();
    }
}
