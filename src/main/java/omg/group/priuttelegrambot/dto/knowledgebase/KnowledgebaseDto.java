package omg.group.priuttelegrambot.dto.knowledgebase;

import lombok.Data;

@Data
public class KnowledgebaseDto {

    private Long id;
    private String command;
    private String commandDescription;
    private String message;

}
