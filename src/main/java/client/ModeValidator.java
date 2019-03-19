package client;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ModeValidator implements IParameterValidator {
    public void validate(String argName, String argValue) throws ParameterException {
        if (!(argValue.equals(ClientArgs.READER) || argValue.equals(ClientArgs.WRITER)))
            throw new ParameterException(argName + " can either be reader(" + ClientArgs.READER + ") or writer(" + ClientArgs.WRITER + ")");
    }
}
