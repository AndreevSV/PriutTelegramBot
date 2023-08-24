package omg.group.priuttelegrambot.handlers.menu.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.handlers.menu.MainMenuHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainMenuHandlerImpl implements MainMenuHandler {

    private final TelegramBot telegramBot;

    @Override
    public void executeStartMenuButton(Update update) {
        Long chatId = 0L;
        String firstName = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            firstName = update.message().from().firstName();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            firstName = update.callbackQuery().from().firstName();
        }

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
        Long chatId = 0L;

        if (update.message() != null) {
            chatId = update.message().chat().id();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        SendMessage sendMessage = new SendMessage(chatId, """
                Нет такой команды. Попробуйте воспользоваться кнопками меню:
                """);

        telegramBot.execute(sendMessage);
    }

    @Override
    public void executeSendRationButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить рацион*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *рацион* питомца.
                Введите текстом рацион и нажимте кнопку *Отправить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendFeelingButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить самочувствие*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *самочувствие* питомца.
                Введите текстом самочувствие и нажимте кнопку "Отправить"
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendChangesButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить изменения*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *изменения в поведении* питомца.
                Введите текстом изменения и нажимте кнопку *Отправить*
                 """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendPhotoButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить фотографию*.
                Теперь нажатие кнопок недоступно, пока вы не отправите *фотографию* питомца.
                Загрузите фотографию и нажимте кнопку *Отправить*
                 """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeSendContactsButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Отправить контакты*.
                Теперь нажатие кнопок недоступно, пока вы не отправите свой *контакт*.
                Выберите свой контакт и нажмите кнопку *Отправить*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void executeCallVolunteerButton(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Ранее вы нажали на кнопку *Позвать волонтера*.
                Теперь нажатие кнопок недоступно, пока вы не завершите чат.
                Нажмите на кнопку *Завершить чат* или отправьте команду */cats_close*
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
    public void rationAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня рацион. Отправлять сегодня рацион больше не нужно.
                Выберите другой пункт меню:
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
    public void changesAlreadySentMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                Вы отправили сегодня изменения в поведении животного. Отправлять сегодня изменени больше не нужно.
                Выберите другой пункт меню:
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public void chatAlreadySetMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
        telegramBot.execute(new EditMessageText(chatId, messageId, """
                У вас уже открыт чат с волонтером. Для завершения отправьте *cats_close*
                """)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup));
    }

//    @Override
//    public void waitingForContactMessage(Long chatId, int messageId, InlineKeyboardMarkup inlineKeyboardMarkup) {
//        telegramBot.execute(new EditMessageText(chatId, messageId, """
//                Вы нажали кнопку *Оставить контактные данные* и мы ожидаем подтверждение отправки.
//                Нажмите кнопку "Поделиться контактом* под клавиатурой.
//                """)
//                .parseMode(ParseMode.Markdown)
//                .replyMarkup(inlineKeyboardMarkup));
//    }

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
        SendMessage messageToOwner = new SendMessage(ownerChatId, """
                Волонтер подтвердил готовность проконсультровать Вас. Отправьте ему свой вопрос.
                Для завершения чата отправьте команду */cats_close*.
                """)
                .parseMode(ParseMode.Markdown);
        telegramBot.execute(messageToOwner);

        SendMessage sendMessage = new SendMessage(volunteerChatId, """
                Вы подтвердили чат с клиентом. Дождитесь вопроса от него.
                Для завершения чата отправьте команду */cats_close*.
                """)
                .parseMode(ParseMode.Markdown);
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
}
