package omg.group.priuttelegrambot.entity.photo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Photos {
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

}
