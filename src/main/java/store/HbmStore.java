package store;

import model.Marka;
import model.Model;
import model.User;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.function.Function;

public class HbmStore implements Store {
    private final StandardServiceRegistry registry;
    private final SessionFactory sf;
    private static final Logger log = LoggerFactory.getLogger(HbmStore.class);

    public HbmStore() {
        BasicConfigurator.configure();
        log.info("Init store");
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
        log.info("Destroy store");
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
            log.error("Tr error ", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public <T> Collection<T> findAll(Class<T> item) {
        return this.tx(session -> {
            final Query<T> query = session.createQuery("from " + item.getName() + " fetch all properties", item);
            log.info("Find ALL");
            return query.list();
        });
    }

    @Override
    public Collection<Marka> findAllMarka() {
        return this.tx(session -> 
             session.createQuery("select c from Marka c join fetch c.models").list()
        );
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
            final Query<T> query = session.createQuery("from " + item.getName() + " m fetch all properties where m.id=:id", item);
            query.setParameter("id", id);
            return query.stream().findAny().orElse(null);
        });
    }

    @Override
    public Collection<Model> findModelByMarka(Marka id) {
        return this.tx(session -> {
            final Query<Model> query = session.createQuery("select distinct m from Model m where m.marka=:marka", Model.class);
            query.setParameter("marka", id);
            return query.list();
        });
    }


}
