import java.util.*;

class NewsAgency implements subject {
    private List<observer> observers = new ArrayList<>();
    private String news;

    public void subscribe(observer o) {
        observers.add(o);
    }

    public void unsubscribe(observer o) {
        observers.remove(o);
    }

    public void setNews(String news) {
        this.news = news;
        notifyObservers();   
    }

    public void notifyObservers() {
        for (observer o : observers) {
            o.update(news);
        }
    }
}