package omg.group.priuttelegrambot.repository.reports;

import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.reports.ReportCat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<ReportCat, Long> {

    List<ReportCat> findByDateOfReport(LocalDate date);
    Optional<ReportCat> findByFileId(String fileId);

    Optional<ReportCat> findByHashCodeOfPhoto(int hashOfPhoto);

    List<ReportCat> findByOwner(OwnerCat owner);

    List<ReportCat> findByOwnerAndPet(OwnerCat owner, Cat pet);

    Optional<ReportCat> findByOwnerAndPetAndDateOfReport(OwnerCat owner, Cat pet, LocalDate date);

    List<ReportCat> findByOwnerAndDateOfReport(OwnerCat owner, LocalDate date);

}
