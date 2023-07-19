package omg.group.priuttelegrambot.entity.pets;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "cats")
public class Cat extends Pet {

    @Column(name = "breed")
    @Enumerated(EnumType.ORDINAL)
    private CatsBreed breed;

    @OneToOne
    @JoinColumn(name = "id_volunteer", referencedColumnName = "id")
    private OwnerCat volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_cats_id", referencedColumnName = "id")
    private OwnerCat ownerCat;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Cat cat = (Cat) o;
        return getId() != null && Objects.equals(getId(), cat.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}
