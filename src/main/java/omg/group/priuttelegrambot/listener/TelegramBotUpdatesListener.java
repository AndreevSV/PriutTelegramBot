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
                telegramBot
                        .execute(new SendMessage(
                                chatId,
                                String.format(
                                        """
                                                Привет %s
                                                Вы запустили бот приютов собак и кошек.
                                                Выберите необходимый приют ниже:
                                                """, userName))
                                .replyMarkup(inlineKeyboardMarkup));
            }

            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация новому клиенту").callbackData("/cat_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация хозяина").callbackData("/cat_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отослать отчет").callbackData("/cat_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));
                telegramBot
                        .execute(new SendMessage(
                                chatId,
                                String.format(
                                        """
                                                Привет %s
                                                Вы запустили бот приют кошек.
                                                Выберите что вас интересует:
                                                """, userName))
                                .replyMarkup(inlineKeyboardMarkup));
            }
            case "/cat_info" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/cat_about"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы, адрес, схема проезда").callbackData("/cat_timetable"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оформление пропуска на машину").callbackData("/cat_admission"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Техника безопасности").callbackData("/cat_safety_measures"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/dog_receive_contacts"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

                telegramBot
                        .execute(new SendMessage(chatId,
                                """ 
                                        Вы выбрали приют для кошек.
                                        Что вы хотите сделать?""")
                                .replyMarkup(inlineKeyboardMarkup));
            }
            case "/cat_about" -> executeCommandAndShowMenuCatAbout(chatId, text);
            case "/cat_timetable" -> executeCommandAndShowMenuCatAbout(chatId, text);
            case "/cat_admission" -> executeCommandAndShowMenuCatAbout(chatId, text);
            case "/cat_safety_measures" -> executeCommandAndShowMenuCatAbout(chatId, text);

            case "/cat_take" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_connection_rules" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_documents" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_transportation" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_kitty_at_home" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_at_home" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_disability" -> executeCommandAndShowMenuCatTake(chatId, text);
            case "/cat_refusal_reasons" -> executeCommandAndShowMenuCatTake(chatId, text);

            case "/cat_send_report" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_send_photo" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_send_ration" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_send_feeling" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_send_changes" -> executeCommandAndShowMenuCatSendReport(chatId, text);

            case "/cat_back" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_volunteer" -> executeCommandAndShowMenuCatSendReport(chatId, text);
            case "/cat_receive_contacts" -> executeCommandAndShowMenuCatTake(chatId, text); ///!!!!!!!!!!!!!!!!!!!


            case "/dog" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация новому клиенту").callbackData("/dog_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация хозяина").callbackData("/dog_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отослать отчет").callbackData("/dog_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));
                telegramBot
                        .execute(new SendMessage(
                                chatId,
                                String.format(
                                        """
                                                Привет %s
                                                Вы запустили бот приюта собак.
                                                Выберите что вас интересует:
                                                """, userName))
                                .replyMarkup(inlineKeyboardMarkup));
            }

            case "/dog_about" -> executeCommandAndshowMenuDogAbout(chatId, text);
            case "/dog_timetable" -> executeCommandAndshowMenuDogAbout(chatId, text);
            case "/dog_admission" -> executeCommandAndshowMenuDogAbout(chatId, text);
            case "/dog_safety_measures" -> executeCommandAndshowMenuDogAbout(chatId, text);

            case "/dog_take" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_connection_rules" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_documents" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_transportation" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_puppy_at_home" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_at_home" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_disability" -> executeCommandAndshowMenuDogTake(chatId, text);
            case "/dog_refusal_reasons" -> executeCommandAndshowMenuDogTake(chatId, text);

            case "/dog_send_report" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_send_photo" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_send_ration" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_send_feeling" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_send_changes" -> executeCommandAndShowMenuDogSendReport(chatId, text);

            case "/dog_back" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_volunteer" -> executeCommandAndShowMenuDogSendReport(chatId, text);
            case "/dog_receive_contacts" -> executeCommandAndshowMenuDogTake(chatId, text);

            default -> sendMessage(chatId, "Нет такой команды");
        }
    }

    private void executeCommandAndshowMenuDogAbout(Long chatId, String text) {

        String message = knowledgebaseDogsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/dog_about"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы, адрес, схема проезда").callbackData("/dog_timetable"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оформление пропуска на машину").callbackData("/dog_admission"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Техника безопасности").callbackData("/dog_safety_measures"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/dog_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        telegramBot.execute(new SendMessage(chatId, message + "\n Выберете команду:").replyMarkup(inlineKeyboardMarkup));
    }

    private void executeCommandAndshowMenuDogTake(Long chatId, String text) {

        String message = knowledgebaseDogsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация нового хозяина").callbackData("/dog_take"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Правила знакомста").callbackData("/dog_connection_rules"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Необходимые документы").callbackData("/dog_documents"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Транспортировка животного").callbackData("/dog_transportation"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для щенка.").callbackData("/dog_puppy_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для взрослого животного.").callbackData("/dog_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для животного с ограниченными возможностями").callbackData("/dog_disability"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Советы кинолога").callbackData("/dog_recommendations"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рекомендуемые кинологи").callbackData("/dog_cynologist"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Причины отказа").callbackData("/dog_refusal_reasons"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/dog_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_vulonteer"));

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        telegramBot
                .execute(new SendMessage(
                        chatId,
                        message +
                                """ 
                                            Вы выбрали раздел: Консультация нового хозяина.
                                            Выберете команду:
                                        """)
                        .replyMarkup(inlineKeyboardMarkup));
    }

    private void executeCommandAndShowMenuDogSendReport(Long chatId, String text) {

        String message = knowledgebaseDogsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Сообщение об отчете о животном").callbackData("/dog_send_report"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Сообщение об отсылке фото").callbackData("/dog_send_photo"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Сообщение об отсылке рациона").callbackData("/dog_send_ration"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Сообщение об отсылке самочувствия").callbackData("/dog_send_feeling"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Сообщение об отсылке изменений").callbackData("/dog_send_changes"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        telegramBot
                .execute(new SendMessage(
                        chatId,
                        message +
                                """
                                        Вы выбрали раздел: Ведение питомца.
                                        Выберете команду
                                        """)
                        .replyMarkup(inlineKeyboardMarkup));
    }

    private void executeCommandAndShowMenuCatAbout(Long chatId, String text) {

        String message = knowledgebaseCatsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/cat_about"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы приюта и адрес, схему проезда").callbackData("/cat_timetable"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину").callbackData("/cat_admission"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/cat_safety_measures"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Записать контактные данные для связи").callbackData("/cat_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        telegramBot.execute(new SendMessage(chatId, message + "\n Выберете команду:").replyMarkup(inlineKeyboardMarkup));
    }

    private void executeCommandAndShowMenuCatTake(Long chatId, String text) {

        String message = knowledgebaseCatsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация нового хозяина").callbackData("/cat_take"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Знакомство с животным").callbackData("/cat_connection_rules"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Необходимые документы").callbackData("/cat_documents"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Транспортировка").callbackData("/cat_transportation"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для щенка.").callbackData("/cat_puppy_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для взрослого животного.").callbackData("/cat_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Животные с сограниченными возможностями").callbackData("/cat_disability"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Причины отказа").callbackData("/cat_refusal_reasons"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/cat_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

        telegramBot
                .execute(new SendMessage(chatId,
                        message +
                                """
                                        Вы выбрали раздел: Консультация хозяина.
                                        Выберете команду""")
                        .replyMarkup(inlineKeyboardMarkup));
    }

    private void executeCommandAndShowMenuCatSendReport(Long chatId, String text) {

        String message = knowledgebaseCatsService.findMessageByCommand(text);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить ежедневный отчет о животном").callbackData("/cat_send_report"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить фото").callbackData("/cat_send_photo"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить рацион").callbackData("/cat_send_ration"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить самочувствие").callbackData("/cat_send_feeling"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить изменение").callbackData("/cat_send_changes"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

        telegramBot
                .execute(new SendMessage(chatId,
                        message +
                                """
                                        Вы выбрали раздел: Ведение питомца.
                                        Выберете команду:
                                        """)
                        .replyMarkup(inlineKeyboardMarkup));
    }


    private void sendMessage(Long chatId, String text) {
        this.telegramBot.execute(new SendMessage(chatId, text));
    }


}
