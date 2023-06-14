package omg.group.priuttelegrambot.entity.knowledgebases;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "knowledge_base_dogs")
public class KnowledgebaseDogs extends Knowledgebase {


}
