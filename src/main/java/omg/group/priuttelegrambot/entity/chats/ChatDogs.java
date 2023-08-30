package omg.group.priuttelegrambot.entity.chats;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "chat_dogs")
public class ChatDogs extends Chat {

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "chat_id")
    private OwnerDog owner;

    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "chat_id")
    private OwnerDog volunteer;

}
