package omg.group.priuttelegrambot.util;

import omg.group.priuttelegrambot.entity.animals.Cats;
import omg.group.priuttelegrambot.entity.animals.Dogs;
import omg.group.priuttelegrambot.entity.clients.ClientsCats;
import omg.group.priuttelegrambot.entity.clients.ClientsDogs;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;


    public HibernateSessionFactoryUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration
                        .addAnnotatedClass(Cats.class)
                        .addAnnotatedClass(Dogs.class)
                        .addAnnotatedClass(ClientsCats.class)
                        .addAnnotatedClass(ClientsDogs.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }
        return sessionFactory;
    }
}
