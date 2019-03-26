package server.rmi;

import server.NewsBulletin;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NewsBulletinRmiWrapper extends Remote {
    NewsBulletin.NewsInfo getCurrentNews(int clientID) throws RemoteException;
    NewsBulletin.NewsInfo setCurrentNews(int news, int clientID) throws RemoteException;
    int getNumRequests() throws RemoteException;
}
