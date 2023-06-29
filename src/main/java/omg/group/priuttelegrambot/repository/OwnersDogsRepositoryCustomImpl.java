package omg.group.priuttelegrambot.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

import java.util.Optional;

public class OwnersDogsRepositoryCustomImpl implements OwnersDogsRepositoryCustom  {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OwnerDog> findFirstByVolunteerIsTrue() {

        String hql = "FROM OwnerDog WHERE isVolunteer = true";

        OwnerDog owner = entityManager
                .createQuery(hql,OwnerDog.class)
                .getSingleResult();

        return Optional.ofNullable(owner);
    }
}
