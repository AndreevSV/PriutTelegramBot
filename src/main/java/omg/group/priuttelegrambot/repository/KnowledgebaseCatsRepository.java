package omg.group.priuttelegrambot.repository;

import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgebaseCatsRepository extends JpaRepository<KnowledgebaseCats, Long> {

//    List<KnowledgebaseCats> findAll(String command);


}
