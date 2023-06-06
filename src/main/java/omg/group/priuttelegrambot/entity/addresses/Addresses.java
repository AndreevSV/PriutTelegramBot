package omg.group.priuttelegrambot.entity.addresses;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.clients.ClientsCats;
import omg.group.priuttelegrambot.entity.clients.ClientsDogs;

import java.util.Collection;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "addresses")
public class Addresses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "index")
    private int index;
    @Column(name = "country")
    private String country;
    @Column(name = "region")
    private String region;
    @Column(name = "city")
    private String city;
    @Column(name = "street")
    private String street;
    @Column(name = "house")
    private int house;
    @Column(name = "letter")
    private char letter;
    @Column(name = "building")
    private int building;
    @Column(name = "flat")
    private int flat;

    @OneToMany(mappedBy = "address")
    private Collection<ClientsDogs> clientsDogs;

    @OneToMany(mappedBy = "address")
    private Collection<ClientsCats> clientsCats;

}
