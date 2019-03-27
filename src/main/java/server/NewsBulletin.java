package server;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NewsBulletin implements Serializable {
    public class NewsInfo implements Serializable {
        private int requestNum;
        private int serviceNum;
        private int news;
        private int numOfReaders;

        NewsInfo(int requestNum, int serviceNum, int news, int numOfReaders) {
            this.requestNum = requestNum;
            this.serviceNum = serviceNum;
            this.news = news;
            this.numOfReaders = numOfReaders;
        }

        public int getRequestNum() {
            return requestNum;
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

    private AtomicInteger requestNum;
    private AtomicInteger serviceNum;
    private ReentrantReadWriteLock readWriteLock;
    private int news;

    public NewsBulletin() {
        this.requestNum = new AtomicInteger(1);
        this.serviceNum = new AtomicInteger(1);
        this.readWriteLock = new ReentrantReadWriteLock();
        this.news = -1;
    }

    public NewsInfo getCurrentNews() {
        int req = this.requestNum.getAndIncrement();
        this.readWriteLock.readLock().lock();
        NewsInfo info = new NewsInfo(req, this.serviceNum.getAndIncrement(), this.news, readWriteLock.getReadLockCount());
        this.readWriteLock.readLock().unlock();
        return info;
    }

    public NewsInfo setCurrentNews(int news) {
        int req = this.requestNum.getAndIncrement();
        this.readWriteLock.writeLock().lock();
        this.news = news;
        NewsInfo info = new NewsInfo(req, this.serviceNum.getAndIncrement(), news, 0);
        this.readWriteLock.writeLock().unlock();
        return info;
    }

    public int getNumOfRequests() {
        return this.requestNum.get();
    }
}