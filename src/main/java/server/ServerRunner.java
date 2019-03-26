package server;

import com.beust.jcommander.JCommander;
import server.http.HttpServer;
import server.rmi.RmiServer;

public class ServerRunner {
    public static Server getServer(String[] args) {
        ServerArgs serverArgs = new ServerArgs();
        new JCommander(serverArgs, args);
        return serverArgs.getArch().equals(ServerArgs.HTTP) ? new HttpServer() : new RmiServer();
    }
}
