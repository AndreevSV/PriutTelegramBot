package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.pets.Cat;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.report.ReportCat;
import omg.group.priuttelegrambot.handlers.pets.CatsHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersCatsRepository;
import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class CatsHandlerImpl implements CatsHandler {

    private final int DAYS_OF_PROBATION = 14;

    @Value("${telegram.bot.token}")
    private String token;
    private final KnowledgebaseCatsService knowledgebaseCatsService;
    private final TelegramBot telegramBot;
    private final OwnersCatsService ownersCatsService;
    private final OwnersCatsRepository ownersCatsRepository;
    private final ReportsCatsRepository reportsCatsRepository;

    public CatsHandlerImpl(KnowledgebaseCatsService knowledgebaseCatsService,
                           TelegramBot telegramBot,
                           OwnersCatsService ownersCatsService,
                           OwnersCatsRepository ownersCatsRepository,
                           ReportsCatsRepository reportsCatsRepository) {
        this.knowledgebaseCatsService = knowledgebaseCatsService;
        this.telegramBot = telegramBot;
        this.ownersCatsService = ownersCatsService;
        this.ownersCatsRepository = ownersCatsRepository;
        this.reportsCatsRepository = reportsCatsRepository;
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
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы, адрес, схема проезда").callbackData("/cat_timetable"));
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
        Long chatId = 0L;
        int messageId = 0;
        String command = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            messageId = update.message().messageId();
            command = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
        }

        String description = knowledgebaseCatsService.findMessageByCommand(command).getCommandDescription();
        String message = knowledgebaseCatsService.findMessageByCommand(command).getMessage();

        EditMessageText editedMessage = new EditMessageText(chatId, messageId, description + "\n" + message)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(editedMessage);
    }

    /**
     * New user for the Cat's shelter registration - put in the database
     */
    @Override
    public void newOwnerRegister(@NotNull Update update) {

        Long chatId = 0L;
        String userName = "";
        String firstName = "";
        String lastName = "";
        int date = 0;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
            date = update.message().date();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
            date = update.callbackQuery().message().date();
        }

        LocalDateTime registrationDate = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (!ownersCatsService.findByChatId(chatId)) {

            OwnerCatDto ownerCatDto = new OwnerCatDto();
            ownerCatDto.setChatId(chatId);
            ownerCatDto.setUserName(userName);
            ownerCatDto.setName(firstName);
            ownerCatDto.setSurname(lastName);
            ownerCatDto.setIsVolunteer(false);
            ownerCatDto.setCreatedAt(registrationDate);

            ownersCatsService.add(ownerCatDto);
        }
    }

    /**
     * Method checks if Owner of the Cat(s) exists
     */
    @Override
    public OwnerCat checkForOwnerExist(Update update) {

        Long chatId = update.message().from().id();
        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) {
            return ownerCat.get();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new OwnerCat();
        }
    }

    /**
     * Method checks if Owner has a Cat(s)
     */
    @Override
    public List<Cat> checkForOwnerHasCat(OwnerCat ownerCat) {

        Long chatId = ownerCat.getChatId();

        if (!ownerCat.getCats().isEmpty()) {
            return ownerCat.getCats();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    У Вас еще нет домашнего питомца
                    и Вы не можете отправлять отчет.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new ArrayList<>();
        }
    }

    /**
     * Method checks if Cat(s) on the probation period
     */
    @Override
    public List<Cat> checkForCatsOnProbation(List<Cat> cats) {

        if (!cats.isEmpty()) {

            List<Cat> catsOnProbation = new ArrayList<>();

            for (Cat cat : cats) {
                if (cat.getFirstProbation().equals(true) || cat.getSecondProbation().equals(true)) {
                    catsOnProbation.add(cat);
                }
            }
            return catsOnProbation;
        }
        return new ArrayList<>();
    }

    /**
     * Method checks if quantity Cat(s) in the probation period more than 1, then - true
     */
    @Override
    public Boolean checkForCatsOnProbationMoreThanOne(List<Cat> cats) {
        return cats.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a cat(s), cat(s) on a probation period and returns
     * list of cats on a probation
     */
    @Override
    public List<Cat> returnCatsOnProbation(Update update) {

        OwnerCat ownerCat = checkForOwnerExist(update);

        if (ownerCat != null) {
            List<Cat> cats = checkForOwnerHasCat(ownerCat);
            if (!cats.isEmpty()) {
                List<Cat> catsInProbation = checkForCatsOnProbation(cats);
                if (!catsInProbation.isEmpty()) {
                    return catsInProbation;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public Cat returnOneCatOnProbation(Update update) {

        Long chatId = 0L;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        List<Cat> cats = returnCatsOnProbation(update);

        Long idCat = Long.valueOf(text);

        if (!checkForCatsOnProbationMoreThanOne(cats) && !cats.isEmpty()) {

            return cats.get(0);

        } else if (checkForCatsOnProbationMoreThanOne(cats) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке
                    со следующими кошками:
                    """;

            for (Cat cat : cats) {
                id = cat.getId();
                nickName = cat.getNickName();
                String string = String.format("""
                        Номер %d, кличка %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку кошки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForCatsOnProbationMoreThanOne(cats) && !update.message().text().isEmpty()) {

            boolean found = false;

            for (Cat cat : cats) {
                if (cat.getId().equals(idCat) || cat.getNickName().equals(text)) {
                    return cat;
                }
            }

            if (!found) {
                SendMessage message = new SendMessage(chatId, """
                        Введенный вами номер животного
                        или кличка не верны. Попробуйте
                        ввести снова ЛИБО номер ЛИБО кличку.
                        """);
                telegramBot.execute(message);
            }
        }
        return null;
    }

    /**
     * Method checks if report for today exist in ReportsCatsRepository
     */
    @Override
    public Boolean isReportExist(Update update) {

        Cat cat = returnOneCatOnProbation(update);

        Long animalId = cat.getId();
        Long clientId = cat.getOwnerCat().getId();

        Optional<ReportCat> report = reportsCatsRepository
                .findByClientIdAndAnimalIdAndDateReport(clientId, animalId, LocalDate.now());

        return report.isPresent();
    }

    /**
     * Method that receives photo and stores it in database
     */
    @Override
    @Transactional
    public void receivePhoto(Update update) {

        Long chatId = 0L;
        PhotoSize[] photos = new PhotoSize[0];
        Integer date = null;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            photos = update.message().photo();
            date = update.message().date();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        PhotoSize photo = photos[photos.length - 1];
        String fileId = photo.fileId();
        String filePath = "https://api.telegram.org/bot" + token + "/getFile?file_id=" + fileId;

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Cat cat = returnOneCatOnProbation(update);

        Long clientId = cat.getOwnerCat().getId();
        Long catId = cat.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportCat report = new ReportCat(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(catId);
            report.setPath(filePath);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsCatsRepository.save(report); // Save photoPath in report

            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланная фотография успешно сохранена.
                    Вам необходимо отослать следующую информацию:
                    - рацион животного;
                    - самочувствие животного;
                    - произошедшие изменения.
                    Для этого нажмите соответствующую кнопку""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

        } else if (isReportExist(update)) {

            Optional<ReportCat> report = reportsCatsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

            if (report.isPresent() && report.get().getPath().isEmpty()) {

                report.get().setPath(filePath);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsCatsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Фотография успешно сохранена*.
                        Выберите следующую команду:""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

            }
            if (report.isPresent() && !report.get().getPath().isEmpty() &&
                    reportsCatsRepository.findByPath(filePath).isPresent()) {

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        Вы уже отсылали такую фотографию.
                        Отошлите свежую фотографию.""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        }
    }


    /**
     * Method that receives ration of the animal and stores it in database
     */
    @Override
    public void receiveRation(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Cat cat = returnOneCatOnProbation(update);

        Long clientId = cat.getOwnerCat().getId();
        Long catId = cat.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportCat report = new ReportCat(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(catId);
            report.setRation(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsCatsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный рацион успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - самочувствие животного;
                    - произошедшие изменения.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

        } else if (isReportExist(update)) {

            Optional<ReportCat> report = reportsCatsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

            if (report.isPresent() && report.get().getRation().isEmpty()) {

                report.get().setRation(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsCatsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Введеный *рацион* успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

            }
            if (report.isPresent() && !report.get().getRation().isEmpty()) {

                String ration = report.get().getRation();

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня рацион.
                        Вот он:
                        %s""", ration))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        }
    }

    /**
     * Method that receives self-feeling of the animal and stores it in database
     */
    @Override
    public void receiveFeeling(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Cat cat = returnOneCatOnProbation(update);

        Long clientId = cat.getOwnerCat().getId();
        Long catId = cat.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportCat report = new ReportCat(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(catId);
            report.setFeeling(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsCatsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный отчет о *самочувствии животного* успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - рацион животного;
                    - произошедшие изменения.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

        } else if (isReportExist(update)) {

            Optional<ReportCat> report = reportsCatsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

            if (report.isPresent() && report.get().getRation().isEmpty()) {

                report.get().setFeeling(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsCatsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Введенный отчет *о самочувствии животного*
                        успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

            }
            if (report.isPresent() && !report.get().getRation().isEmpty()) {

                String feeling = report.get().getFeeling();

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня отчет
                        *о самочувствии животного*.
                        Вот он:
                        %s""", feeling))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        }
    }

    /**
     * Method that receives changes that happened with the animal and stores it in database
     */
    @Override
    public void receiveChanges(Update update) {
        Long chatId = 0L;
        Integer date = null;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            date = update.message().date();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Cat cat = returnOneCatOnProbation(update);

        Long clientId = cat.getOwnerCat().getId();
        Long catId = cat.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportCat report = new ReportCat(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(catId);
            report.setChanges(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsCatsRepository.save(report);

            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Создан новый отчет за сегодняший день и
                    высланный отчет *об изменениях в поведении
                    животного* успешно сохранен.
                    Вам необходимо дополнительно отослать
                    следующую информацию:
                    - фотограцию животного;
                    - рацион животного;
                    - самочувствие животного.
                    Для этого нажмите соответствующую кнопку
                    """)
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

        } else if (isReportExist(update)) {

            Optional<ReportCat> report = reportsCatsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now());

            if (report.isPresent() && report.get().getChanges().isEmpty()) {

                report.get().setChanges(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsCatsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        **Введенный отчет *об изменениях в
                        поведении животного* успешно сохранен*.
                        Выберите следующую команду:
                        """)
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

            }
            if (report.isPresent() && !report.get().getRation().isEmpty()) {

                String changes = report.get().getChanges();

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, String.format("""
                        Вы уже отсылали сегодня отчет
                        *об изменениях в поведении животного*
                        Вот он:
                        %s""", changes))
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        }
    }

    /**
     * Method that checks if a probation period for the new owner of the animal set and valid
     */
    @Override
    public boolean checkForProbationPeriodSetAndValid(@NotNull Update update) {

        Long chatId = 0L;
        String userName = null;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            userName = update.message().chat().username();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        Cat cat = returnOneCatOnProbation(update);

        LocalDate probationStarts = cat.getProbationStarts();
        LocalDate probationEnds = cat.getProbationEnds();

        String probationStartsString = String.format("%tF", probationStarts);
        String probationEndsString = String.format("%tF", probationEnds);


        if (probationStarts == null) {

            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForInfoMenuButton();

            telegramBot.execute(new SendMessage(chatId, """
                    У вас еще не назначен испытательный срок.
                    Вы не можете отправлять отчет.
                    Если вы взяли животное, но видите это сообщение,
                    значит Ваш менеджер не установил Вам испытательный срок.
                    Мы отправили ему запрос на установление испытательного
                    срока. Попробуйте отослать отчет чуть позже.
                    Выберите следующую команду:""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));

            OwnerCat ownerCat = cat.getOwnerCat();

            Long volunteerChatId = ownerCat.getVolunteer().getChatId();

            SendMessage sendMessage = new SendMessage(volunteerChatId, String.format("""
                    Клиент %s с chatId %d пытается отправить Вам 1-й
                    отчет по своему животному, но вы не установили
                    ему испытательный срок в системе. Пожалуйста,
                    установите ему испытательный срок.
                    """, userName, chatId));
            telegramBot.execute(sendMessage);

            return false;

        } else if ((LocalDate.now().isAfter(probationStarts) || LocalDate.now().isEqual(probationStarts)) &&
                (LocalDate.now().isBefore(probationEnds) || LocalDate.now().isEqual(probationEnds))) {

            return true;

        } else if (LocalDate.now().isBefore(probationStarts)) {

            InlineKeyboardMarkup inlineKeyboardMarkup = formPriutMainMenuButton();

            SendMessage sendMessage = new SendMessage(chatId, String.format("""
                    У вас еще не начался испытательный срок.
                    Вы можете начать отправлять отчет начиная
                    с %s
                    """, probationStartsString))
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(sendMessage);

            return false;

        } else if (LocalDate.now().isAfter(probationEnds)) {

            InlineKeyboardMarkup inlineKeyboardMarkup = formPriutMainMenuButton();

            SendMessage sendMessage = new SendMessage(chatId, String.format("""
                    У вас уже закончился испытательный срок
                    %s.
                    Вам не нужно больше отправлять отчет.
                    Выберите следующую команду:
                    """, probationEndsString))
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup);

            telegramBot.execute(sendMessage);
        }

        return false;
    }

}



