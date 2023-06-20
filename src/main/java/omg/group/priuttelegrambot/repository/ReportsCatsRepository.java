package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.CatsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsCatsRepository extends JpaRepository<CatsReport, Long> {

}
