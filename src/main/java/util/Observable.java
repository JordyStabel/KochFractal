package util;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private List<Observer> observers = new ArrayList<>();

    /** Add an observer to the list */
    public void subscribe(Observer observer){
        observers.add(observer);
    }

    /** Remove an observer to the list */
    public void unSubscribe(Observer observer){
        observers.remove(observer);
    }

    /** Loop through all observers and update them */
    public void notifyObservers(Object object){
        for (Observer observer : observers){
            observer.update(object);
        }
    }
}
