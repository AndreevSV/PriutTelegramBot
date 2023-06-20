package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseDogs;
import omg.group.priuttelegrambot.repository.KnowledgebaseDogsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
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
    public String findMessageByCommand(String command) {
        Optional<KnowledgebaseDogs> knowledge = knowledgebaseDogsRepository.findByCommand(command);
        if (knowledge.isPresent()) {
            KnowledgebaseDto knowledgebaseDto = new KnowledgebaseDto();
            knowledgebaseDto.setCommand(knowledge.get().getCommand());
            knowledgebaseDto.setMessage(knowledge.get().getMessage());
            return knowledgebaseDto.getMessage();
        } else {
            throw new NullPointerException("Такая команда не найдена");
        }
    }

}
