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
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Map<String, String>> maps = new ArrayList<>();
        for (Body value : Body.values()) {
            Map<String, String> map = Map.of("id", value.name(), "name", value.name());
            maps.add(map);
        }
        System.out.println(mapper.writeValueAsString(maps));
        /*
        BasicConfigurator.configure();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();

            List<Marka> list = (ArrayList<Marka>) HbmStore.instOf().findAll(Marka.class);
            list.forEach(l-> l.setModels(null));
            ObjectMapper mapper = new ObjectMapper();
            String string = mapper.writeValueAsString(list);
            int markaId = 1;
            Marka marka = HbmStore.instOf().findById(Marka.class, markaId);
            Collection<Model> list2 = HbmStore.instOf().findModelByMarka(marka);
            list2.forEach(l -> l.setMarka(null));
             string = mapper.writeValueAsString(list2);
            System.out.println(string);
            mapper.writeValueAsString(Body.values());
           /* Marka marka = session.load(Marka.class, 1);
            Model model = session.load(Model.class, 1);
            Car car = Car.of(marka, model, Body.Седан, Gear.Ручная, EngineType.Газ, Privod.Задний, 1986, 155000, 75000.0d);

            session.persist(car);
            Car car2 = Car.of(marka, model, Body.Седан, Gear.Ручная, EngineType.Газ, Privod.Задний, 1988, 234000, 15000.0d);

            session.persist(car2);
*/


         /*   session.getTransaction().commit();
            session.close();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }*/
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
