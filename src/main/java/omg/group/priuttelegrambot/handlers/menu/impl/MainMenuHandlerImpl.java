package omg.group.priuttelegrambot.handlers.menu.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.*;
import omg.group.priuttelegrambot.handlers.media.OtherMediaHandler;
import omg.group.priuttelegrambot.handlers.media.PhotoHandler;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import omg.group.priuttelegrambot.handlers.updates.OwnUpdatesHandler;
import org.springframework.stereotype.Service;

@Service
public class MainMenuHandlerImpl implements MainMenuHandler {

    private final TelegramBot telegramBot;
    private final PhotoHandler photoHandler;
    private final OtherMediaHandler otherMediaHandler;
    private final OwnUpdatesHandler ownUpdatesHandler;

    public MainMenuHandlerImpl(TelegramBot telegramBot,
                               PhotoHandler photoHandler,
                               OtherMediaHandler otherMediaHandler,
                               OwnUpdatesHandler ownUpdatesHandler) {
        this.telegramBot = telegramBot;
        this.photoHandler = photoHandler;
        this.otherMediaHandler = otherMediaHandler;
        this.ownUpdatesHandler = ownUpdatesHandler;
    }

    @Override
    public void executeStartMenuButton(Update update) {
        Long chatId = ownUpdatesHandler.getChatId(update);
        String firstName = ownUpdatesHandler.getFirstName(update);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dog"));
        SendMessage sendMessage = new SendMessage(chatId, String.format("""
                Привет *%s* !
                Вы запустили telegram-бот приютов *собак и кошек*.
                Выберите необходимый приют ниже:
                """, firstName))
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }

    @Override
    public void noSuchCommandSendMessage(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(
                new InlineKeyboardButton("Приют для кошек").callbackData("/cat"),
                new InlineKeyboardButton("Приют для собак").callbackData("/dog"));
        Long chatId = ownUpdatesHandler.getChatId(update);
        SendMessage sendMessage = new SendMessage(chatId, """
                Нет такой команды. Попробуйте воспользоваться кнопками меню:
                """)
                .replyMarkup(inlineKeyboardMarkup);
        telegramBot.execute(sendMessage);
    }

    @Override
    public void noReportExistMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Отсылать сегодня отчет не требуется. Выберите другую команду:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendRationButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить рацион*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *рацион* питомца.
                Введите текстом рацион и нажимте кнопку *Отправить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendFeelingButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить самочувствие*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *самочувствие* питомца.
                Введите текстом самочувствие и нажимте кнопку "Отправить"
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendChangesButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить изменения*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *изменения в поведении* питомца.
                Введите текстом изменения и нажимте кнопку *Отправить*
                 """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendPhotoButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить фотографию*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *фотографию* питомца.
                Загрузите фотографию и нажимте кнопку *Отправить*
                 """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendContactsButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить контакты*.
                Теперь нажатие кнопок недоступно, пока вы не отправите свой *контакт*.
                Выберите свой контакт и нажмите кнопку *Отправить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeCallVolunteerButtonMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Позвать волонтера*.
                Теперь нажатие кнопок недоступно, пока вы не завершите чат.
                Для завершения чата нажмите *Заершить* под клавиатурой.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void reportAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня отчет. Больше сегодня отправлять ничего не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void photoAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня фотографию. Отправлять сегодня фотографию не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void photoDublicateSentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы уже отсылали такую фотографию. Отошлите свежую фотографию.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void photoSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                *Фотография успешно сохранена*. Выберите следующую команду:""")
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void notPhotoMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы пытаетесь отослать не фотографию. Для отсылки фотографии нажмите снова команду *Отправить фото*.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void rationAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня рацион. Отправлять сегодня рацион больше не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void rationSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Введеный *рацион* успешно сохранен. Выберите следующую команду:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void feelingAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня информацию о самочувствии. Отправлять сегодня информацию о самочувствии больше не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void feelingSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Введенный отчет *о самочувствии животного* успешно сохранен. Выберите следующую команду:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void changesAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня изменения в поведении животного. Отправлять сегодня изменени больше не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void changesSavedOkMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Введенный отчет *об изменениях в поведении животного* успешно сохранен. Выберите следующую команду:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void telephoneAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                У нас уже есть Ваш телефонный номер. Повторная отсылка не требуется.
                Выберите другой пункт меню.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void waitingForContactMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new SendMessage(chatId, """
                Вы нажали кнопку *Оставить контактные данные* и мы ожидаем подтверждение отправки.
                Нажмите кнопку "Поделиться контактом* под клавиатурой.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void contactSavedOkMessage(Long chatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new SendMessage(chatId, """
                Ваш телефон успешно сохранен.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void volunteerOpenedChatMessage(Long ownerChatId, Long volunteerChatId) {
        KeyboardButton closeKeyboardButton = new KeyboardButton("Завершить");

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);

        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Волонтер подтвердил готовность проконсультровать Вас. Отправьте ему свой вопрос.
                Для завершения нажмите *Завершить*.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkup);
        telegramBot.execute(messageToOwner);

        SendMessage sendMessage = new SendMessage(volunteerChatId, """
                Вы подтвердили чат с клиентом. Дождитесь вопроса от него.
                Для завершения чата нажмите *Завершить*.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkup);

        telegramBot.execute(sendMessage);
    }


    @Override
    public void ownerClosedChatMessage(Long ownerChatId, Long volunteerChatId) {
        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Вы завершили чат с волонтером. Чтобы начать новую консультацию снова нажмите кнопу "Позвать волонтера"
                """)
                .parseMode(ParseMode.Markdown);
        telegramBot.execute(messageToOwner);

        SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                Пользователь завершил чат с вами. Чат закрыт и удален из базы.
                """)
                .parseMode(ParseMode.Markdown);
        telegramBot.execute(messageToVolunteer);
    }

    @Override
    public void volunteerClosedChatMessage(Long volunteerChatId, Long ownerChatId) {
        SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                Вы завершили чат с пользователем. Чат закрыт и удален из базы.
                """)
                .parseMode(ParseMode.Markdown);
        telegramBot.execute(messageToVolunteer);

        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Волонтер завершил чат с Вами. Чтобы начать новую консультацию снова нажмите кнопу "Позвать волонтера"
                """)
                .parseMode(ParseMode.Markdown);
        telegramBot.execute(messageToOwner);
    }

    @Override
    public void noFreeVolunteerAvailableMessage(Long ownerChatId) {
        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                К сожалению в настоящее время нет свободных волонтеров, которые могут вас проконсультировать.
                Пожалуйста, обратитесь позднее.
                """)
                .parseMode(ParseMode.Markdown);

        telegramBot.execute(messageToOwner);
    }

    @Override
    public void inquiryToVolunteerForChat(Long volunteerChatId, Long ownerChatId) {
        // Send message to volunteer
        KeyboardButton replayKeyboardButton = new KeyboardButton("Ответить");
        KeyboardButton closeKeyboardButton = new KeyboardButton("Завершить");

        ReplyKeyboardMarkup replyKeyboardMarkupForVolunteer = new ReplyKeyboardMarkup(replayKeyboardButton, closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);

        SendMessage messageToVolunteer = new SendMessage(volunteerChatId, """
                Клиенту требуется консультация.
                Пожалуйста, свяжитесь с ним в ближайшее время, нажав *Ответить*.
                По завершению нажмите *Завершить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForVolunteer);
        telegramBot.execute(messageToVolunteer);

        // Send message to owner
        ReplyKeyboardMarkup replyKeyboardMarkupForOwner = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Волонтеру направлен запрос на чат с вами.
                Пожалуйста, дождитесь ответа волонтера, либо нажмите *Завершить* для завершения.
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForOwner);
        telegramBot.execute(messageToOwner);
    }

    @Override
    public void chatAlreadySetToOwnerMessage(Long ownerChatId) {
        KeyboardButton closeKeyboardButton = new KeyboardButton("Завершить");

        ReplyKeyboardMarkup replyKeyboardMarkupForVolunteer = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);

        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Вы уже ведете чат с волонтером. Введите свой вопрос.
                Для завершения чата нажмите кнопку *Завершить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(replyKeyboardMarkupForVolunteer);
        telegramBot.execute(messageToOwner);
    }

    @Override
    public void chatAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                У вас уже открыт чат с волонтером. Для завершения нажмите *Завершить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void chatForwardMessageWithCloseButton(Long chatId, Update update) {
        KeyboardButton closeKeyboardButton = new KeyboardButton("Завершить");
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(closeKeyboardButton)
                .resizeKeyboard(true)
                .oneTimeKeyboard(true);
        if (update.message() != null) {
            if (update.message().text() != null) {
                String text = update.message().text();
                SendMessage message = new SendMessage(chatId, text)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(message);
            } else if (update.message().photo() != null) {
                String fileId = photoHandler.getFileIdFromUpdate(update);
                SendPhoto photo = new SendPhoto(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(photo);
            } else if (update.message().video() != null) {
                String fileId = otherMediaHandler.getVideoFileIdFromUpdate(update);
                SendVideo video = new SendVideo(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(video);
            } else if (update.message().voice() != null) {
                String fileId = otherMediaHandler.getVoiceFileIdFromUpdate(update);
                SendVoice voice = new SendVoice(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(voice);
            } else if (update.message().audio() != null) {
                String fileId = otherMediaHandler.getAudioFileIdFromUpdate(update);
                SendAudio audio = new SendAudio(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(audio);
            } else if (update.message().document() != null) {
                String fileId = otherMediaHandler.getDocumentFileIdFromUpdate(update);
                SendDocument document = new SendDocument(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(document);
            } else if (update.message().sticker() != null) {
                String fileId = otherMediaHandler.getStickerFileIdFromUpdate(update);
                SendSticker sticker = new SendSticker(chatId, fileId)
                        .replyMarkup(replyKeyboardMarkup);
                telegramBot.execute(sticker);
            }
        }
    }

    @Override
    public void noPetMessage(Long ownerChatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new SendMessage(ownerChatId, """
                У Вас еще нет домашнего питомца и Вы не можете отправлять отчет. Просмотрите информацию, как взять себе питомца.
                Для этого нажмите соответствующу кнопку ниже""")
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void noOwnOfPetRegisteredMessage(Long ownerChatId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new SendMessage(ownerChatId, """
                Вы еще не зарегистрированы как владелец животного. Просмотрите информацию, как взять себе питомца.
                Для этого нажмите соответствующу кнопку ниже""")
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

}
