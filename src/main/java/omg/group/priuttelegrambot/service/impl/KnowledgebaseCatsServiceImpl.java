package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;
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
    public KnowledgebaseDto findMessageByCommand(String command) {
        Optional<KnowledgebaseCats> knowledge = knowledgebaseCatsRepository.findByCommand(command);
        if (knowledge.isPresent()) {

            KnowledgebaseDto knowledgebaseDto = new KnowledgebaseDto();

            knowledgebaseDto.setCommand(knowledge.get().getCommand());
            knowledgebaseDto.setMessage(knowledge.get().getMessage());
            knowledgebaseDto.setCommandDescription(knowledge.get().getCommandDescription());

            return knowledgebaseDto;
        } else {
            throw new NullPointerException("Такая команда не найдена");
        }
    }



}
