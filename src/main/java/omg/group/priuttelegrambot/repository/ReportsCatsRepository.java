package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<ReportCat, Long> {

    Optional<ReportCat> findByClientIdAndAnimalIdAndDateReport(Long clientId, Long animalId, LocalDate dateReport);

    Optional<ReportCat> findByPath(String path);

    Optional<ReportCat> findByDateReport(LocalDate date);



}
