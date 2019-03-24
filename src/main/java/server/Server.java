package server;

import shared.Config;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private ServerSocket serverSocket;
    private int expectedNumOfRequests;

    Server() {
        try {
            serverSocket = new ServerSocket(Config.getServerPort());
            expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
            System.out.println("--Server started--");
            System.out.println("Waiting for clients...");
            System.out.println("----------------------");

            while (expectedNumOfRequests > 0) { // TODO: check condition
                new Thread(new WorkerThread(serverSocket.accept())).start(); //TODO: Use Dispatcher instead
                expectedNumOfRequests--;
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        // TODO: join, use shutdown instead
    }

    public static void main(String[] args) {
        new Server();
    }
}
