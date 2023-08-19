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
    public Optional<OwnerDog> findVolunteerByVolunteerIsTrueAndNoChatsOpened() {

        String hql = "FROM OwnerDog WHERE isVolunteer = true AND (volunteerChatOpened = false OR volunteerChatOpened = null)";

        return entityManager.createQuery(hql, OwnerDog.class)
                .getResultList()
                .stream().findAny();
    }
}
