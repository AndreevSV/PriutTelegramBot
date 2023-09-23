package omg.group.priuttelegrambot.entity.reports;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "reports_cats")
public class ReportCat extends Report {

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private OwnerCat owner;

    @ManyToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id")
    private Cat pet;

}
