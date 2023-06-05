package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream().filter(update -> update.message() != null).forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }


    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {
            processText(update);
        } else {
            this.sendMessage(update.message().chat().id(), "Какой-то текст");
        }

    }

    private void processText(Update update) {

        LOG.info("Получен следующий апдэйт {}", update);

        String text = update.message().text();
        Long chatId = update.message().chat().id();
        String userName = update.message().from().username();

        switch (text) {
            case "/start" -> {
                sendMessage(chatId, """
                        Какой-то текст - берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);

            }
//            case "/dog" -> {
//
//            }
//            case "/dog_info" -> {
//
//            }
//            case "/dog_about" -> {
//
//            }
//            case "/dog_timetable" -> {
//
//            }
//            case "/dog_admission" -> {
//
//            }
//            case "/dog_safety_measures" -> {
//
//            }
//            case "/dog_take" -> {
//
//            }
//            case "/dog_connection_rules" -> {
//
//            }
//            case "/dog_documents" -> {
//
//            }
//            case "/dog_transportation" -> {
//
//            }
//            case "/dog_puppy_at_home" -> {
//
//            }
//            case "/dog_at_home" -> {
//
//            }
//            case "/dog_disability" -> {
//
//            }
//            case "/dog_refusal_reasons" -> {
//
//            }
//            case "/dog_receive_contacts" -> {
//
//            }
//            case "/dog_send_report" -> {
//
//            }
//            case "/dog_send_photo" -> {
//
//            }
//            case "/dog_send_ration" -> {
//
//            }
//            case "/dog_send_feeling" -> {
//
//            }
//            case "/dog_send_changes" -> {
//
//            }
//            case "/dog_back" -> {
//
//            }
//            case "/dog_volonteer" -> {
//
//            }
//            case "/cat" -> {
//
//            }
//            case "/cat_info" -> {
//
//            }
//            case "/cat_about" -> {
//
//            }
//            case "/cat_timetable" -> {
//
//            }
//            case "/cat_admission" -> {
//
//            }
//            case "/cat_safety_measures" -> {
//
//            }
//            case "/cat_take" -> {
//
//            }
//            case "/cat_connection_rules" -> {
//
//            }
//            case "/cat_documents" -> {
//
//            }
//            case "/cat_transportation" -> {
//
//            }
//            case "/cat_kitty_at_home" -> {
//
//            }
//            case "/cat_at_home" -> {
//
//            }
//            case "/cat_disability" -> {
//
//            }
//            case "/cat_refusal_reasons" -> {
//
//            }
//            case "/cat_receive_contacts" -> {
//
//            }
//            case "/cat_send_report" -> {
//
//            }
//            case "/cat_send_photo" -> {
//
//            }
//            case "/cat_send_ration" -> {
//
//            }
//            case "/cat_send_feeling" -> {
//
//            }
//            case "/cat_send_changes" -> {
//
//            }
//            case "/cat_back" -> {
//
//            }
//            case "/cat_volonteer" -> {
//
//            }
//            case "/back_dog_cat" -> {
//
//            }
//            case "/dog_recommendations" -> {
//
//            }
//            case "/dog_cynologist" -> {
//
//            }
            default -> {
                System.out.println("Какая-то ошибка");
            }
        }

    }

    private void sendMessage(Long chatId, String text) {
        this.telegramBot.execute(new SendMessage(chatId, text));
    }

}
