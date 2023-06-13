package omg.group.priuttelegrambot.entity.animals;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type_of_animal")
    private AnimalType animalType;
    @Column(name = "animal_sex")
    private Sex sex;
    @Column(name = "nickname")
    private String nickName;
    @Column(name = "birthday")
    private LocalDateTime birthday;
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
