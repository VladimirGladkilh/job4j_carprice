package store;

import java.util.Collection;

public interface Store extends AutoCloseable {

    <T> Collection<T> findAll(Class<T> item);
    <T> T save(T item);
    <T> boolean delete(Class<T> item);

}
