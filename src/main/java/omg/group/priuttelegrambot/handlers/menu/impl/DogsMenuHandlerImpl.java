package omg.group.priuttelegrambot.handlers.menu.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import omg.group.priuttelegrambot.handlers.menu.DogsMenuHandler;
import omg.group.priuttelegrambot.service.knowledgebases.KnowledgebaseDogsService;
import org.springframework.stereotype.Service;

@Service
public class DogsMenuHandlerImpl implements DogsMenuHandler {

    private final TelegramBot telegramBot;
    private final KnowledgebaseDogsService knowledgebaseDogsService;

    public DogsMenuHandlerImpl(TelegramBot telegramBot, KnowledgebaseDogsService knowledgebaseDogsService) {
        this.telegramBot = telegramBot;
        this.knowledgebaseDogsService = knowledgebaseDogsService;
    }

    /**
     * @return buttons menu for Dog's Main Menu partition
     */
    @Override
    public InlineKeyboardMarkup formPriutMainMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Информация новому клиенту").callbackData("/dog_info"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Консультация нового хозяина").callbackData("/dog_take"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отослать ежедневный отчет").callbackData("/dog_send_report"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/start"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Dog's Info partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForInfoMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рассказать о приюте").callbackData("/dog_about"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание, адрес, схема проезда").callbackData("/dog_timetable"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оформление пропуска на машину").callbackData("/dog_admission"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Техника безопасности").callbackData("/dog_safety_measures"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/dog_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Dog's Take Menu partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForTakeMenuButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Знакомство с животным").callbackData("/dog_connection_rules"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Необходимые документы").callbackData("/dog_documents"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Транспортировка животного").callbackData("/dog_transportation"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для щенка").callbackData("/dog_puppy_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для собаки").callbackData("/dog_at_home"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Обустройство дома для собаки-инвалида").callbackData("/dog_disability"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Советы кинолога").callbackData("/dog_recommendations"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Рекомендуемые кинологи").callbackData("/dog_cynologist"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Причины отказа").callbackData("/dog_refusal_reasons"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Оставить контактные данные").callbackData("/dog_receive_contacts"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        return inlineKeyboardMarkup;
    }

    /**
     * @return buttons menu for Dog's Send Report Menu partition
     */
    @Override
    public InlineKeyboardMarkup formInlineKeyboardForSendReportButton() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить фото").callbackData("/dog_send_photo"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить рацион").callbackData("/dog_send_ration"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить самочувствие").callbackData("/dog_send_feeling"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Отправить изменение").callbackData("/dog_send_changes"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Позвать волонтера").callbackData("/dog_volunteer"));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("назад").callbackData("/dog"));

        return inlineKeyboardMarkup;
    }

    /**
     * Command processing for Dog's commands
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
            String description = knowledgebaseDogsService.findMessageByCommand(command).getCommandDescription();
            String message = knowledgebaseDogsService.findMessageByCommand(command).getMessage();

            EditMessageText editedMessage = new EditMessageText(chatId, messageId, "Вы в приюте *Собак* \n" + description + "\n" + message)
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(editedMessage);
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
            String description = knowledgebaseDogsService.findMessageByCommand(command).getCommandDescription();
            String message = knowledgebaseDogsService.findMessageByCommand(command).getMessage();

            EditMessageText editedMessage = new EditMessageText(chatId, messageId, "Вы в приюте *Собак* \n" + description + "\n" + message)
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(editedMessage);
        }
    }
}
