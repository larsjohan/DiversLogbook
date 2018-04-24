package no.ntnu.diverslogbook.util;

import java.util.ArrayList;
import java.util.List;

public abstract class Observer<T> implements Callback<T>{

    public static class ObserverManager {
        private List<Observer> observers;

        public ObserverManager() {
            observers = new ArrayList<>();
        }

        public void register(Observer observer) {
            this.observers.add(observer);
        }
        public void unregister(Observer observer) {
            this.observers.remove(observer);
        }

        public void notifyChange(T changedObject){
            for(Observer observer : this.observers) {
                observer.update(changedObject);
            }
        }

    }

}
