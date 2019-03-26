package server.rmi;

import server.NewsBulletin;

import java.rmi.Remote;

public interface NewsBulletinRmiWrapper extends Remote {
    NewsBulletin.NewsInfo getCurrentNews(int clientID);
    NewsBulletin.NewsInfo setCurrentNews(int news, int clientID);
    int getNumRequests();
}
