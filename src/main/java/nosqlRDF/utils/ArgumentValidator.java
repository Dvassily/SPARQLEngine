package nosqlRDF.utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import java.io.File;

public class ArgumentValidator implements IParameterValidator {
    @Override
    public void validate(String name, String value) {
        if (name.equals("-queries") || name.equals("-q")) {
            File file = new File(value);

            if (! file.isDirectory()) {
                throw new ParameterException("Argument " + name + " must point towards directory");
            }
        }
    }
}
