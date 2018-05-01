package no.ntnu.diverslogbook.util;

/**
 * A Functional interface for the {@link ObserverManager}.
 * @param <T> The class to receive
 * @author Lars Johan
 * @see ObserverManager
 */
public interface Observer<T> {
    boolean update(T changedObject);
}
