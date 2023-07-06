package omg.group.priuttelegrambot.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.OwnersCatsRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OwnersCatsRepositoryCustomImpl implements OwnersCatsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OwnerCat> findFirstByVolunteerIsTrue() {

        String hql = "FROM OwnerCat WHERE isVolunteer = true";

        OwnerCat owner = entityManager
                .createQuery(hql,OwnerCat.class)
                .getSingleResult();

        return Optional.ofNullable(owner);
    }
}
