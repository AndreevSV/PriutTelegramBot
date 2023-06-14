package omg.group.priuttelegrambot.dto.knowledgebase;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class KnowledgebaseDto {

    private Long id;
    private String command;
    private String commandDescription;
    private String message;

}
