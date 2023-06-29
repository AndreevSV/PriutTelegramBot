package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.report.DogsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReportsDogsRepository extends JpaRepository<DogsReport, Long> {

    Optional<DogsReport> findByClientIdAndAnimalIdAndCreatedAt(Long clientId, Long animalId, LocalDateTime createdAt);
}
