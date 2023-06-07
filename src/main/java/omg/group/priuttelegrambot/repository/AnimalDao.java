package omg.group.priuttelegrambot.repository;

import jakarta.transaction.Transactional;
import omg.group.priuttelegrambot.entity.animals.Cat;
//import omg.group.priuttelegrambot.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class AnimalDao {

    private final SessionFactory sessionFactory;

    public AnimalDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Transactional
    public void save(Cat cat) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(cat);
    }

}

