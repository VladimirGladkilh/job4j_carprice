package store;

import model.Car;
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

import java.util.Calendar;
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
            session.close();
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
             session.createQuery("select c from Marka c fetch all properties ").list()
        );
    }

    @Override
    public Collection<Car> findAllCar() {
        return this.tx(session ->
                session.createQuery("select c from Car c fetch all properties ").list()
        );
    }

    @Override
    public Collection<Car> findCarByDay() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);;
        today.set(Calendar.SECOND, 0);
        return this.tx(session ->
                session.createQuery("select c from Car c fetch all properties where c.created>=:data ")
                        .setParameter("data", today.getTime())
                        .list()
        );
    }

    @Override
    public Collection<Car> findCarWithPhoto() {
        return this.tx(session ->
                session.createQuery("select c from Car c fetch all properties where c.photos.size > 0 ")
                        .list()
        );
    }

    @Override
    public Collection<Car> findCarByMarka(Marka marka) {
        return this.tx(session ->
                session.createQuery("select c from Car c fetch all properties where c.marka=:marka ")
                        .setParameter("marka", marka)
                        .list()
        );
    }

    @Override
    public <T> T save(T item) {
        return this.tx(session -> {
            session.saveOrUpdate(item);
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
            final Query<T> query = session.createQuery("from " + item.getName() + " e where e.id=:id", item);
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
