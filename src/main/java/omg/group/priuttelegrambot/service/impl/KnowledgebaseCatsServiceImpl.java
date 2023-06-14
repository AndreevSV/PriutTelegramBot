package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import omg.group.priuttelegrambot.repository.KnowledgebaseCatsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class KnowledgebaseCatsServiceImpl implements KnowledgebaseCatsService {

    private final KnowledgebaseCatsRepository knowledgebaseCatsRepository;

    @Override
    public String findMessageByCommand(String command) {
        Optional<KnowledgebaseCats> knowledge = knowledgebaseCatsRepository.findByCommand(command);
        System.out.println(knowledge);
        if (knowledge.isPresent()) {
            return knowledge.get().getMessage();
        } else {
            throw new NullPointerException("Такая команда не найдена");
        }
    }

}
