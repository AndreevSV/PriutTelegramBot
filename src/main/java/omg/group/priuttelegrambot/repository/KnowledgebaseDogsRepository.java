package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseDogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgebaseDogsRepository extends JpaRepository<KnowledgebaseDogs, Long> {
//    List<KnowledgebaseDogs> findByCommand(String command);
}
