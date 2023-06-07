package omg.group.priuttelegrambot.entity.clients;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.Cats;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "clients_cats")
public class ClientsCats extends Clients {

    @Column(name = "cat_id")
    private Long catId;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ClientsCats volunteer;

    @OneToMany(mappedBy = "clientCat")
    private Collection<Cats> cats;


}
