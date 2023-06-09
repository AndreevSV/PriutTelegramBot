package omg.group.priuttelegrambot.repository.impl;

import omg.group.priuttelegrambot.configuration.HibernateSessionFactoryUtil;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.repository.AnimalsRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class AnimalsCatsRepositoryImpl implements AnimalsRepository {


    @Override
    public void save(Cat cat) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(cat);
            transaction.commit();
        }

    }


}

