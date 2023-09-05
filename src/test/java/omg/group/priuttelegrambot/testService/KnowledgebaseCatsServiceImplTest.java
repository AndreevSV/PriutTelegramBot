package omg.group.priuttelegrambot.testService;

import omg.group.priuttelegrambot.entity.knowledgebases.KnowledgebaseCats;
import omg.group.priuttelegrambot.service.impl.KnowledgebaseCatsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import omg.group.priuttelegrambot.repository.KnowledgebaseCatsRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KnowledgebaseCatsServiceImplTest {

    @Mock
    private KnowledgebaseCatsRepository knowledgebaseCatsRepository;

    @InjectMocks
    private KnowledgebaseCatsServiceImpl knowledgebaseCatsService;
    /**
     * Тест метода findMessageByCommand (передаётся сужествующая команда)класса CatsController.
     */
    @Test
    void findMessageByCommand_existingCommand_returnsMessage() {
        String command = "command";
        String message = "message";
        KnowledgebaseCats knowledgebaseCats = new KnowledgebaseCats();
        knowledgebaseCats.setCommand(command);
        knowledgebaseCats.setMessage(message);
        knowledgebaseCats.setCommandDescription(message);
        Optional<KnowledgebaseCats> optional = Optional.of(knowledgebaseCats);

        when(knowledgebaseCatsRepository.findByCommand(command)).thenReturn(optional);

        String result = knowledgebaseCatsService.findMessageByCommand(command);

        assertEquals(message, result);
    }

    /**
     * Тест метода findMessageByCommand (передается несуществующая команда, выбрасывает NullPointerException)
     * класса CatsController.
     */
    @Test
    void findMessageByCommand_nonExistingCommand_throwsException() {
        String command = "nonExistingCommand";

        when(knowledgebaseCatsRepository.findByCommand(command)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> knowledgebaseCatsService.findMessageByCommand(command));
    }
}
