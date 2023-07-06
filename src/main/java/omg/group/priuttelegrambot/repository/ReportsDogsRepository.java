package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.CatsReport;
import omg.group.priuttelegrambot.entity.report.DogsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportsDogsRepository extends JpaRepository<DogsReport, Long> {

    Optional<DogsReport> findByClientIdAndAnimalIdAndDateReport(Long clientId, Long animalId, LocalDate dateReport);

    Optional<CatsReport> findByPath(String path);
}
