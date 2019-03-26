package server.rmi;

import server.Server;
import shared.Config;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer implements Server {
    private NewsBulletinRmiWrapper newsBulletin;
    private Registry registry;
    private int expectedNumOfRequests;
    private static final int REGISTRY_PORT = 1099;
    private static final String RMI_KEY = "news";

    public RmiServer() {
        init();
    }

    private void init() {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        this.newsBulletin = new NewsBulletinRmiWrapperImpl();
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

            while (stub.getNumRequests() < this.expectedNumOfRequests) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new ServerException(e);
                }
            }
        } catch (RemoteException e) {
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
}
