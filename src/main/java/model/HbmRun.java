package model;

import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmRun {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Marka marka = Marka.of("LADA");
            session.persist(marka);
            Marka marka2 = Marka.of("BMW");
            session.persist(marka2);

            Model one = Model.of("2101", marka);
            session.persist(one);

            Model two = Model.of("2110", marka);
            session.persist(two);

            Model three = Model.of("Kalina", marka);
            session.persist(three);

            Model four = Model.of("Priora", marka);
            session.persist(four);

            Model five = Model.of("Vesta", marka);
            session.persist(five);

            Model b1 = Model.of("X5", marka2);
            session.persist(b1);

            Model b2 = Model.of("535i", marka2);
            session.persist(b2);


            session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static <T> T create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(model);
        session.getTransaction().commit();
        session.close();
        return model;
    }

    public static <T> List<T> findAll(Class<T> cl, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<T> list = session.createQuery("from " + cl.getName(), cl).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }
}
