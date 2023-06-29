package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.CatsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<CatsReport, Long> {

    Optional<CatsReport> findByClientIdAndAnimalIdAndCreatedAt(Long clientId, Long animalId, LocalDate createdAt);

}
