package omg.group.priuttelegrambot.repository.chats;

import omg.group.priuttelegrambot.entity.chats.ChatCats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatsCatsRepository extends JpaRepository<ChatCats, Long> {

    Optional<ChatCats> findByOwnerCatChatId(Long ownerCatId);

    Optional<ChatCats> findByVolunteerCatChatId(Long volunteerCatId);

    List<ChatCats> findByIsChattingFalseAndVolunteerCatId(Long volunteerCatId);

    List<ChatCats> findByIsChattingTrueAndVolunteerCatId(Long volunteerCatId);

    Optional<ChatCats> findByIsChattingTrueAndOwnerCatId(Long ownerCatId);

}
