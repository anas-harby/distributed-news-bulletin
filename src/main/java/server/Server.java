package server;

import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;
import shared.Config;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    Server() {
        try {
            serverSocket = new ServerSocket(Config.getServerPort());
            System.out.println("--Server started--");
            System.out.println("Waiting for clients...");

            clientSocket = serverSocket.accept();
            System.out.println("Client connection accepted");

            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            Request request = (Request) inputStream.readObject();
            System.out.println(request.getType() + " request received");

            if (request.getType() == Request.Type.GET) {
                Response response = new Response(1234);
                outputStream.writeObject(response);
                System.out.println("Response sent");
            } else if (request.getType() == Request.Type.POST) {
                System.out.println("Data: " + ((PostRequest) request).getData());
            }

            clientSocket.close();
            System.out.println("--Connection closed--");
        } catch (IOException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
