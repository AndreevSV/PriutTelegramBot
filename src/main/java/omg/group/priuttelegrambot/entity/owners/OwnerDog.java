package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "clients_dogs")
public class OwnerDog extends Owner {

    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private AddressOwnerDog address;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerDog volunteer;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
//    @ToString.Exclude
    private List<Dog> dogs;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OwnerDog ownerDog = (OwnerDog) o;
        return getId() != null && Objects.equals(getId(), ownerDog.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
