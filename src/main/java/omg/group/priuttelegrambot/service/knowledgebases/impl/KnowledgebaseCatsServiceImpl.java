package omg.group.priuttelegrambot.service.knowledgebases.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import omg.group.priuttelegrambot.repository.knowledgebases.KnowledgebaseCatsRepository;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseCatsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class KnowledgebaseCatsServiceImpl implements KnowledgebaseCatsService {

    private final KnowledgebaseCatsRepository knowledgebaseCatsRepository;
    @Override
    public KnowledgebaseDto findMessageByCommand(String command) {

        Optional<KnowledgebaseCats> knowledge = knowledgebaseCatsRepository.findByCommand(command);

        if (knowledge.isPresent()) {

            KnowledgebaseDto knowledgebaseDto = new KnowledgebaseDto();

            knowledgebaseDto.setCommand(knowledge.get().getCommand());
            knowledgebaseDto.setMessage(knowledge.get().getMessage());
            knowledgebaseDto.setCommandDescription(knowledge.get().getCommandDescription());

            return knowledgebaseDto;
        } else {
            System.out.println("Такая команда не найдена.");
            return null;
        }

    }



}
