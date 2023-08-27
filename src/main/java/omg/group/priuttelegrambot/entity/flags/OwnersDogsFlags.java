package omg.group.priuttelegrambot.entity.flags;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "flags_dogs")
public class OwnersDogsFlags extends Flags {
    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private OwnerDog owner;
    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerDog volunteer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Dog dog;
}
