package omg.group.priuttelegrambot.entity.reports;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "reports_dogs")
public class ReportDog extends Report {

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private OwnerDog owner;

    @ManyToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id")
    private Dog pet;



}
