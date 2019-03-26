package server.rmi;

import server.NewsBulletin;

public class NewsBulletinRmiWrapperImpl implements NewsBulletinRmiWrapper {
    private NewsBulletin newsBulletin;
    private int numRequests = 0;

    @Override
    public NewsBulletin.NewsInfo getCurrentNews() {
        this.numRequests++;
        //TODO: Logging
        return this.newsBulletin.getCurrentNews();
    }

    @Override
    public NewsBulletin.NewsInfo setCurrentNews(int news) {
        this.numRequests++;
        return this.newsBulletin.setCurrentNews(news);
    }

    public int getNumRequests() {
        return this.numRequests;
    }
}
