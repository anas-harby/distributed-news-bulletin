package server;

import shared.logger.Logger;
import shared.message.PostRequest;
import shared.message.Request;
import shared.message.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerThread implements Runnable {
    private NewsBulletin newsBulletin;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private Logger readLogger;
    private Logger writeLogger;

    public WorkerThread(NewsBulletin newsBulletin, Socket socket, Logger readLogger, Logger writeLogger) {
        init(newsBulletin, socket, readLogger, writeLogger);
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

    private void init(NewsBulletin newsBulletin, Socket socket, Logger readLogger, Logger writeLogger) {
        this.newsBulletin = newsBulletin;

        this.socket = socket;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.readLogger = readLogger;
        this.writeLogger = writeLogger;
    }

    private void handleGetRequest(Request request) {
        try {
            NewsBulletin.NewsInfo newsInfo = newsBulletin.getCurrentNews();
            Response response = new Response();
            response.setRequestNum(newsInfo.getRequestNum());
            response.setServiceNum(newsInfo.getServiceNum());
            response.setData(newsInfo.getNews());
            outputStream.writeObject(response);
            System.out.println("Response sent");

            readLogger.writeToFile(new String[]{Integer.toString(newsInfo.getServiceNum()),
                    Integer.toString(newsInfo.getNews()),
                    Integer.toString(request.getClientID()),
                    Integer.toString(newsInfo.getNumOfReaders())});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostRequest(Request request) {
        try {
            NewsBulletin.NewsInfo newsInfo = newsBulletin.setCurrentNews(((PostRequest) request).getData());
            System.out.println("Data: " + newsInfo.getNews());
            Response response = new Response();
            response.setRequestNum(newsInfo.getRequestNum());
            response.setServiceNum(newsInfo.getServiceNum());
            outputStream.writeObject(response);
            System.out.println("Response sent");

            writeLogger.writeToFile(new String[]{Integer.toString(newsInfo.getServiceNum()),
                    Integer.toString(newsInfo.getNews()),
                    Integer.toString(request.getClientID())});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void terminate() {
        try {
            socket.close();
            System.out.println("--HttpClient connection closed--");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
