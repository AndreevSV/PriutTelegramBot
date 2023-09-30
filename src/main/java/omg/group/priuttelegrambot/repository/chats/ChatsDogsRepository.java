package omg.group.priuttelegrambot.repository.chats;

import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatsDogsRepository extends JpaRepository<ChatDogs, Long> {

    Optional<ChatDogs> findByOwner(OwnerDog owner);

    Optional<ChatDogs> findByVolunteer(OwnerDog volunteer);
}
