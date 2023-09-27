package omg.group.priuttelegrambot.repository.reports;

import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.reports.ReportDog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportsDogsRepository extends JpaRepository<ReportDog, Long> {
    Optional<ReportDog> findByOwnerAndPetAndDateOfReport(OwnerDog owner, Dog dog, LocalDate date);
    Optional<ReportDog> findByDateOfReport(LocalDate date);
    Optional<ReportDog>  findByFileId(String fileId);
    Optional<ReportDog> findByHashCodeOfPhoto(int hashOfPhoto);

    @Query(value = "SELECT * FROM reports_dogs r WHERE :currentDate = date_trunc('day', r.created_at) AND (r.file_id IS NULL OR r.ration IS NULL OR r.feeling IS NULL OR r.changes IS NULL)", nativeQuery = true)
    List<ReportDog> findAllReportsThatNotFullfilled(LocalDateTime currentDate);
}
