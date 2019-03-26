package client.http;

import client.Client;
import shared.message.GetRequest;
import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;
import shared.Config;

import java.io.*;
import java.net.Socket;

public class HttpClient extends Client {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public HttpClient(int id, String mode) {
        init(id, mode);
    }

    @Override
    public void connect() {
        try {
            socket = new Socket(Config.getServerAddress(), Config.getServerPort());
            System.out.println("--Connected--");

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        requests.get(type).run();
    }

    @Override
    public void terminate() {
        try {
            socket.close();
            System.out.println("--Connection closed--");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendGetRequest() {
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

    protected void sendPostRequest() {
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
}