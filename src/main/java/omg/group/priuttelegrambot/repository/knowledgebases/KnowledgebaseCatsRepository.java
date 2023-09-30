package omg.group.priuttelegrambot.repository.knowledgebases;

import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KnowledgebaseCatsRepository extends JpaRepository<KnowledgebaseCats, Long> {

    Optional<KnowledgebaseCats> findByCommand(String command);

}
