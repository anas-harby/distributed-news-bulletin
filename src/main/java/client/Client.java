package client;

import shared.logger.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Client {
    protected int id;
    protected Type type;
    protected Map<Type, Runnable> requests;

    protected Logger logger;

    public abstract void connect();

    public abstract void run();

    public abstract void terminate();

    protected abstract void sendGetRequest();

    protected abstract void sendPostRequest();

    protected void init(int id, String mode) {
        this.id = id;
        type = mode.equals(ClientArgs.READER) ? Type.READER : Type.WRITER;
        initRequests();
        initLogger();
    }

    private void initRequests() {
        requests = new HashMap<>();
        requests.put(Type.READER, this::sendGetRequest);
        requests.put(Type.WRITER, this::sendPostRequest);
    }

    private void initLogger() {
        try {
            logger = new Logger(id) {
                @Override
                public void writeHeaders(File logFile) {
                    try {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write("HttpClient Name: " + id + "\n");
                        writer.write("HttpClient Type: " + type + "\n\n");
                        writer.write(String.join("\t", "rSeq", "sSeq", type == Type.READER ? "oVal" : "") + "\n");
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
