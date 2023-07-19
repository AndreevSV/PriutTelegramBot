package omg.group.priuttelegrambot.entity.pets;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.pets.petsenum.DogsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "dogs")
public class Dog extends Pet {

    @Column(name = "breed")
    @Enumerated(EnumType.ORDINAL)
    private DogsBreed breed;

    @OneToOne
    @JoinColumn(name = "id_volunteer", referencedColumnName = "id")
    private OwnerDog volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_dogs_id", referencedColumnName = "id")
    private OwnerDog ownerDog;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Dog dog = (Dog) o;
        return getId() != null && Objects.equals(getId(), dog.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
