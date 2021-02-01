package store;

import model.Marka;
import model.Model;
import model.User;

import java.util.Collection;

public interface Store extends AutoCloseable {

    <T> Collection<T> findAll(Class<T> item);
    Collection<Marka> findAllMarka();
    <T> T save(T item);
    <T> boolean delete(T item);
    User findUserByEmail(String email);
    <T> T findById(Class<T> item, int id);
    Collection<Model> findModelByMarka(Marka id);

}
