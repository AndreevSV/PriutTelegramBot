package omg.group.priuttelegrambot.entity.clients;

import jakarta.persistence.*;
import omg.group.priuttelegrambot.entity.animals.Cats;

import java.util.Collection;

@Entity
@Table(name = "clients_cats")
public class ClientsCats extends Clients {
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ClientsCats volunteer;

    @OneToMany(mappedBy = "clientCat")
    private Collection<Cats> cats;


}
