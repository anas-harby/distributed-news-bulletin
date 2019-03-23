package client;

import shared.message.GetRequest;
import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;
import shared.Config;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private int id;
    private Type type;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Map<Type, Runnable> requestMap;

    public enum Type {
        READER,
        WRITER
    }

    Client(int id, String mode) {
        init(id, mode);
        initRequestMap();

        requestMap.get(type).run();

        terminate();
    }

    private void init(int id, String mode) {
        try {
            this.id = id;
            type = mode.equals(ClientArgs.READER) ? Type.READER : Type.WRITER;

            socket = new Socket(Config.getServerAddress(), Config.getServerPort());
            System.out.println("--Connected--");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initRequestMap() {
        requestMap = new HashMap<>();
        requestMap.put(Type.READER, this::sendGetRequest);
        requestMap.put(Type.WRITER, this::sendPostRequest);
    }

    private void sendGetRequest() {
        try {
            Request request = new GetRequest(id);
            outputStream.writeObject(request);
            System.out.println("GET request sent");

            Response response = (Response) inputStream.readObject();
            System.out.println("Response received");
            System.out.println("Data: " + response.getData());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendPostRequest() {
        try {
            Request request = new PostRequest(id, id);
            outputStream.writeObject(request);
            System.out.println("POST request sent");
        } catch (IOException e) {
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