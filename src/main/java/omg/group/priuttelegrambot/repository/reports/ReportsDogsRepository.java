package omg.group.priuttelegrambot.repository.reports;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportsDogsRepository extends JpaRepository<ReportDog, Long> {
    Optional<ReportDog> findByOwnerAndPetAndDateOfReport(OwnerDog owner, Dog dog, LocalDate date);
    List<ReportDog> findByDateOfReport(LocalDate date);
    Optional<ReportDog>  findByFileId(String fileId);
    Optional<ReportDog> findByHashCodeOfPhoto(int hashOfPhoto);

    @Query("SELECT r FROM ReportDog r WHERE :currentDate = r.dateOfReport AND (r.fileId = NULL OR r.ration = NULL OR r.feeling = NULL OR r.changes = NULL)")
    List<ReportDog> findAllReportsThatNotFullfilled(LocalDate currentDate);
}
