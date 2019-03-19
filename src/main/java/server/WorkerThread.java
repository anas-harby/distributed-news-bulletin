package server;

import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerThread implements Runnable {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    WorkerThread(Socket socket) {
        init(socket);
    }

    @Override
    public void run() {
        try {
            Request request = (Request) inputStream.readObject();
            System.out.println(request.getType() + " request received");

            if (request.getType() == Request.Type.GET)
                handleGetRequest(request);
            else if (request.getType() == Request.Type.POST)
                handlePostRequest(request);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        terminate(); // TODO: check
    }

    private void init(Socket socket) {
        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(Request request) {
        try {
            Response response = new Response(1234); // TODO: read news from the system and write it to the socket
            outputStream.writeObject(response);
            System.out.println("Response sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostRequest(Request request) {
        // TODO: read news from the POST request data and write it to the system
        System.out.println("Data: " + ((PostRequest) request).getData());
    }

    private void terminate() {
        try {
            socket.close();
            System.out.println("--Client connection closed--");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
