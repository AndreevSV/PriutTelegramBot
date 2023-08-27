package omg.group.priuttelegrambot.entity.pets;

import jakarta.persistence.*;
import lombok.*;
import omg.group.priuttelegrambot.entity.pets.petsenum.PetType;
import omg.group.priuttelegrambot.entity.pets.petsenum.Sex;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_of_animal")
    @Enumerated(EnumType.ORDINAL)
    private PetType animalType;

    @Column(name = "animal_sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @Column(name = "nickname")
    private String nickName;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date birthday;

    @Column(name = "disabilities")
    private Boolean disabilities;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "date_outcome")
    private LocalDateTime dateOutcome;

    @Column(name = "photo_path")
    private String photoPath;

    @Column(name = "first_probation")
    private Boolean firstProbation;

    @Column(name = "second_probation")
    private Boolean secondProbation;

    @Column(name = "probation_starts")
    private LocalDate probationStarts;

    @Column(name = "probation_ends")
    private LocalDate probationEnds;

    @Column(name = "passed_first_probation")
    private Boolean passedFirstProbation;

    @Column(name = "passed_second_probation")
    private Boolean passedSecondProbation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Pet pet = (Pet) o;
        return getId() != null && Objects.equals(getId(), pet.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
