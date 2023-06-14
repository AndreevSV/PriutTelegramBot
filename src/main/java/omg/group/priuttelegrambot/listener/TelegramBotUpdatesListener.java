package omg.group.priuttelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Data;
import omg.group.priuttelegrambot.service.CatsService;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    private final KnowledgebaseCatsService knowledgebaseCatsService;

    private final KnowledgebaseDogsService knowledgebaseDogsService;

    private final CatsService catsService;


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      KnowledgebaseDogsService knowledgebaseDogsService,
                                      KnowledgebaseCatsService knowledgebaseCatsService,
    CatsService catsService) {
        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.knowledgebaseDogsService = knowledgebaseDogsService;
        this.catsService = catsService;
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);

    }

    @Override
    public int process(List<Update> updates) {
        updates.stream().filter(update -> update.message() != null || update.callbackQuery() != null).forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }


    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null || update.callbackQuery() != null) {
            processText(update);
        } else {
            this.sendMessage(update.message().chat().id(), "Нет такой команды. Попробуйте /help");
        }
    }

    private void processText(Update update) {
        // текст сообщения от пользователя
        String text = "/start";
//        id пользователя
        Long chatId = 0L;
//        имя пользователя
        String userName = " ";
        LOG.info("Получен следующий апдэйт {}", update);

        if (update.message() != null) {
            chatId = update.message().chat().id();
            text = update.message().text();
            userName = update.message().from().username();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            text = update.callbackQuery().data();
            userName = update.callbackQuery().from().username();
        }


        switch (text) {
//            обработка команды /start
            case "/start" -> {
//                создание Inline клавиатуры с двумя кнопками
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                        new InlineKeyboardButton("Приют для собак").callbackData("/dog"));
//                отправка сообщения пользователю с клавиатурой
                telegramBot.execute(new SendMessage(chatId, "Привет " + userName
                        + "\n Это телеграм бот приюта домашних животных. \n Выберите приют:  ").replyMarkup(inlineKeyboardMarkup));
            }

            case "/dog" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о приюте").callbackData("/dog_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Как взять животное").callbackData("/dog_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/dog_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volonteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));

                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для собак. \n " +
                        "Что бы вы хотели узнать?").replyMarkup(inlineKeyboardMarkup));

            }

            case "/dog_info" -> {
// формирование клавиатуры
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/dog_about"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы приюта и адрес, схему проезда").callbackData("/dog_timetable"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину").callbackData("/dog_admission"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/dog_safety_measures"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Записать контактные данные для связи").callbackData("/dog_receive_contacts"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volonteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для собак. \n " +
                        "Что вы хотите сделать?").replyMarkup(inlineKeyboardMarkup));

            }

            case "/dog_about" -> {

                String message = knowledgebaseDogsService.findMessageByCommand(text);

                System.out.println(message);

                sendMessage(chatId, message);


            }
            case "/dog_timetable" -> {
                sendMessage(chatId, """
                        Расписание работы приюта и адрес, схему проезда - берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);

            }
            case "/dog_admission" -> {
                sendMessage(chatId, """
                        Контактные данные охраны для оформления пропуска на машину- берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);

            }
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
            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о приюте").callbackData("/cat_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Как взять животное").callbackData("/cat_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/cat_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volonteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для кошек. \n " +
                        "Что бы вы хотели узнать?").replyMarkup(inlineKeyboardMarkup));


            }
            case "/cat_info" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/cat_about"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы приюта и адрес, схему проезда").callbackData("/cat_timetable"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину").callbackData("/cat_admission"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/cat_safety_measures"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Записать контактные данные для связи").callbackData("/dog_receive_contacts"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volonteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для кошек. \n " +
                        "Что вы хотите сделать?").replyMarkup(inlineKeyboardMarkup));
            }
            case "/cat_about" -> {
                sendMessage(chatId, """
                        Информация о приюте для кошек - берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);


            }
            case "/cat_timetable" -> {

                sendMessage(chatId, """
                        Расписание работы приюта, адрес, схему проезда - берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);


            }
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
