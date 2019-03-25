package server;

import shared.Config;
import shared.Dispatcher;
import shared.news.NewsBulletin;
import ssh.SshConnection;
import ssh.SshRunnable;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    private int nextRequestNum;
    private int expectedNumOfRequests;
    private Dispatcher sshDispatcher;
    private Dispatcher requestsDispatcher;
    private NewsBulletin newsBulletin;
    private ServerSocket serverSocket;
    private static final int DEFAULT_SSH_PORT = 22;

    public Server() {
        try {
            init();

            /* Dispatch Readers ssh invocation */
            for (int i = 0; i < Config.getNumOfReaders(); i++) {
                try {
                    sshDispatcher.dispatch(new SshRunnable(
                            System.getProperty("user.name"),
                            Config.getReaders().get(i),
                            DEFAULT_SSH_PORT,
                            Config.getNumOfAccesses(),
                            i ,
                            SshRunnable.Mode.READ
                    ));
                } catch (SshConnection.SshException e) {
                    e.printStackTrace();
                }
            }

            /* Dispatch Writers ssh invocation */
            for (int i = 0; i < Config.getNumOfWriters(); i++) {
                try {
                    sshDispatcher.dispatch(new SshRunnable(
                            System.getProperty("user.name"),
                            Config.getWriters().get(i),
                            DEFAULT_SSH_PORT,
                            Config.getNumOfAccesses(),
                            i + Config.getNumOfReaders(),
                            SshRunnable.Mode.WRITE
                    ));
                } catch (SshConnection.SshException e) {
                    e.printStackTrace();
                }
            }

            /* Dispatch worker threads that handle incoming requests */
            while (nextRequestNum <= expectedNumOfRequests)
                requestsDispatcher.dispatch(new WorkerThread(nextRequestNum++, newsBulletin, serverSocket.accept()));
        } catch (IOException e) {
            System.out.println(e);
        }
        terminate();
    }

    private void init() throws IOException {
        nextRequestNum = 1;
        expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
        requestsDispatcher = new Dispatcher(expectedNumOfRequests);
        sshDispatcher = new Dispatcher(Config.getNumOfReaders() + Config.getNumOfWriters());
        newsBulletin = new NewsBulletin();
        serverSocket = new ServerSocket(Config.getServerPort());
        System.out.println("--Server started--");
        System.out.println("Waiting for clients...");
        System.out.println("----------------------");
    }

    private void terminate() {
        sshDispatcher.shutdown();
        requestsDispatcher.shutdown();
    }
}
