package client;

import com.beust.jcommander.JCommander;

public class ClientRunner {
    public static void main(String[] args) {
        ClientArgs clientArgs = new ClientArgs();
        new JCommander(clientArgs, args);
        new Client(clientArgs.getId(), clientArgs.getMode());
    }
}
