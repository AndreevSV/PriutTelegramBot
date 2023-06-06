package omg.group.priuttelegrambot.entity.clients;

import jakarta.persistence.*;
import omg.group.priuttelegrambot.entity.animals.Dogs;

import java.util.Collection;

@Entity
@Table(name = "clients_dogs")
public class ClientsDogs extends Clients {
    @Column(name = "dog_id")
    private Long dogId;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ClientsDogs volunteer;

    @OneToMany(mappedBy = "clientDog")
    private Collection<Dogs> dogs;


}
