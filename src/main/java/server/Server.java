package server;

import shared.Config;
import shared.Dispatcher;
import shared.logger.Logger;

import java.io.*;
import java.net.ServerSocket;

public class Server {
    private int nextRequestNum;
    private int expectedNumOfRequests;
    private Dispatcher dispatcher;
    private NewsBulletin newsBulletin;
    private ServerSocket serverSocket;

    private Logger readLogger;
    private Logger writeLogger;

    public Server() {
        init();
    }

    private void init() {
        nextRequestNum = 1;
        expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
        dispatcher = new Dispatcher(expectedNumOfRequests);
        newsBulletin = new NewsBulletin();
        initLoggers();
    }

    public void listen() throws IOException {
        serverSocket = new ServerSocket(Config.getServerPort());
    }

    /* Dispatch worker threads that handle incoming requests */
    public void accept() throws IOException {
        while (nextRequestNum <= expectedNumOfRequests)
            dispatcher.dispatch(new WorkerThread(nextRequestNum++, newsBulletin, serverSocket.accept(),
                    readLogger, writeLogger));
    }

    private void initLoggers() {
        try {
            readLogger = new Logger("readers") {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(String.join("\t", "sSeq", "oVal", "r-ID", "rNum") + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            writeLogger = new Logger("writers") {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(String.join("\t", "sSeq", "oVal", "w-ID") + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void terminate() {
        dispatcher.shutdown();
    }
}
