package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.clients.ClientsCats;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "cats")
public class Cats extends Animals {
    @Column(name = "breed")
    private CatsBreed breed;

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private ClientsCats volunteer;
    @ManyToOne
    @JoinColumn(name = "clients_cats_id", referencedColumnName = "id")
    private ClientsCats clientCat;

}
