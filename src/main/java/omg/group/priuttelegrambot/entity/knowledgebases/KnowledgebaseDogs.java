package omg.group.priuttelegrambot.entity.knowledgebases;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "knowledge_base_dogs")
public class KnowledgebaseDogs extends Knowledgebase {


}
