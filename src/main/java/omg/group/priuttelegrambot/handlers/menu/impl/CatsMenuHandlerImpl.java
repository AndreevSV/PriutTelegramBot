package omg.group.priuttelegrambot.handlers.menu.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.handlers.menu.CatsMenuHandler;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseCatsService;
import org.springframework.stereotype.Service;

@Service
public class CatsMenuHandlerImpl implements CatsMenuHandler {

    private final TelegramBot telegramBot;
    private final KnowledgebaseCatsService knowledgebaseCatsService;

    public CatsMenuHandlerImpl(TelegramBot telegramBot, KnowledgebaseCatsService knowledgebaseCatsService) {
        this.telegramBot = telegramBot;
        this.knowledgebaseCatsService = knowledgebaseCatsService;
    }

    /**
     * @return buttons menu for Cat's Main Menu partition
     */
    @Override
    public InlineKeyboardMarkup formPriutMainMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация новому клиенту").callbackData("/cat_info"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация нового хозяина").callbackData("/cat_take"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отослать ежедневный отчет").callbackData("/cat_send_report"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Cat's Info partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/cat_about"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание, адрес, схема проезда").callbackData("/cat_timetable"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оформление пропуска на машину").callbackData("/cat_admission"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Техника безопасности").callbackData("/cat_safety_measures"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/cat_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Cat's Take Menu partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Знакомство с животным").callbackData("/cat_connection_rules"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Необходимые документы").callbackData("/cat_documents"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Транспортировка животного").callbackData("/cat_transportation"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для котенка").callbackData("/cat_kitty_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для кота/кошки").callbackData("/cat_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для животного-инвалида").callbackData("/cat_disability"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Причины отказа").callbackData("/cat_refusal_reasons"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/cat_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Cat's Send Report Menu partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForSendReportButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить фото").callbackData("/cat_send_photo"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить рацион").callbackData("/cat_send_ration"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить самочувствие").callbackData("/cat_send_feeling"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить изменение").callbackData("/cat_send_changes"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/cat_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/cat"));

        return inlineKeyboardMarkup;
    }

    /**
     * Command processing for Cat's commands
     */
    @Override
    public void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup) {
        Long chatId;
        int messageId;
        String command;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            messageId = update.message().messageId();
            command = update.message().text();
            String description = knowledgebaseCatsService.findMessageByCommand(command).getCommandDescription();
            String message = knowledgebaseCatsService.findMessageByCommand(command).getMessage();

            EditMessageText editedMessage = new EditMessageText(chatId, messageId, "Вы в приюте *Кошек*. \n" + description + " " + message)
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(editedMessage);
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
            String description = knowledgebaseCatsService.findMessageByCommand(command).getCommandDescription();
            String message = knowledgebaseCatsService.findMessageByCommand(command).getMessage();

            EditMessageText editedMessage = new EditMessageText(chatId, messageId, "Вы в приюте *Кошек*. \n" + description + " " + message)
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);
            telegramBot.execute(editedMessage);
        }
    }

    @Override
    public void inquiryToVolunteerForChat(Long volunteerChatId, Long ownerChatId) {
        // Send message to volunteer
        KeyboardButton answerKeyboardButton = new KeyboardButton("/Ответить");
        KeyboardButton closeKeyboardButton = new KeyboardButton("/Завершить");

        ReplyKeyboardMarkup replyKeyboardMarkupForVolunteer = new ReplyKeyboardMarkup(answerKeyboardButton, closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .isPersistent(true);

        SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                Клиенту требуется консультация. Пожалуйста, свяжитесь с ним в ближайшее время, нажав */Ответить*.
                По завершению чата нажмите *Завершить* или введите команду */Завершить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForVolunteer);
        telegramBot.execute(messageToVolunteer);

        // Send message to owner
        ReplyKeyboardMarkup replyKeyboardMarkupForOwner = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .isPersistent(true);
        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Волонтеру направлен запрос на чат с вами.
                Пожалуйста, дождитесь ответа волонтера, либо введите команду */Завершить* для завершения.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForOwner);
        telegramBot.execute(messageToOwner);
    }

    @Override
    public void chatAlreadySetToOwnerMessage(Long ownerChatId) {
        // Send message to owner
        KeyboardButton closeKeyboardButton = new KeyboardButton("/Завершить");

        ReplyKeyboardMarkup replyKeyboardMarkupForVolunteer = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true)
                .isPersistent(true);

        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Вы уже ведете чат с волонтером. Введите свой вопрос.
                Для завершения чата нажмите кнопку */Завершить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForVolunteer);
        telegramBot.execute(messageToOwner);
    }

}
