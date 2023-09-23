package omg.group.priuttelegrambot.service.knowledgebases.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseDogs;
import omg.group.priuttelegrambot.repository.knowledgebases.KnowledgebaseDogsRepository;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Data
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KnowledgebaseDogsServiceImpl implements KnowledgebaseDogsService {

    private final KnowledgebaseDogsRepository knowledgebaseDogsRepository;

    @Override
    public KnowledgebaseDto findMessageByCommand(String command) {

        Optional<KnowledgebaseDogs> knowledge = knowledgebaseDogsRepository.findByCommand(command);

        if (knowledge.isPresent()) {
            KnowledgebaseDto knowledgebaseDto = new KnowledgebaseDto();

            knowledgebaseDto.setCommand(knowledge.get().getCommand());
            knowledgebaseDto.setMessage(knowledge.get().getMessage());
            knowledgebaseDto.setCommandDescription(knowledge.get().getCommandDescription());

            return knowledgebaseDto;
        } else {
            System.out.println("Такая команда не найдена");
            return null;
        }
    }

}
