package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import omg.group.priuttelegrambot.service.CatsService;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;
    @Mock
    private KnowledgebaseCatsService knowledgebaseCatsService;

    private KnowledgebaseDogsService knowledgebaseDogsService;

    private CatsService catsService;

}
