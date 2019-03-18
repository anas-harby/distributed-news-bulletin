import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;

    Client() {
        try {
            socket = new Socket(Config.getServerAddress(), Config.getServerPort());
            System.out.println("Connected");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Hi");
            System.out.println("Request sent!");
            String reply = in.readLine();
            System.out.println("Reply: " + reply);

            socket.close();
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
