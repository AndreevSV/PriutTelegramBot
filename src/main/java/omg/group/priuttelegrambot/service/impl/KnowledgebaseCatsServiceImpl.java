package omg.group.priuttelegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
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
    private final TelegramBot telegramBot;
//    private final Update update;


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
//            telegramBot.execute(new SendMessage(update.message().chat().id(), "Такая команда не найдена"));
        }
        return null;
    }



}
