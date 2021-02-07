package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import store.HbmStore;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class HbmRun {

    public static void main(String[] args) throws JsonProcessingException {

        BasicConfigurator.configure();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            Marka marka = new Marka();
            marka.setName("LADA");

            List<Model> models = List.of(Model.of("2101", marka), Model.of("2110", marka), Model.of("GRANTA", marka),
                    Model.of("KALINA", marka), Model.of("VESTA", marka), Model.of("PRIORA", marka) );
            models.forEach(session::persist);
            marka.setModels(models);
            session.persist(marka);



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
