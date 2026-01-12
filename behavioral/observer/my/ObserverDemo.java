import java.util.List;
import java.util.ArrayList;

// Observer interface
interface Observer {
    void update(String state);
}

public class ObserverDemo {
    
    public static void main(String[] args) {
        System.out.println("Observer Pattern Demo");
        Subject sub  = new Subject();
        Observer ob1 = new Observerone();
        Observer ob2 = new Observertwo();

        sub.attach(ob1);
        sub.attach(ob2);

        sub.setState("Initial State");
        sub.setState("Updated State");
    }
}

class Observerone implements Observer {
    
    @Override
    public void update(String state) {
        System.out.println("ObserverOne received update: " + state);
    }
}


class Observertwo implements Observer {
    
    @Override
    public void update(String state) {
        System.out.println("Observertwo received update: " + state);
    }
}

class Subject {
    
    private List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers(String state) {
        for (Observer observer : observers) {
            observer.update(state);
        }
    }
    
    public void setState(String state) {
        System.out.println("Subject state changed to: " + state);
        notifyObservers(state);
    }
}