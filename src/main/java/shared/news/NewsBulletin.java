package shared.news;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NewsBulletin {
    public class NewsInfo {

        private int serviceNum;
        private int news;

        NewsInfo(int serviceNum, int news) {
            this.serviceNum = serviceNum;
            this.news = news;
        }

        public int getServiceNum() {
            return serviceNum;
        }

        public int getNews() {
            return news;
        }
    }

    private AtomicInteger serviceNum;
    private ReadWriteLock readWriteLock;
    private int news = 0;

    public NewsBulletin() {
        this.serviceNum = new AtomicInteger(1);
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public NewsInfo getCurrentNews() {
        this.readWriteLock.readLock().lock();
        NewsInfo info = new NewsInfo(this.serviceNum.getAndIncrement(), this.news);
        this.readWriteLock.readLock().unlock();
        return info;
    }

    public NewsInfo setCurrentNews(int news) {
        this.readWriteLock.writeLock().lock();
        this.news = news;
        NewsInfo info = new NewsInfo(this.serviceNum.getAndIncrement(), news);
        this.readWriteLock.writeLock().unlock();
        return info;
    }
}