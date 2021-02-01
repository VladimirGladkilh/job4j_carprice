package model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.BasicConfigurator;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import store.HbmStore;

import java.util.List;
import java.util.StringJoiner;

public class HbmRun {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Session session = sf.openSession();
            session.beginTransaction();
         //   Collection<Car> cars = HbmStore.instOf().findAll(Car.class);
         //   cars.forEach(System.out::println);

            List<Marka> list = (List<Marka>) HbmStore.instOf().findAll(Marka.class);
            list.forEach(l -> {
                System.out.println(l.getName());
                l.getModels().forEach(System.out::println);
            });
            //List<Model> list2 = (List<Model>) HbmStore.instOf().findAll(Model.class);
           /* ObjectMapper mapper = new ObjectMapper();
            StringJoiner string = new StringJoiner(System.lineSeparator());
            string.add(mapper.writeValueAsString(list));
           // string.add(mapper.writeValueAsString(list2));
            System.out.println(string.toString());*/

           /* Marka marka = session.load(Marka.class, 1);
            Model model = session.load(Model.class, 1);
            Car car = Car.of(marka, model, Body.Седан, Gear.Ручная, EngineType.Газ, Privod.Задний, 1986, 155000, 75000.0d);

            session.persist(car);
            Car car2 = Car.of(marka, model, Body.Седан, Gear.Ручная, EngineType.Газ, Privod.Задний, 1988, 234000, 15000.0d);

            session.persist(car2);
*/


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
