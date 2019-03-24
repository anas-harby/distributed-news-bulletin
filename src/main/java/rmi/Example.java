package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Example extends Remote {
    int get() throws RemoteException;
}
