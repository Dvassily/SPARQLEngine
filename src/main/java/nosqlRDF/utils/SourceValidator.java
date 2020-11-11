package nosqlRDF.utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.io.File;

public class SourceValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) {
	File file = new File(value);

	if (! file.exists()) {

	}
    }
}
