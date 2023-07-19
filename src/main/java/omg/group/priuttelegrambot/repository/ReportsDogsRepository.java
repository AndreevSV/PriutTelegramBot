package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.ReportCat;
import omg.group.priuttelegrambot.entity.report.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportsDogsRepository extends JpaRepository<ReportDog, Long> {

    Optional<ReportDog> findByClientIdAndAnimalIdAndDateReport(Long clientId, Long animalId, LocalDate dateReport);

    Optional<ReportCat> findByPath(String path);
}
