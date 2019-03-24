package server;

import shared.Config;
import shared.Dispatcher;
import shared.news.NewsBulletin;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    private int nextRequestNum;
    private int expectedNumOfRequests;
    private Dispatcher dispatcher;
    private NewsBulletin newsBulletin;
    private ServerSocket serverSocket;

    Server() {
        try {
            init();

            while (nextRequestNum <= expectedNumOfRequests) {
                dispatcher.dispatch(new WorkerThread(nextRequestNum++, newsBulletin, serverSocket.accept()));
            }

        } catch (IOException e) {
            System.out.println(e);
        }
        dispatcher.shutdown();
    }

    private void init() throws IOException {
        nextRequestNum = 1;
        expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
        dispatcher = new Dispatcher(expectedNumOfRequests);
        newsBulletin = new NewsBulletin();
        serverSocket = new ServerSocket(Config.getServerPort());
        System.out.println("--Server started--");
        System.out.println("Waiting for clients...");
        System.out.println("----------------------");
    }

    public static void main(String[] args) {
        new Server();
    }
}
