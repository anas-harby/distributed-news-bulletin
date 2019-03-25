package client;

import shared.logger.Logger;
import shared.message.GetRequest;
import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;
import shared.Config;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Client {

    private int id;
    private Type type;
    private Map<Type, Runnable> requests;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Logger logger;

    public enum Type {
        READER,
        WRITER
    }

    Client(int id, String mode) {
        init(id, mode);
        requests.get(type).run();
        terminate();
    }

    private void init(int id, String mode) {
        try {
            this.id = id;
            type = mode.equals(ClientArgs.READER) ? Type.READER : Type.WRITER;
            initRequests();

            socket = new Socket(Config.getServerAddress(), Config.getServerPort());
            System.out.println("--Connected--");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            initLogger();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        writer.write("Client Name: " + id + "\n");
                        writer.write("Client Type: " + type + "\n\n");
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

    private void sendGetRequest() {
        try {
            Request request = new GetRequest(id);
            outputStream.writeObject(request);
            System.out.println("GET request sent");

            Response response = (Response) inputStream.readObject();
            System.out.println("Response received");

            logger.writeToFile(new String[]{Integer.toString(response.getRequestNum()),
                    Integer.toString(response.getServiceNum()),
                    Integer.toString(response.getData())});
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequest() {
        try {
            Request request = new PostRequest(id, id);
            outputStream.writeObject(request);
            System.out.println("POST request sent");

            Response response = (Response) inputStream.readObject();
            System.out.println("Response received");

            logger.writeToFile(new String[]{Integer.toString(response.getRequestNum()),
                    Integer.toString(response.getServiceNum())});
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void terminate() {
        try {
            socket.close();
            System.out.println("--Connection closed--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}