package omg.group.priuttelegrambot.service.knowledgebases;

import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;

public interface KnowledgebaseCatsService {
    KnowledgebaseDto findMessageByCommand(String command);
}
