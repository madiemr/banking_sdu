import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<User> observers = new ArrayList<>();

    public void addObserver(User user) {
        observers.add(user);
    }

    public void removeObserver(User user) {
        observers.remove(user);
    }

    public void notifyObservers(String message) {
        for (User user : observers) {
            System.out.println("Notification for " + user.getName() + ": " + message);
        }
    }
}