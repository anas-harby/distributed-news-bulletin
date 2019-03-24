package rmi;

import java.rmi.RemoteException;

public class Impl implements Example {
    @Override
    public int get() throws RemoteException {
        return 1;
    }
}
