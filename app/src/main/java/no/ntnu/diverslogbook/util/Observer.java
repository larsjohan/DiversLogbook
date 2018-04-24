package no.ntnu.diverslogbook.util;

public interface Callback<T> {
    void update(T changedObject);
}
