package omg.group.priuttelegrambot.repository.owners.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OwnersDogsRepositoryCustomImpl implements OwnersDogsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OwnerDog> findVolunteerByVolunteerIsTrueAndChatsOpenedMinimum() {

        String hql = "FROM OwnerCat WHERE isVolunteer = true ORDER BY chatsOpened ASC";

        List<OwnerDog> owners = entityManager.createQuery(hql, OwnerDog.class)
                .setMaxResults(1)
                .getResultList();

        return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
    }
}
