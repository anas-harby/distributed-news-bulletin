package server.rmi;

import server.NewsBulletin;

import java.rmi.Remote;

public interface NewsBulletinRmiWrapper extends Remote {
    NewsBulletin.NewsInfo getCurrentNews();
    NewsBulletin.NewsInfo setCurrentNews(int news);
}
