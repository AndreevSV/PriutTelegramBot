package omg.group.priuttelegrambot.repository.owners.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepositoryCustom;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OwnersCatsRepositoryCustomImpl implements OwnersCatsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<OwnerCat> findVolunteerByVolunteerIsTrueAndChatsOpenedMinimum() {

        String hql = "FROM OwnerCat WHERE isVolunteer = true ORDER BY chatsOpened ASC";

        List<OwnerCat> owners = entityManager.createQuery(hql, OwnerCat.class)
                .setMaxResults(1)
                .getResultList();

        return owners.isEmpty() ? Optional.empty() : Optional.of(owners.get(0));
    }
}
