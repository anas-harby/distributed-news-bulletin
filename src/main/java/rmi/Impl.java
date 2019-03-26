package rmi;

import java.rmi.RemoteException;

public class Impl implements Example {
    private int x = 0;
    @Override
    public int get() throws RemoteException {
        return x;
    }

    @Override
    public void set(int x) throws RemoteException {
        this.x = x;
    }

}
