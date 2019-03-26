package server;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NewsBulletin {
    public class NewsInfo implements Serializable {
        private int serviceNum;
        private int news;
        private int numOfReaders;

        NewsInfo(int serviceNum, int news, int numOfReaders) {
            this.serviceNum = serviceNum;
            this.news = news;
            this.numOfReaders = numOfReaders;
        }

        public int getServiceNum() {
            return serviceNum;
        }

        public int getNews() {
            return news;
        }

        public int getNumOfReaders() {
            return numOfReaders;
        }
    }

    private AtomicInteger serviceNum;
    private ReentrantReadWriteLock readWriteLock;
    private int news = -1;

    public NewsBulletin() {
        this.serviceNum = new AtomicInteger(1);
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public NewsInfo getCurrentNews() {
        this.readWriteLock.readLock().lock();
        NewsInfo info = new NewsInfo(this.serviceNum.getAndIncrement(), this.news, readWriteLock.getReadLockCount());
        this.readWriteLock.readLock().unlock();
        return info;
    }

    public NewsInfo setCurrentNews(int news) {
        this.readWriteLock.writeLock().lock();
        this.news = news;
        NewsInfo info = new NewsInfo(this.serviceNum.getAndIncrement(), news, 0);
        this.readWriteLock.writeLock().unlock();
        return info;
    }
}