package omg.group.priuttelegrambot.repository.chats;

import omg.group.priuttelegrambot.entity.chats.ChatDogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatsDogsRepository extends JpaRepository<ChatDogs, Long> {

    Optional<ChatDogs> findByChatId(Long chatId);

    Optional<ChatDogs> findByOwnerDogId(Long ownerDogId);

    Optional<ChatDogs> findByVolunteerDogId(Long volunteerDogId);
}
