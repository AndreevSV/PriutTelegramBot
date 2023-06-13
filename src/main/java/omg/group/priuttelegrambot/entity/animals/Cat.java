package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.CatsBreed;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "cats")
public class Cat extends Animal {

    @Column(name = "breed")
    private CatsBreed breed;

    @OneToOne
    @JoinColumn(name = "id_volunteer", referencedColumnName = "id")
    private OwnerCat volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_cats_id", referencedColumnName = "id")
    private OwnerCat ownerCat;

}
