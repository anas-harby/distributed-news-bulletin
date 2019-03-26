package client;

import client.http.HttpClient;
import client.rmi.RmiClient;
import com.beust.jcommander.JCommander;

public class ClientRunner {
    public static void main(String[] args) {
        ClientArgs clientArgs = new ClientArgs();
        new JCommander(clientArgs, args);

        Client client = clientArgs.getArch().equals(ClientArgs.HTTP) ?
                new HttpClient(clientArgs.getId(), clientArgs.getMode()) :
                new RmiClient(clientArgs.getId(), clientArgs.getMode());

        client.connect();
        client.run();
        client.terminate();
    }
}
