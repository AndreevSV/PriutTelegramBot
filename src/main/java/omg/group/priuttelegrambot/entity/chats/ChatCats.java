package omg.group.priuttelegrambot.entity.chats;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "chat_cats")
public class ChatCats extends Chat {

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "chat_id")
    private OwnerCat owner;

    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "chat_id")
    private OwnerCat volunteer;

}
