package omg.group.priuttelegrambot.repository.chats;

import omg.group.priuttelegrambot.entity.chats.ChatCats;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatsCatsRepository extends JpaRepository<ChatCats, Long> {

    Optional<ChatCats> findByOwner(OwnerCat owner);

    Optional<ChatCats> findByVolunteer(OwnerCat volunteer);

//    List<ChatCats> findByIsChattingFalseAndVolunteerChatId(Long volunteerCatId);
//
//    List<ChatCats> findByIsChattingTrueAndVolunteerChatId(Long volunteerCatId);
//
//    Optional<ChatCats> findByIsChattingTrueAndOwnerChatId(Long ownerCatId);

}
