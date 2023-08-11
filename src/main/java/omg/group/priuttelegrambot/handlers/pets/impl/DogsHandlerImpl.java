package omg.group.priuttelegrambot.handlers.pets.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import omg.group.priuttelegrambot.dto.owners.OwnerDogDto;
import omg.group.priuttelegrambot.entity.pets.Dog;
import omg.group.priuttelegrambot.entity.owners.OwnerDog;
import omg.group.priuttelegrambot.entity.report.ReportDog;
import omg.group.priuttelegrambot.handlers.pets.DogsHandler;
import omg.group.priuttelegrambot.repository.owners.OwnersDogsRepository;
import omg.group.priuttelegrambot.repository.ReportsDogsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseDogsService;
import omg.group.priuttelegrambot.service.OwnersDogsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DogsHandlerImpl implements DogsHandler {

    private final int DAYS_OF_PROBATION = 14;
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

        String description = knowledgebaseDogsService.findMessageByCommand(command).getCommandDescription();
        String message = knowledgebaseDogsService.findMessageByCommand(command).getMessage();

        EditMessageText editedMessage = new EditMessageText(chatId, messageId, description + "\n" + message)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(editedMessage);
    }

    /**
     * New user for the Dog's shelter registration - put in the database
     */
    @Override
    public void newOwnerRegister(@NotNull Update update) {

        Long chatId = 0L;
        Long telegramUserId = 0L;
        String userName = "";
        String firstName = "";
        String lastName = "";
        int date = 0;

        if (update.message() != null) {
            chatId = update.message().chat().id();
            telegramUserId = update.message().from().id();
            userName = update.message().from().username();
            firstName = update.message().from().firstName();
            lastName = update.message().from().lastName();
            date = update.message().date();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
            telegramUserId = update.callbackQuery().from().id();
            userName = update.callbackQuery().from().username();
            firstName = update.callbackQuery().from().firstName();
            lastName = update.callbackQuery().from().lastName();
            date = update.callbackQuery().message().date();
        }

        LocalDateTime registrationDate = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        if (!ownersDogsService.findByChatId(chatId)) {

            OwnerDogDto ownerDogDto = new OwnerDogDto();

            ownerDogDto.setChatId(chatId);
            ownerDogDto.setTelegramUserId(telegramUserId);
            ownerDogDto.setUserName(userName);
            ownerDogDto.setName(firstName);
            ownerDogDto.setSurname(lastName);
            ownerDogDto.setIsVolunteer(false);
            ownerDogDto.setCreatedAt(registrationDate);
            ownerDogDto.setVolunteerChatOpened(false);

            ownersDogsService.add(ownerDogDto);
        }
    }

    /**
     * Method checks if Owner of the Dog(s) exists
     */
    @Override
    public OwnerDog checkForOwnerExist(Update update) {

        Long chatId = update.message().from().id();
        Optional<OwnerDog> ownerDog = ownersDogsRepository.findByChatId(chatId);

        if (ownerDog.isPresent()) {
            return ownerDog.get();
        } else {
            InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForTakeMenuButton();
            telegramBot.execute(new SendMessage(chatId, """
                    Вы еще не зарегистрированы как владелец животного.
                    Просмотрите информацию, как взять себе питомца.
                    Для этого нажмите соответствующу кнопку ниже""")
                    .parseMode(ParseMode.Markdown)
                    .replyMarkup(inlineKeyboardMarkup));
            return new OwnerDog();
        }
    }

    /**
     * Method checks if Owner has a Dog(s)
     */
    @Override
    public List<Dog> checkForOwnerHasDog(OwnerDog ownerDog) {

        Long chatId = ownerDog.getChatId();

        if (!ownerDog.getDogs().isEmpty()) {
            return ownerDog.getDogs();
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
     * Method checks if Dog(s) on the probation period
     */
    @Override
    public List<Dog> checkForDogsOnProbation(List<Dog> dogs) {

        if (!dogs.isEmpty()) {

            List<Dog> dogsOnProbation = new ArrayList<>();

            for (Dog dog : dogs) {
                if ((dog.getFirstProbation() != null && dog.getFirstProbation().equals(true)) ||
                        (dog.getSecondProbation() != null && dog.getSecondProbation().equals(true))) {
                    dogsOnProbation.add(dog);
                }
            }
            return dogsOnProbation;
        }
        return new ArrayList<>();
    }

    /**
     * Method checks if quantity Dog(s) in the probation period more than 1, then - true
     */
    @Override
    public Boolean checkForDogsOnProbationMoreThanOne(List<Dog> dogs) {
        return dogs.size() > 1;
    }

    /**
     * Method checks if owner exist, owner has a dog(s), dog(s) on a probation period and returns
     * list of dogs on a probation
     */
    @Override
    public List<Dog> returnDogsOnProbation(Update update) {

        OwnerDog ownerDog = checkForOwnerExist(update);

        if (ownerDog != null) {
            List<Dog> dogs = checkForOwnerHasDog(ownerDog);
            if (!dogs.isEmpty()) {
                List<Dog> dogsInProbation = checkForDogsOnProbation(dogs);
                if (!dogsInProbation.isEmpty()) {
                    return dogsInProbation;
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * Method returns one animal on probation
     */
    @Override
    public Dog returnOneDogOnProbation(Update update) {

        Long chatId = 0L;
        String text = "";

        if (update.message() != null) {
            chatId = update.message().chat().id();
            text = update.message().text();
        } else if (update.callbackQuery() != null) {
            chatId = update.callbackQuery().message().chat().id();
        }

        List<Dog> dogs = returnDogsOnProbation(update);

        if (!checkForDogsOnProbationMoreThanOne(dogs) && !dogs.isEmpty()) {

            return dogs.get(0);

        } else if (checkForDogsOnProbationMoreThanOne(dogs) && update.message().text().isEmpty()) {

            Long id;
            String nickName;

            StringBuilder stringBuilder = new StringBuilder();

            String startMessage = """
                    Вы на испытательном сроке
                    со следующими собаками:
                    """;

            for (Dog dog : dogs) {
                id = dog.getId();
                nickName = dog.getNickName();
                String string = String.format("""
                        Номер %d, кличка %s
                        """, id, nickName);
                stringBuilder.append(string);
            }

            SendMessage message = new SendMessage(chatId,
                    startMessage + "\n " + stringBuilder +
                            "\n Отправьте номер или кличку собаки," +
                            "отчет по которой вы хотите отправить:");
            telegramBot.execute(message);

        } else if (checkForDogsOnProbationMoreThanOne(dogs) && !update.message().text().isEmpty()) {

            Long idDog = Long.valueOf(text);

            boolean found = false;

            for (Dog dog : dogs) {
                if (dog.getId().equals(idDog) || dog.getNickName().equals(text)) {
                    return dog;
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
        } else {
            SendMessage message = new SendMessage(chatId, """
                        У вас нет животного на испытательном
                        сроке, вы не можете ничего отправить.
                        """);
            telegramBot.execute(message);
        }
        return null;
    }

    /**
     * Method checks if report for today exist in ReportsDogsRepository
     */
    @Override
    public Boolean isReportExist(Update update) {

        Dog dog = returnOneDogOnProbation(update);

        Long animalId = dog.getId();
        Long clientId = dog.getOwnerDog().getId();

        Optional<ReportDog> report = reportsDogsRepository
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
        }

        PhotoSize photo = photos[photos.length - 1];
        String fileId = photo.fileId();
        String filePath = "https://api.telegram.org/bot" + token + "/getFile?file_id=" + fileId;

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Dog dog = returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setPath(filePath);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report); // Save photoPath in report

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

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && report.get().getPath().isEmpty()) {

                report.get().setPath(filePath);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

                InlineKeyboardMarkup inlineKeyboardMarkup = formInlineKeyboardForSendReportButton();
                telegramBot.execute(new SendMessage(chatId, """
                        *Фотография успешно сохранена*.
                        Выберите следующую команду:""")
                        .parseMode(ParseMode.Markdown)
                        .replyMarkup(inlineKeyboardMarkup));

            }
            if (report.isPresent() && !report.get().getPath().isEmpty() &&
                    reportsDogsRepository.findByPath(filePath).isPresent()) {

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

        Dog dog = returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setRation(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

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

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && (report.get().getRation() == null || report.get().getRation().isEmpty())) {

                report.get().setRation(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

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

        Dog dog = returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setFeeling(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

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

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && report.get().getRation().isEmpty()) {

                report.get().setFeeling(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

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

        Dog dog = returnOneDogOnProbation(update);

        Long clientId = dog.getOwnerDog().getId();
        Long dogId = dog.getId();

        if (!isReportExist(update)) { // If report for today not exist

            ReportDog report = new ReportDog(); // Set new report with filePath
            report.setClientId(clientId);
            report.setAnimalId(dogId);
            report.setChanges(text);
            report.setCreatedAt(localDateTime);
            report.setDateReport(LocalDate.now());
            report.setDateLastReport(LocalDate.now().plusDays(DAYS_OF_PROBATION));

            reportsDogsRepository.save(report);

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

            Optional<ReportDog> report = reportsDogsRepository
                    .findByClientIdAndAnimalIdAndDateReport(clientId, dogId, LocalDate.now());

            if (report.isPresent() && report.get().getChanges().isEmpty()) {

                report.get().setChanges(text);
                report.get().setUpdatedAt(LocalDateTime.now());

                reportsDogsRepository.save(report.get());

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

        Dog dog = returnOneDogOnProbation(update);

        LocalDate probationStarts = dog.getProbationStarts();
        LocalDate probationEnds = dog.getProbationEnds();

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

            OwnerDog ownerDog = dog.getOwnerDog();

            Long volunteerChatId = ownerDog.getVolunteer().getChatId();

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