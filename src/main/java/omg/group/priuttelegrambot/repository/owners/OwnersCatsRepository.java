package omg.group.priuttelegrambot.repository.owners;


import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnersCatsRepository extends JpaRepository<OwnerCat, Long>, OwnersCatsRepositoryCustom {

    @NotNull
    Optional<OwnerCat> findById(@NotNull Long id);

    Optional<OwnerCat> findByChatId(Long chatId);

    List<OwnerCat> findByUserNameContainingIgnoreCase(String username);

    List<OwnerCat> findBySurnameContainingIgnoreCase(String surname);

    List<OwnerCat> findByTelephoneContainingIgnoreCase(String telephone);

    Optional<OwnerCat> findByVolunteerIsTrueAndChatId(Long chatId);


}
