package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.DogsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportsDogsRepository extends JpaRepository<DogsReport, Long> {
}
