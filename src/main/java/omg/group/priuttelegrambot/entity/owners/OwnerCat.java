package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import omg.group.priuttelegrambot.entity.addresses.AddressOwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "clients_cats")
public class OwnerCat extends Owner {
    @ManyToOne
    @JoinColumn(name = "address", referencedColumnName = "id")
    private AddressOwnerCat address;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerCat volunteer;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Cat> cats;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OwnerCat ownerCat = (OwnerCat) o;
        return getId() != null && Objects.equals(getId(), ownerCat.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}