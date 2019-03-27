package client.rmi;

import client.Client;
import client.ClientArgs;
import client.Type;
import server.NewsBulletin;
import server.rmi.NewsBulletinRmiWrapper;
import shared.logger.Logger;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RmiClient extends Client {
    private NewsBulletinRmiWrapper newsBulletinRmiWrapper;

    public RmiClient(int id, String mode) {
        init(id, mode);
    }

    @Override
    public void connect() {
        try {
            newsBulletinRmiWrapper = (NewsBulletinRmiWrapper) LocateRegistry.getRegistry(null).lookup("news");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void terminate() {

    }

    protected void sendGetRequest() {
        try {
            NewsBulletin.NewsInfo newsInfo = newsBulletinRmiWrapper.getCurrentNews(this.id);
            System.out.println("GET request sent");
            System.out.println("Response received");

            logger.writeToFile(new String[]{Integer.toString(newsInfo.getRequestNum()),
                    Integer.toString(newsInfo.getServiceNum()),
                    Integer.toString(newsInfo.getNews())});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendPostRequest() {
        try {
            NewsBulletin.NewsInfo newsInfo = newsBulletinRmiWrapper.setCurrentNews(this.id, this.id);
            System.out.println("POST request sent");
            System.out.println("Response received");

            logger.writeToFile(new String[]{Integer.toString(newsInfo.getRequestNum()),
                    Integer.toString(newsInfo.getServiceNum())});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
