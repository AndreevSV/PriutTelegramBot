package omg.group.priuttelegrambot.entity.knowledgebases;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "knowledge_base_cats")
public class KnowledgebaseCats extends Knowledgebase{


}
