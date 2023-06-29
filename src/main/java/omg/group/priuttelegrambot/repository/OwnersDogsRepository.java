package omg.group.priuttelegrambot.repository;

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

    @NotNull
    Optional<OwnerDog> findByChatId(@NotNull Long chatId);

    List<OwnerDog> findByUserNameContainingIgnoreCase(String username);

    List<OwnerDog> findBySurnameContainingIgnoreCase(String surname);

    List<OwnerDog> findByTelephoneContainingIgnoreCase(String telephone);

}
