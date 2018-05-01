package no.ntnu.diverslogbook.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps track of all the observers and notifies them when an update occurs.
 *
 * @author Lars Johan
 * @see Observer
 */
public class ObserverManager {

    /**
     * A List of observers to notify
     */
    private final List<Observer> observers;

    /**
     * Constructor.
     * Initializes the list
     */
    public ObserverManager() {
        observers = new ArrayList<>();
    }

    /**
     * Registers a new observer to be notified when something happens.
     * @param observer The Observer to register
     * @see Observer
     */
    public synchronized void register(Observer observer) {
        if(!this.observers.contains(observer)) {
            this.observers.add(observer);
        }
        Log.d("DiverApp", "Registered new observer: " + this.observers.size());
    }

    /**
     * Unregisters an observer.
     * Stops the observer from receiving updates
     * @param observer The Observer to unregister
     * @see Observer
     */
    public synchronized void unregister(Observer observer) {
        this.observers.remove(observer);
        Log.d("DiverApp", "UnRegistered observer: " + this.observers.size());
    }

    /**
     * Notify the observers that a change has occured.
     * @param changedObject The object that has changed
     * @param <T> The type of object that has changed
     * @see Observer
     */
    public synchronized <T> void notifyChange(T changedObject){
        Iterator<Observer> iterator = this.observers.iterator();
        while (iterator.hasNext()){
            Observer current = iterator.next();
            boolean removeObserver = current.update(changedObject);

            if(removeObserver){
                unregister(current);
            }
        }
    }

}

