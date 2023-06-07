package omg.group.priuttelegrambot.util;

//public class HibernateSessionFactoryUtil {
//
//    private static SessionFactory sessionFactory;
//
//
//    public HibernateSessionFactoryUtil() {
//    }
//
//    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null) {
//            try {
//                Configuration configuration = new Configuration().configure();
//                configuration
//                        .addAnnotatedClass(Cat.class)
//                        .addAnnotatedClass(Dog.class)
//                        .addAnnotatedClass(OwnerCat.class)
//                        .addAnnotatedClass(OwnerDog.class);
//                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
//                sessionFactory = configuration.buildSessionFactory(builder.build());
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//
//            }
//        }
//        return sessionFactory;
//    }
//}
