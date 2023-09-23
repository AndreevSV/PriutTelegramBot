package omg.group.priuttelegrambot.service.knowledgebases;

import omg.group.priuttelegrambot.dto.knowledgebase.KnowledgebaseDto;

public interface KnowledgebaseDogsService {
    KnowledgebaseDto findMessageByCommand(String command);


}
