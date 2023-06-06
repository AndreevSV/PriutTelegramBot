package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.clients.ClientsDogs;

@Data
@RequiredArgsConstructor
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
