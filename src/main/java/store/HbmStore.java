package store;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import java.util.Collection;
import java.util.function.Function;

public class HbmStore implements Store {
    private final StandardServiceRegistry registry;
    private final SessionFactory sf;

    public HbmStore() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public <T> Collection<T> findAll(Class<T> item) {
        return this.tx(session -> {
            final Query<T> query = session.createQuery("from "+ item.getName(), item);
            return query.list();
        });
    }

    @Override
    public <T> T save(T item) {
        return this.tx(session -> {
            session.persist(item);
            return item;
        });
    }

    @Override
    public <T> boolean delete(T item) {
        this.tx(session -> {
            session.delete(item);
            return true;
        });
        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        return this.tx(session -> {
            final Query<User> query = session.createQuery("from model.User m where m.email=:email");
            query.setParameter("email", email);
            return query.stream().findAny().orElse(null);
        });
    }

    @Override
    public <T> T findById(Class<T> item, int id) {
        return (T) this.tx(session -> {
            final Query<T> query = session.createQuery("from "+ item.getName() + " m where m.id=:id", item);
            query.setParameter("id", id);
            return query.stream().findAny().orElse(null);
        });
    }


}
