package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.AnimalType;
import omg.group.priuttelegrambot.entity.animals.enimalsenum.Sex;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_of_animal")
    @Enumerated(EnumType.ORDINAL)
    private AnimalType animalType;

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
}
