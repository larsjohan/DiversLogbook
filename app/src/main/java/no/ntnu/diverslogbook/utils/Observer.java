package no.ntnu.diverslogbook.utils;

/**
 * A Functional interface for the {@link ObserverManager}.
 * @param <T> The class to receive
 * @author Lars Johan
 * @see ObserverManager
 */
public interface Observer<T> {
    boolean update(T changedObject);
}
