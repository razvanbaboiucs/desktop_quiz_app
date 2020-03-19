package repositories;

import entities.HasID;

import java.util.Collection;

public interface Repository<T, E extends HasID<T>> {
    Collection<E> getAll();
}
