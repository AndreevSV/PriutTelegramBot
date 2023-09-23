package omg.group.priuttelegrambot.entity.flags;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "flags_cats")
public class OwnersCatsFlags extends Flags {
    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private OwnerCat owner;
    @OneToOne
    @JoinColumn(name = "volunteer_id", referencedColumnName = "id")
    private OwnerCat volunteer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "animal_id", referencedColumnName = "id")
    private Cat cat;


}
