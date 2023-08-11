package omg.group.priuttelegrambot.repository.owners.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OwnersCatsRepositoryCustomImpl implements OwnersCatsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OwnerCat> findVolunteerByVolunteerIsTrueAndNoChatsOpened() {

        String hql = "FROM OwnerCat WHERE isVolunteer = true AND volunteerChatOpened = false";

        return entityManager.createQuery(hql, OwnerCat.class)
                .getResultList()
                .stream().findAny();
    }
}
