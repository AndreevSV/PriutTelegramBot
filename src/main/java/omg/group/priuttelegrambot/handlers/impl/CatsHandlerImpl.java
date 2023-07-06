package omg.group.priuttelegrambot.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.report.CatsReport;
import omg.group.priuttelegrambot.handlers.CatsHandler;
import omg.group.priuttelegrambot.repository.OwnersCatsRepository;
import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class CatsHandlerImpl implements CatsHandler {
    private Long chatId;
    private int messageId;
    private String command;
    private String userName;
    private String firstName;
    private String lastName;
    private PhotoSize[] photos;
    Integer date;

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
     * Extracting fields from Update to use them in method: executeButtonOrCommand
     */


    private void extractDataFromUpdate(Update update) {

        if (update.message() != null) {
            chatId = update.message().chat().id();
            messageId = update.message().messageId();
            command = update.message().text();
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
            photos = update.message().photo();
            date = update.message().date();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            messageId = update.callbackQuery().message().messageId();
            command = update.callbackQuery().data();
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
        }
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

        extractDataFromUpdate(update);

        String description = knowledgebaseCatsService.findMessageByCommand(command).getCommandDescription();
        String message = knowledgebaseCatsService.findMessageByCommand(command).getMessage();

        EditMessageText editedMessage = new EditMessageText(chatId, messageId, description + "\n" + message)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(editedMessage);
    }

//    @Override
//    public void sendMessageAfterSendReport(Update update, InlineKeyboardMarkup inlineKeyboardMarkup) {
//
//        extractDataFromUpdate(update);
//
//        String description = knowledgebaseCatsService.findMessageByCommand("/cat_send_report").getCommandDescription();
//        String message = knowledgebaseCatsService.findMessageByCommand("/cat_send_report").getMessage();
//
//        EditMessageText editedMessage = new EditMessageText(chatId, messageId, description + "\n" + message)
//                .disableWebPagePreview(true)
//                .parseMode(ParseMode.Markdown)
//                .replyMarkup(null);
//
//        telegramBot.execute(editedMessage);
//    }


    /**
     * New user for the Dog's shelter registration - put in the database
     */
    @Override
    public void newOwnerRegister() {

        if (!ownersCatsService.findByChatId(chatId)) {

            OwnerCatDto ownerCatDto = new OwnerCatDto();

            ownerCatDto.setChatId(chatId);
            ownerCatDto.setUserName(userName);
            ownerCatDto.setName(firstName);
            ownerCatDto.setSurname(lastName);
            ownerCatDto.setIsVolunteer(false);
            ownerCatDto.setFirstProbation(false);

            ownersCatsService.add(ownerCatDto);
        }
    }

    @Override
    @Transactional
    public void receivePhoto(Update update) {

        extractDataFromUpdate(update);

        PhotoSize photo = photos[photos.length - 1];
        String fileId = photo.fileId();
        String filePath = "https://api.telegram.org/bot" + token + "/getFile?file_id=" + fileId;

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();



        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerCat.get().getCatId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerCat.get().getId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getPath() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        CatsReport catsReport = catsReportOptional.get();
                        catsReport.setPath(filePath);
                        catsReport.setUpdatedAt(LocalDateTime.now());

                        reportsCatsRepository.save(catsReport); // Save photoPath in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Фотография успешно сохранена*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else if (reportsCatsRepository.findByPath(filePath).isPresent()) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                Вы уже отсылали такую фотографию. 
                                Отошлите свежую фотографию.""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path
                    LocalDateTime probationEnds = ownerCat.get().getProbationEnds();

                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(clientId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setPath(filePath);
                    catsReport.setDateReport(LocalDate.now());
                    catsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsCatsRepository.save(catsReport);

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
                }
            } else {
                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
                telegramBot.execute(new SendMessage(chatId, """
                        У Вас еще нет домашнего питомца
                        и Вы не можете отправлять отчет.
                        Просмотрите информацию, как взять себе питомца.
                        Для этого нажмите соответствующу кнопку ниже""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
        }
    }

    @Override
    public void receiveRation(Update update) {

        extractDataFromUpdate(update);

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerCat.get().getCatId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerCat.get().getId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getRation() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        CatsReport catsReport = catsReportOptional.get();
                        catsReport.setRation(command);
                        catsReport.setUpdatedAt(LocalDateTime.now());

                        reportsCatsRepository.save(catsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введеный *рацион* успешно сохранен*.
                                    Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String ration = catsReportOptional.get().getRation();
                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, String.format("""
                                        Вы уже отсылали сегодня рацион.
                                        Вот он: 
                                        %s""", ration))
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path

                    LocalDateTime probationEnds = ownerCat.get().getProbationEnds();

                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(clientId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setRation(command);
                    catsReport.setDateReport(LocalDate.now());
                    catsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsCatsRepository.save(catsReport);

                    InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            Создан новый отчет за сегодняший день и
                            высланный рацион успешно сохранен.
                            Вам необходимо дополнительно отослать 
                            следующую информацию:
                            - фотограцию животного;
                            - самочувствие животного;
                            - произошедшие изменения.
                            Для этого нажмите соответствующую кнопку""")
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                }
            } else {
                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
                telegramBot.execute(new SendMessage(chatId, """
                        У Вас еще нет домашнего питомца
                        и Вы не можете отправлять отчет.
                        Просмотрите информацию, как взять себе питомца.
                        Для этого нажмите соответствующую кнопку ниже""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующую кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
        }

    }

    @Override
    public void receiveFeeling(Update update) {

        extractDataFromUpdate(update);

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerCat.get().getCatId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerCat.get().getId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getFeeling() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        CatsReport catsReport = catsReportOptional.get();
                        catsReport.setFeeling(command);
                        catsReport.setUpdatedAt(LocalDateTime.now());

                        reportsCatsRepository.save(catsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введенный отчет *о самочувствии животного* успешно сохранен*.
                                    Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String feeling = catsReportOptional.get().getFeeling();

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, String.format("""
                                        Вы уже отсылали сегодня отчет о *самочувствии животного*.
                                        Вот он: 
                                        %s""", feeling))
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path

                    LocalDateTime probationEnds = ownerCat.get().getProbationEnds();

                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(clientId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setFeeling(command);
                    catsReport.setDateReport(LocalDate.now());
                    catsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsCatsRepository.save(catsReport);

                    InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                    telegramBot.execute(new SendMessage(chatId, """
                            Создан новый отчет за сегодняший день и
                            высланный отчет о *самочувствии животного* успешно сохранен.
                            Вам необходимо дополнительно отослать
                            следующую информацию:
                            - фотограцию животного;
                            - рацион животного;
                            - произошедшие изменения.
                            Для этого нажмите соответствующую кнопку""")
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                }
            } else {
                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
                telegramBot.execute(new SendMessage(chatId, """
                        У Вас еще нет домашнего питомца
                        и Вы не можете отправлять отчет.
                        Просмотрите информацию, как взять себе питомца.
                        Для этого нажмите соответствующую кнопку ниже""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующую кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
        }

    }

    @Override
    public void receiveChanges(Update update) {

        extractDataFromUpdate(update);

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerCat.get().getCatId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerCat.get().getId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getChanges() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        CatsReport catsReport = catsReportOptional.get();
                        catsReport.setChanges(command);
                        catsReport.setUpdatedAt(LocalDateTime.now());

                        reportsCatsRepository.save(catsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введенный отчет 
                                *об изменениях в поведении животного* 
                                успешно сохранен*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String changes = catsReportOptional.get().getChanges();

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, String.format("""
                                Вы уже отсылали сегодня отчет
                                *об изменениях в поведении животного*
                                Вот он:
                                %s
                                Выберите следующую команду:""", changes))
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path

                    LocalDateTime probationEnds = ownerCat.get().getProbationEnds();

                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(clientId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setChanges(command);
                    catsReport.setDateReport(LocalDate.now());
                    catsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsCatsRepository.save(catsReport);

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
                            Для этого нажмите соответствующую кнопку""")
                            .parseMode(ParseMode.Markdown)
                            .replyMarkup(inlineKeyboardMarkup));
                }
            } else {
                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
                telegramBot.execute(new SendMessage(chatId, """
                        У Вас еще нет домашнего питомца
                        и Вы не можете отправлять отчет.
                        Просмотрите информацию, как взять себе питомца.
                        Для этого нажмите соответствующую кнопку ниже""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));
            }
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующую кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
        }
    }

    @Override
    public Boolean isProbationStarted(Update update) {

        extractDataFromUpdate(update);

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent() && ownerCat.get().getProbationStarts() == null) {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
            telegramBot.execute(new SendMessage(chatId, """
                                У вас еще не назначен испытательный срок.
                                Вы не можете отправлять отчет.
                                Если вы взяли животное, но видите это сообщение,
                                попросите своего менеджера установить Вам
                                испытательный срок.
                                Выберите следующую команду:""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return false;
        }
        return true;
    }

//    @Override
//    public Boolean isProbationEnded(Update update) {
//
//        extractDataFromUpdate(update);
//
//        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);
//
//        if (ownerCat.isPresent() && ownerCat.get().getProbationEnds() == null) {
//            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
//            telegramBot.execute(new SendMessage(chatId, """
//                                У вас уже закончился испытательный срок.
//                                Вам не нужно больше отправлять отчет.
//                                Выберите следующую команду:""")
//                    .parseMode(ParseMode.Markdown)
//                    .replyMarkup(inlineKeyboardMarkup));
//            return
//        }
//    }
}



