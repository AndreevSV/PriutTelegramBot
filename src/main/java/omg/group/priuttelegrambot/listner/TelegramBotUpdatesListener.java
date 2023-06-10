package omg.group.priuttelegrambot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private static final Logger LOG = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;

    /**
     * конструктортелеграм бота.
     * {@link TelegramBot#setUpdatesListener(UpdatesListener)}
     *
     * @param telegramBot
     */
    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
        this.telegramBot.setUpdatesListener(this);
    }

    /**
     * Process - главные метод по работе с обновлениями
     *
     * @param updates
     * @return
     */

    @Override
    public int process(List<Update> updates) {
        /**
         * создаем стрим для фильтрации входящих сообщений
         */
        updates.stream().filter(update -> update.message() != null || update.callbackQuery() != null)
                .forEach(this::handleUpdate);
        return CONFIRMED_UPDATES_ALL;
    }

    /**
     * метод апдейта хэндлера использующий параметры:
     * Update update
     *
     * @param update
     */
    private void handleUpdate(Update update) {
        if (update.message() != null && update.message().text() != null) {

            processText(update);
        }
        if (update.callbackQuery() != null) {
            processCallbackQuery(update);
            processText(update);

        }
    }

    private void processCallbackQuery(Update update) {
        String callbackData = update.callbackQuery().data();
        if (callbackData.equals("Приют для кошек")) {
            telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "/cat"));

        } else if (callbackData.equals("Приют для собак")) {
            telegramBot.execute(new SendMessage(update.callbackQuery().from().id(), "/dog"));
        }

    }

    /**
     * Метод для обработки текстовых команд
     * {@link TelegramBot}
     *
     * @param update
     */
    private void processText(Update update) {
        String text = "/start";
        Long chatId = 0L;
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
            case "/start" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                        new InlineKeyboardButton("Приют для собак").callbackData("/dog"));
                telegramBot.execute(new SendMessage(chatId, "Привет " + userName
                        + "\n Это телеграм бот приюта домашних животных. \n Выберите приют:  ").replyMarkup(inlineKeyboardMarkup));


            }
            case "/cat" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о приюте").callbackData("/cat_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Как взять животное").callbackData("/cat_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/cat_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/volonteer"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для кошек. \n " +
                        "Что бы вы хотели узнать?").replyMarkup(inlineKeyboardMarkup));

            }
            case "/cat_info" -> {
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").url("https://telegra.ph/Priyut-dlya-bezdomnyh-koshechek-Mona-roza-06-09"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы приюта и адрес, схему проезда").url("https://telegra.ph/Pravila-poseshcheniya-06-09"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину").callbackData("/cat_admission"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/cat_safety_measures"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Записать контактные данные для связи").callbackData("/contacts"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volonteer"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для собак. \n " +
                        "Что вы хотите сделать?").replyMarkup(inlineKeyboardMarkup));
            }
            case "/dog" -> {

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация о приюте").callbackData("/dog_info"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Как взять животное").callbackData("/dog_take"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Прислать отчет о питомце").callbackData("/dog_send_report"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/volonteer"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для собак. \n " +
                        "Что бы вы хотели узнать?").replyMarkup(inlineKeyboardMarkup));


            }
            case "/dog_info" -> {
// формирование клавиатуры
                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/shelter_about"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы приюта и адрес, схему проезда").callbackData("/dog_timetable"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Контактные данные охраны для оформления пропуска на машину").callbackData("/dog_admission"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Общие рекомендации о технике безопасности на территории приюта").callbackData("/dog_safety_measures"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Записать контактные данные для связи").callbackData("/dog_receive_contacts"));
                inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volonteer"));
                telegramBot.execute(new SendMessage(chatId, "Вы выбрали приют для собак. \n " +
                        "Что вы хотите сделать?").replyMarkup(inlineKeyboardMarkup));

            }
            case "/dog_about" -> {
                sendMessage(chatId, """
                        Информация о приюте для собак - берется из базы данных.
                        Метод, считывающий строку базы данных и вставляющий значение.
                        Ему передается команда со слешем, по этому ключу идет обрашение к базе данных.
                        """);

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

        }
    }

    /**
     * метод для отправки фискированных сообщений пользователю
     */
    //TODO редактировать после создания базы данных
    private void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }
}

