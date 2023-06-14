package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import omg.group.priuttelegrambot.repository.KnowledgebaseCatsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Service
@RequiredArgsConstructor
public class KnowledgebaseCatsServiceImpl implements KnowledgebaseCatsService {

    private final KnowledgebaseCatsRepository knowledgebaseCatsRepository;

    @Override
    public String findMessageByCommand(String command) {
        List<KnowledgebaseCats> knowledge = knowledgebaseCatsRepository.findAll();
        System.out.println(knowledge);
//        if (knowledge.isPresent()) {
//            return knowledge.;
//        } else {
//            throw new NullPointerException("Такая команда не найдена");
//        }
        return command;
    }

}
