package store;

import model.User;

import java.util.Collection;

public interface Store extends AutoCloseable {

    <T> Collection<T> findAll(Class<T> item);
    <T> T save(T item);
    <T> boolean delete(T item);
    User findUserByEmail(String email);
    <T> T findById(Class<T> item, int id);

}
