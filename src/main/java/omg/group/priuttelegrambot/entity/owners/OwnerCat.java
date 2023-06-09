package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.Cat;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "clients_cats")
public class OwnerCat extends Owner {

    @Column(name = "cat_id")
    private Long catId;

//    @OneToOne
//    @JoinColumn(name = "id", referencedColumnName = "id")
//    private OwnerCat volunteer;

    @OneToMany(mappedBy = "clientCat")
    private Collection<Cat> cats;


}
