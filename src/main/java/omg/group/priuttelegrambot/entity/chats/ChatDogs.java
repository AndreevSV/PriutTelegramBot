package omg.group.priuttelegrambot.entity.chats;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "chat_dogs")
public class ChatDogs extends Chat {

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "chat_id")
    private OwnerDog ownerDog;

    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "chat_id")
    private OwnerDog volunteerDog;

}
