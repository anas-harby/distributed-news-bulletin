package server;

import shared.Config;
import shared.Dispatcher;
import shared.logger.Logger;

import java.io.*;
import java.net.ServerSocket;

public class Server {

    private int nextRequestNum;
    private int expectedNumOfRequests;
    private NewsBulletin newsBulletin;

    private Dispatcher dispatcher;
    private ServerSocket serverSocket;

    private Logger readLogger;
    private Logger writeLogger;

    public Server() {
        try {
            init();
            while (nextRequestNum <= expectedNumOfRequests)
                dispatcher.dispatch(new WorkerThread(nextRequestNum++, newsBulletin, serverSocket.accept(), readLogger, writeLogger));
        } catch (IOException e) {
            System.out.println(e);
        }
        terminate();
    }

    private void init() throws IOException {
        nextRequestNum = 1;
        expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
        newsBulletin = new NewsBulletin();

        dispatcher = new Dispatcher(expectedNumOfRequests);
        serverSocket = new ServerSocket(Config.getServerPort());
        System.out.println("--Server started--");
        System.out.println("Waiting for clients...");
        System.out.println("----------------------");

        initLoggers();
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

    private void terminate() {
        dispatcher.shutdown();
    }
}
