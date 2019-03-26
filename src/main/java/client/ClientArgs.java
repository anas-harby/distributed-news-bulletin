package client;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class ClientArgs {

    public static class ModeValidator implements IParameterValidator {
        @Override
        public void validate(String argName, String argValue) throws ParameterException {
            if (!(argValue.equals(READER) || argValue.equals(WRITER)))
                throw new ParameterException(argName + " can either be reader(" + ClientArgs.READER + ") or writer(" + ClientArgs.WRITER + ")");
        }
    }

    public static class ArchValidator implements IParameterValidator {
        @Override
        public void validate(String argName, String argValue) throws ParameterException {
            if (!(argValue.equals(HTTP) || argValue.equals(RMI)))
                throw new ParameterException(argName + " can either be (" + HTTP + ") or (" + RMI + ")");
        }
    }

    public static final String READER = "r";
    public static final String WRITER = "w";

    public static final String HTTP = "http";
    public static final String RMI = "rmi";

    @Parameter(names = "-id", description = "Client ID", required = true)
    private int id;

    @Parameter(names = "-mode", description = "Client mode", required = true, validateWith = ModeValidator.class)
    private String mode;

    @Parameter(names = "-arch", description = "Architecture used in client", required = true, validateWith = ClientArgs.ArchValidator.class)
    private String arch;

    public int getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }

    public String getArch() {
        return arch;
    }
}
