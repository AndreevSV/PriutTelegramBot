package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.report.CatsReport;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReportsCatsRepository extends JpaRepository<CatsReport, Long> {



}
