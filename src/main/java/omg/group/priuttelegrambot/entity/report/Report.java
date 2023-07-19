package omg.group.priuttelegrambot.entity.report;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
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

    @Column(name = "date_of_report")
    private LocalDate dateReport;

    @Column(name = "date_of_last_report")
    private LocalDate dateLastReport;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Report report = (Report) o;
        return getId() != null && Objects.equals(getId(), report.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
