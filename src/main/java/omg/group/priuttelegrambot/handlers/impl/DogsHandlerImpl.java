package omg.group.priuttelegrambot.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.report.DogsReport;
import omg.group.priuttelegrambot.handlers.DogsHandler;
import omg.group.priuttelegrambot.repository.OwnersDogsRepository;
import omg.group.priuttelegrambot.repository.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
import omg.group.priuttelegrambot.service.OwnersDogsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class DogsHandlerImpl implements DogsHandler {

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
    private final KnowledgebaseDogsService knowledgebaseDogsService;
    private final TelegramBot telegramBot;
    private final OwnersDogsService ownersDogsService;
    private final OwnersDogsRepository ownersDogsRepository;
    private final ReportsDogsRepository reportsDogsRepository;


    public DogsHandlerImpl(KnowledgebaseDogsService knowledgebaseDogsService,
                           TelegramBot telegramBot,
                           OwnersDogsService ownersDogsService,
                           OwnersDogsRepository ownersDogsRepository,
                           ReportsDogsRepository reportsDogsRepository) {
        this.knowledgebaseDogsService = knowledgebaseDogsService;
        this.telegramBot = telegramBot;
        this.ownersDogsService = ownersDogsService;
        this.ownersDogsRepository = ownersDogsRepository;
        this.reportsDogsRepository = reportsDogsRepository;
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
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton("Расписание работы, адрес, схема проезда").callbackData("/dog_timetable"));
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
    /**
     * Command processing for Cat's commands
     */
    @Override
    public void executeButtonOrCommand(Update update, InlineKeyboardMarkup inlineKeyboardMarkup) {

        extractDataFromUpdate(update);

        String description = knowledgebaseDogsService.findMessageByCommand(command).getCommandDescription();
        String message = knowledgebaseDogsService.findMessageByCommand(command).getMessage();

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

        if (!ownersDogsService.findByChatId(chatId)) {

            OwnerDogDto ownerDogDto = new OwnerDogDto();

            ownerDogDto.setChatId(chatId);
            ownerDogDto.setUserName(userName);
            ownerDogDto.setName(firstName);
            ownerDogDto.setSurname(lastName);
            ownerDogDto.setIsVolunteer(false);
            ownerDogDto.setFirstProbation(false);

            ownersDogsService.add(ownerDogDto);
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



        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(chatId);

        if (ownerDog.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerDog.get().getDogId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerDog.get().getId();
                Optional<DogsReport> dogsReportOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (dogsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (dogsReportOptional.get().getPath() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        DogsReport dogsReport = dogsReportOptional.get();
                        dogsReport.setPath(filePath);
                        dogsReport.setUpdatedAt(LocalDateTime.now());

                        reportsDogsRepository.save(dogsReport); // Save photoPath in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Фотография успешно сохранена*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else if (reportsDogsRepository.findByPath(filePath).isPresent()) {
                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                Вы уже отсылали такую фотографию.
                                Отошлите свежую фотографию.""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path
                    LocalDateTime probationEnds = ownerDog.get().getProbationEnds();

                    DogsReport dogsReport = new DogsReport();

                    dogsReport.setClientId(clientId);
                    dogsReport.setAnimalId(catId);
                    dogsReport.setCreatedAt(localDateTime);
                    dogsReport.setPath(filePath);
                    dogsReport.setDateReport(LocalDate.now());
                    dogsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsDogsRepository.save(dogsReport);

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

        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(chatId);

        if (ownerDog.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerDog.get().getDogId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerDog.get().getId();
                Optional<DogsReport> dogsReportOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (dogsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (dogsReportOptional.get().getRation() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        DogsReport dogsReport = dogsReportOptional.get();
                        dogsReport.setRation(command);
                        dogsReport.setUpdatedAt(LocalDateTime.now());

                        reportsDogsRepository.save(dogsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введеный *рацион* успешно сохранен*.
                                    Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String ration = dogsReportOptional.get().getRation();
                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, String.format("""
                                        Вы уже отсылали сегодня рацион.
                                        Вот он: 
                                        %s""", ration))
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path

                    LocalDateTime probationEnds = ownerDog.get().getProbationEnds();

                    DogsReport dogsReport = new DogsReport();

                    dogsReport.setClientId(clientId);
                    dogsReport.setAnimalId(catId);
                    dogsReport.setCreatedAt(localDateTime);
                    dogsReport.setRation(command);
                    dogsReport.setDateReport(LocalDate.now());
                    dogsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsDogsRepository.save(dogsReport);

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

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);

        if (ownerDogOptional.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerDogOptional.get().getDogId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerDogOptional.get().getId();
                Optional<DogsReport> dogsReportOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (dogsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (dogsReportOptional.get().getFeeling() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        DogsReport dogsReport = dogsReportOptional.get();
                        dogsReport.setFeeling(command);
                        dogsReport.setUpdatedAt(LocalDateTime.now());

                        reportsDogsRepository.save(dogsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введенный отчет *о самочувствии животного* успешно сохранен*.
                                    Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String feeling = dogsReportOptional.get().getFeeling();

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, String.format("""
                                        Вы уже отсылали сегодня отчет о *самочувствии животного*.
                                        Вот он:
                                        %s""", feeling))
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));
                    }
                } else { // If report for today doesn't exist - create new report with photo path

                    LocalDateTime probationEnds = ownerDogOptional.get().getProbationEnds();

                    DogsReport dogsReport = new DogsReport();

                    dogsReport.setClientId(clientId);
                    dogsReport.setAnimalId(catId);
                    dogsReport.setCreatedAt(localDateTime);
                    dogsReport.setFeeling(command);
                    dogsReport.setDateReport(LocalDate.now());
                    dogsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsDogsRepository.save(dogsReport);

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

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);

        if (ownerDogOptional.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            Long catId = ownerDogOptional.get().getDogId();
            if (catId != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long clientId = ownerDogOptional.get().getId();
                Optional<DogsReport> dogsReportOptional = reportsDogsRepository.findByClientIdAndAnimalIdAndDateReport(clientId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (dogsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (dogsReportOptional.get().getChanges() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        DogsReport dogsReport = dogsReportOptional.get();
                        dogsReport.setChanges(command);
                        dogsReport.setUpdatedAt(LocalDateTime.now());

                        reportsDogsRepository.save(dogsReport); // Save ration in report out of update

                        InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                        telegramBot.execute(new SendMessage(chatId, """
                                *Введенный отчет 
                                *об изменениях в поведении животного* 
                                успешно сохранен*.
                                Выберите следующую команду:""")
                                .parseMode(ParseMode.Markdown)
                                .replyMarkup(inlineKeyboardMarkup));

                    } else {
                        String changes = dogsReportOptional.get().getChanges();

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

                    LocalDateTime probationEnds = ownerDogOptional.get().getProbationEnds();

                    DogsReport dogsReport = new DogsReport();

                    dogsReport.setClientId(clientId);
                    dogsReport.setAnimalId(catId);
                    dogsReport.setCreatedAt(localDateTime);
                    dogsReport.setChanges(command);
                    dogsReport.setDateReport(LocalDate.now());
                    dogsReport.setDateLastReport(probationEnds.toLocalDate());

                    reportsDogsRepository.save(dogsReport);

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

        Optional<OwnerDog> ownerDogOptional = ownersDogsRepository.findByChatId(chatId);

        if (ownerDogOptional.isPresent() && ownerDogOptional.get().getProbationStarts() == null) {
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