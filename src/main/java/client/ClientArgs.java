package client;

import com.beust.jcommander.Parameter;

public class ClientArgs {

    public static final String READER = "r";
    public static final String WRITER = "w";

    @Parameter(names = "-id", description = "client.Client ID", required = true)
    private int id;

    @Parameter(names = "-mode", description = "client.Client mode", required = true, validateWith = ModeValidator.class)
    private String mode;

    public int getId() {
        return id;
    }

    public String getMode() {
        return mode;
    }
}
