package omg.group.priuttelegrambot.entity.reports;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "reports_cats")
public class ReportCat extends Report {

    @ManyToOne
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private OwnerCat owner;

    @ManyToOne
    @JoinColumn(name = "id_animal", referencedColumnName = "id")
    private Cat cat;

}
