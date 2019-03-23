package shared.shared.news;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NewsBulletin {
    //TODO: Check if generalizing this class is preferable.
    public class NewsInfo {
        int requestNum;
        int serviceNum;
        int news;
    }

    private AtomicInteger requestNum = null;
    private AtomicInteger serviceNum = null;
    private ReadWriteLock readWriteLock = null;
    private int news = 0;

    public NewsBulletin() {
        this.requestNum = new AtomicInteger(0);
        this.serviceNum = new AtomicInteger(0);
        this.readWriteLock = new ReentrantReadWriteLock();
    }

    public NewsInfo getCurrentNews() {
        NewsInfo info = new NewsInfo();
        info.requestNum = this.requestNum.getAndIncrement();

        this.readWriteLock.readLock().lock();
        info.serviceNum = this.serviceNum.getAndIncrement();
        info.news = this.news;
        this.readWriteLock.readLock().unlock();

        return info;
    }

    public NewsInfo setCurrentNews(int news) {
        NewsInfo info = new NewsInfo();
        info.requestNum = this.requestNum.getAndIncrement();

        this.readWriteLock.writeLock().lock();
        info.serviceNum = this.serviceNum.getAndIncrement();
        this.news = news;
        info.news = this.news;
        this.readWriteLock.writeLock().unlock();

        return info;
    }
}
