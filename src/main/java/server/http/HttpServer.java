package server.http;

import server.NewsBulletin;
import server.Server;
import server.WorkerThread;
import shared.Config;
import shared.Dispatcher;
import shared.logger.Logger;

import java.io.*;
import java.net.ServerSocket;

public class HttpServer implements Server {
    private int expectedNumOfRequests;
    private Dispatcher dispatcher;
    private NewsBulletin newsBulletin;
    private ServerSocket serverSocket;

    private Logger readLogger;
    private Logger writeLogger;

    public HttpServer() {
        init();
    }

    private void init() {
        expectedNumOfRequests = Config.getNumOfAccesses() * (Config.getNumOfReaders() + Config.getNumOfWriters());
        dispatcher = new Dispatcher(expectedNumOfRequests);
        newsBulletin = new NewsBulletin();
        initLoggers();
    }

    @Override
    public void listen() throws ServerException {
        try {
            serverSocket = new ServerSocket(Config.getServerPort());
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void accept() throws ServerException {
        /* Dispatch worker threads that handle incoming requests */
        try {
            while (newsBulletin.getNumOfRequests() < expectedNumOfRequests)
                dispatcher.dispatch(new WorkerThread(newsBulletin, serverSocket.accept(),
                        readLogger, writeLogger));
        } catch (IOException e) {
            throw new ServerException(e);
        }
    }

    @Override
    public void terminate() {
        dispatcher.shutdown();
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

}
