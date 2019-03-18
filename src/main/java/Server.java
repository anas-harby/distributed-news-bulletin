import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    Server() {
        try {
            serverSocket = new ServerSocket(Config.getServerPort());
            System.out.println("Server started...");
            System.out.println("Waiting for clients...");

            clientSocket = serverSocket.accept();
            System.out.println("Client connection accepted...");

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = in.readLine();
            System.out.println("Request: " + message);
            out.println("ACK");
            System.out.println("Message ACKed!");

            clientSocket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
