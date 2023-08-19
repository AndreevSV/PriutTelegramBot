package omg.group.priuttelegrambot.entity.reports;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "id_client")
    private Long clientId;

    @Column(name = "id_animal")
    private Long animalId;

    @Column(name = "file_id")
    private String fileId;

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

    @Column(name = "date_of_report")
    private LocalDate dateReport;

    @Column(name = "date_of_last_report")
    private LocalDate dateLastReport;

    @Column(name = "hash_code")
    private int hashOfPhoto;

}
