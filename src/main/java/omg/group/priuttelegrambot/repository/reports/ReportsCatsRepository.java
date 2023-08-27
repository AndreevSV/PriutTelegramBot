package omg.group.priuttelegrambot.repository.reports;

import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<ReportCat, Long> {

    Optional<ReportCat> findReportCatByOwnerAndCatAndDateReport(OwnerCat owner, Cat cat, LocalDate date);
    Optional<ReportCat> findByDateReport(LocalDate date);

    Optional<ReportCat> findByFileId(String fileId);

    Optional<ReportCat> findByHashOfPhoto(int hashOfPhoto);

}
