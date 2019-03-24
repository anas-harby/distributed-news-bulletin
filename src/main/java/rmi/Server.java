package rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends Impl {
    public static void main(String[] args) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        Impl obj = new Impl();
        Example stub = (Example) UnicastRemoteObject.exportObject(obj, 0);

        LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry();

        try {
            registry.bind("Example", stub);
        } catch (AlreadyBoundException e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }


}
