package server.rmi;

import server.NewsBulletin;
import shared.logger.Logger;

import java.io.IOException;

public class NewsBulletinRmiWrapperImpl implements NewsBulletinRmiWrapper {
    private NewsBulletin newsBulletin;
    private Logger readLogger;
    private Logger writeLogger;

    public NewsBulletinRmiWrapperImpl(Logger readLogger, Logger writeLogger) {
        this.newsBulletin = new NewsBulletin();
        this.readLogger = readLogger;
        this.writeLogger = writeLogger;
    }

    @Override
    public NewsBulletin.NewsInfo getCurrentNews(int clientID) {
        NewsBulletin.NewsInfo newsInfo = this.newsBulletin.getCurrentNews();

        try {
            this.readLogger.writeToFile(new String[]{Integer.toString(newsInfo.getServiceNum()),
                    Integer.toString(newsInfo.getNews()),
                    Integer.toString(clientID),
                    Integer.toString(newsInfo.getNumOfReaders())});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsInfo;
    }

    @Override
    public NewsBulletin.NewsInfo setCurrentNews(int news, int clientID) {
        NewsBulletin.NewsInfo newsInfo = this.newsBulletin.setCurrentNews(news);

        try {
            writeLogger.writeToFile(new String[]{Integer.toString(newsInfo.getServiceNum()),
                    Integer.toString(newsInfo.getNews()),
                    Integer.toString(clientID)});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsInfo;
    }

    public int getNumRequests() {
        return this.newsBulletin.getNumOfRequests();
    }
}
