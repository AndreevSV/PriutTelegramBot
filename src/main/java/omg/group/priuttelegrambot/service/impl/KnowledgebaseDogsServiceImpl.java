package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseDogs;
import omg.group.priuttelegrambot.repository.KnowledgebaseDogsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Data
@Service
@RequiredArgsConstructor
public class KnowledgebaseDogsServiceImpl implements KnowledgebaseDogsService {

    private final KnowledgebaseDogsRepository knowledgebaseDogsRepository;

    @Override
    public String findMessageByCommand(String command) {
        List<KnowledgebaseDogs> knowledge = knowledgebaseDogsRepository.findAll();
        System.out.println(knowledge);
//        if (knowledge.isPresent()) {
//            return knowledge.get().getMessage();
//        } else {
//            throw new NullPointerException("Такая команда не найдена");
//        }
        return command;
    }
}
