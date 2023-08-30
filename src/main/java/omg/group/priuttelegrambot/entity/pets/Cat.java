package omg.group.priuttelegrambot.entity.pets;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.flags.OwnersCatsFlags;
import omg.group.priuttelegrambot.entity.pets.petsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
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
    private OwnerCat owner;

    @OneToMany(mappedBy = "cat", cascade = CascadeType.ALL)
    private List<OwnersCatsFlags> flags;


}
