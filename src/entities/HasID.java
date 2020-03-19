package entities;

public interface HasID<T> {
    T getId();
    void setId(T id);
}
