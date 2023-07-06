package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.CatsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<CatsReport, Long> {

    Optional<CatsReport> findByClientIdAndAnimalIdAndDateReport(Long clientId, Long animalId, LocalDate dateReport);

    Optional<CatsReport> findByPath(String path);

    Optional<CatsReport> findByDateReport(LocalDate date);



}
