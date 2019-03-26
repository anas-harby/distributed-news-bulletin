package server;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class ServerArgs {
    public static class ArchValidator implements IParameterValidator {
        @Override
        public void validate(String argName, String argValue) throws ParameterException {
            if (!(argValue.equals(HTTP) || argValue.equals(RMI)))
                throw new ParameterException(argName + " can either be (" + HTTP + ") or (" + RMI + ")");
        }
    }
    public static final String HTTP = "http";
    public static final String RMI = "rmi";

    @Parameter(names = "-arch", description = "Architecture used in server", required = true, validateWith = ArchValidator.class)
    private String arch;

    public String getArch() {
        return arch;
    }
}
