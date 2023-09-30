package omg.group.priuttelegrambot.service.knowledgebases.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseDogs;
import omg.group.priuttelegrambot.exception.NotFoundException;
import omg.group.priuttelegrambot.repository.knowledgebases.KnowledgebaseDogsRepository;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseDogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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
            log.info("Message from knowledgebase was found {}", knowledgebaseDto);
            return knowledgebaseDto;
        } else {
            throw new NotFoundException("Message from knowledgebase was not found");
        }
    }

}
