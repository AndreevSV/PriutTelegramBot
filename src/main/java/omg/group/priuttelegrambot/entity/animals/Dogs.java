package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import omg.group.priuttelegrambot.entity.clients.Clients;
import omg.group.priuttelegrambot.entity.clients.ClientsCats;
import omg.group.priuttelegrambot.entity.clients.ClientsDogs;

@Entity
@Table(name = "dogs")
public class Dogs extends Animals {
    @Column(name = "breed")
    private DogsBreed breed;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ClientsDogs volunteer;

    @ManyToOne
    @JoinColumn(name = "clients_dogs_id", referencedColumnName = "id")
    private ClientsDogs clientDog;

}
