package client;

import com.beust.jcommander.JCommander;
import message.GetRequest;
import message.PostRequest;
import message.Request;
import message.Response;
import shared.Config;

import java.io.*;
import java.net.Socket;

public class Client { // TODO: always send response containing log info

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    Client(int id, String mode) {
        init();

        if (mode.equals(ClientArgs.READER))
            sendReadRequest();
        else if (mode.equals(ClientArgs.WRITER))
            sendWriteRequest(id);

        terminate();
    }

    private void init() {
        try {
            socket = new Socket(Config.getServerAddress(), Config.getServerPort());
            System.out.println("--Connected--");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendReadRequest() {
        try {
            Request request = new GetRequest();
            outputStream.writeObject(request);
            System.out.println("GET request sent");

            Response response = (Response) inputStream.readObject();
            System.out.println("Response received");
            System.out.println("Data: " + response.getData());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendWriteRequest(int data) {
        try {
            Request request = new PostRequest(data);
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

    public static void main(String[] args) {
        ClientArgs clientArgs = new ClientArgs();
        new JCommander(clientArgs, args);
        new Client(clientArgs.getId(), clientArgs.getMode());
    }
}