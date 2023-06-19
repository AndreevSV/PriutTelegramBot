package omg.group.priuttelegrambot.repository;


import omg.group.priuttelegrambot.entity.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Interface ReportDataRepository.
 *
 * @author OMGgroup
 * @version 1.0.0
 */
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Set<Report> findListByChatId(Long id);

    Report findByChatId(Long id);

}

