package omg.group.priuttelegrambot.entity.chats;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "chat_cats")
public class ChatCats extends Chat {

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "chat_id")
    private OwnerCat ownerCat;

    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "chat_id")
    private OwnerCat volunteerCat;

}
