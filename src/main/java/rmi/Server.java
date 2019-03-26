package rmi;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends Impl {
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        Impl obj = new Impl();
        Example stub = (Example) UnicastRemoteObject.exportObject(obj, 0);

        LocateRegistry.createRegistry(1099);
        Registry registry = LocateRegistry.getRegistry();

        try {
            registry.rebind("Example", stub);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        int x = 10;
        while (x > 0) {
            Thread.sleep(1000);
            System.out.println(stub.get());
            x--;
        }
        UnicastRemoteObject.unexportObject(obj, true);
    }
}
