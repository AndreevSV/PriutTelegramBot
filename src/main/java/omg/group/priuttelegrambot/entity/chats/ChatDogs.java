package omg.group.priuttelegrambot.entity.chats;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "chat_dogs")
public class ChatDogs extends Chat {

    @OneToOne()
    @JoinColumn(name = "owner_id", referencedColumnName = "chat_id")
    private OwnerDog owner;

    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "chat_id")
    private OwnerDog volunteer;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ChatDogs chatDogs = (ChatDogs) o;
        return getId() != null && Objects.equals(getId(), chatDogs.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
