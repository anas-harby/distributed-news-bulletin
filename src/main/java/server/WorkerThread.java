package server;

import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;
import shared.news.NewsBulletin;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerThread implements Runnable {

    private int requestNum;
    private NewsBulletin newsBulletin;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    WorkerThread(int requestNum, NewsBulletin newsBulletin, Socket socket) {
        init(requestNum, newsBulletin, socket);
    }

    @Override
    public void run() {
        try {
            Request request = (Request) inputStream.readObject();
            System.out.println(request.getType() + " request received");

            if (request.getType() == Request.Type.GET) // TODO: map
                handleGetRequest(request);
            else if (request.getType() == Request.Type.POST)
                handlePostRequest(request);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        terminate();
    }

    private void init(int requestNum, NewsBulletin newsBulletin, Socket socket) {
        this.requestNum = requestNum;
        this.newsBulletin = newsBulletin;
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
            NewsBulletin.NewsInfo newsInfo = newsBulletin.getCurrentNews();
            Response response = new Response();
            response.setRequestNum(requestNum);
            response.setServiceNum(newsInfo.getServiceNum());
            response.setData(newsInfo.getNews());
            outputStream.writeObject(response);
            System.out.println("Response sent");
            // TODO: log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostRequest(Request request) {
        try {
            NewsBulletin.NewsInfo newsInfo = newsBulletin.setCurrentNews(((PostRequest) request).getData());
            System.out.println("Data: " + newsInfo.getNews());
            Response response = new Response();
            response.setRequestNum(requestNum);
            response.setServiceNum(newsInfo.getServiceNum());
            outputStream.writeObject(response);
            System.out.println("Response sent");
            // TODO: log
        } catch (IOException e) {
            e.printStackTrace();
        }
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
