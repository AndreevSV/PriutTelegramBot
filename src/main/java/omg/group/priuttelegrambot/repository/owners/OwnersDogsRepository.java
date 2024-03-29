package omg.group.priuttelegrambot.repository.owners;

import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnersDogsRepository extends JpaRepository<OwnerDog, Long>, OwnersDogsRepositoryCustom {

    @NotNull
    Optional<OwnerDog> findById(@NotNull Long id);

    Optional<OwnerDog> findByChatId(Long chatId);

    Optional<OwnerDog> findByIsVolunteerIsFalseAndChatId(Long chatId);

    List<OwnerDog> findByUserNameContainingIgnoreCase(String username);

    List<OwnerDog> findBySurnameContainingIgnoreCase(String surname);

    List<OwnerDog> findByTelephoneContainingIgnoreCase(String telephone);

//    Optional<OwnerDog> findByIVolunteerIsTrueAndChatId(Long chatId);

    Optional<OwnerDog> findByIsVolunteerIsTrueAndChatId(Long chatId);

    Optional<OwnerDog> findOwnerDogByVolunteer(OwnerDog volunteer);

}
