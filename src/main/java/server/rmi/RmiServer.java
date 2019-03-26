package server.rmi;

import server.Server;
import shared.Config;
import shared.logger.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer implements Server {
    private NewsBulletinRmiWrapper newsBulletin;
    private Registry registry;
    private int expectedNumOfRequests;
    private Logger readLogger;
    private Logger writeLogger;
    private static final int REGISTRY_PORT = 1099;
    private static final String RMI_KEY = "news";

    public RmiServer() {
        initLoggers();
        init();
    }

    private void init() {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        this.newsBulletin = new NewsBulletinRmiWrapperImpl(this.readLogger, this.writeLogger);
        this.expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfWriters() + Config.getNumOfReaders());
    }

    @Override
    public void listen() throws ServerException {
        try {
            LocateRegistry.createRegistry(REGISTRY_PORT);
            this.registry = LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void accept() throws ServerException {
        try {
            NewsBulletinRmiWrapper stub = (NewsBulletinRmiWrapper) UnicastRemoteObject.exportObject(this.newsBulletin, 0);
            this.registry.rebind(RMI_KEY, stub);

            while (stub.getNumRequests() < this.expectedNumOfRequests)
                Thread.sleep(100);
        } catch (RemoteException | InterruptedException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void terminate() throws ServerException {
        try {
            UnicastRemoteObject.unexportObject(this.newsBulletin, true);
        } catch (NoSuchObjectException e) {
            throw new ServerException(e);
        }
    }

    private void initLoggers() {
        try {
            this.readLogger = new Logger("readers") {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(String.join("\t", "sSeq", "oVal", "r-ID", "rNum") + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            this.writeLogger = new Logger("writers") {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(String.join("\t", "sSeq", "oVal", "w-ID") + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
