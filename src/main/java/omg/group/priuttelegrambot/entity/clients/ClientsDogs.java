package omg.group.priuttelegrambot.entity.clients;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.Dogs;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
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
