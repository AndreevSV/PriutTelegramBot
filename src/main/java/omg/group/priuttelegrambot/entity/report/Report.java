package omg.group.priuttelegrambot.entity.report;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_client")
    private Long clientId;

    @Column(name = "id_animal")
    private Long animalId;

    @Column(name = "path")
    private String path;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "ration")
    private String ration;

    @Column(name = "feeling")
    private String feeling;

    @Column(name = "changes")
    private String changes;

}
