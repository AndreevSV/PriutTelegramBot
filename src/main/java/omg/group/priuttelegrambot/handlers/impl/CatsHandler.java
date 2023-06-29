package omg.group.priuttelegrambot.handlers.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import omg.group.priuttelegrambot.dto.owners.OwnerCatDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.entity.owners.OwnerCat;
import omg.group.priuttelegrambot.entity.report.CatsReport;
import omg.group.priuttelegrambot.handlers.AnimalHandler;
import omg.group.priuttelegrambot.repository.CatsRepository;
import omg.group.priuttelegrambot.repository.OwnersCatsRepository;
import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.KnowledgebaseCatsService;
import omg.group.priuttelegrambot.service.OwnersCatsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@Qualifier("catsHandler")
public class CatsHandler implements AnimalHandler {

    private Long chatId;
    private int messageId;
    private String command;
    private String userName;
    private String firstName;
    private String lastName;

    @Value("${telegram.bot.token}")
    private String token;
    private final KnowledgebaseCatsService knowledgebaseCatsService;
    private final TelegramBot telegramBot;
    private final OwnersCatsService ownersCatsService;
    private final OwnersCatsRepository ownersCatsRepository;
    private final ReportsCatsRepository reportsCatsRepository;

    public CatsHandler(KnowledgebaseCatsService knowledgebaseCatsService,
                       TelegramBot telegramBot,
                       OwnersCatsService ownersCatsService,
                       OwnersCatsRepository ownersCatsRepository,
                       CatsRepository catsRepository1, ReportsCatsRepository reportsCatsRepository) {
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
            update.message().photo();
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

        EditMessageText sendMessage = new EditMessageText(chatId, messageId, description + "\n" + message)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.Markdown)
                .replyMarkup(inlineKeyboardMarkup);

        telegramBot.execute(sendMessage);

        newOwnerRegister();
    }

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
    public ReportsCatsDto receivePhoto(Update update) {

        Long chatId = update.message().chat().id();
        Integer date = update.message().date();
        PhotoSize[] photos = update.message().photo();
        Integer messageId = update.message().messageId();
        int updateId = update.updateId();



        PhotoSize photo = photos[photos.length - 1];
        String fileId = photo.fileId();
        String filePath = "https://api.telegram.org/file/bot" + token + "/" + fileId;

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            if ((ownerCat.get().getCatId()) != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long catId = ownerCat.get().getCatId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getPath() == null) { // If Client exist, has a cat, and report has been created, check-up if report has photo path field not empty

                        CatsReport catsReport = new CatsReport();
                        catsReport.setPath(filePath);

                        reportsCatsRepository.save(catsReport); // Save photoPath in report out of update

                        Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                                .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                        ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                        reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                        reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                        reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                        reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                        reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                        reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                        reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                        reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                        return reportsCatsDto;
                    }
                } else { // If report for today doesn't exist - create new report with photo path
                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(chatId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setPath(filePath);

                    reportsCatsRepository.save(catsReport);

                    Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                            .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                    ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                    reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                    reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                    reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                    reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                    reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                    reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                    reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                    reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                    return reportsCatsDto;
                }
            } else {
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        "У Вас еще нет домашнего питомца и Вы не можете отправлять отчет"));
                return null;
            }
        } else {
            telegramBot.execute(new EditMessageText(chatId, messageId,
                    "Вы еще не зарегистрированы как клиент приюта."));
            return null;
        }
        return null;
    }

    @Override
    public ReportsCatsDto receiveRation(Update update) {

        Long chatId = update.message().chat().id();
        Integer date = update.message().date();
        String message = update.message().text();
        Integer messageId = update.message().messageId();

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            if ((ownerCat.get().getCatId()) != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long catId = ownerCat.get().getCatId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getRation() == null) { // If Client exist, has a cat, and report has been created, check-up if report has Ration field not empty

                        CatsReport catsReport = new CatsReport();
                        catsReport.setRation(message);

                        reportsCatsRepository.save(catsReport); // Save ration in report out of update

                        Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                                .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                        ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                        reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                        reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                        reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                        reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                        reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                        reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                        reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                        reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                        return reportsCatsDto;
                    }
                } else {
                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(chatId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setRation(message);

                    reportsCatsRepository.save(catsReport);

                    Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                            .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                    ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                    reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                    reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                    reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                    reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                    reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                    reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                    reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                    reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                    return reportsCatsDto;
                }
            } else {
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        "У Вас еще нет домашнего питомца и Вы не можете отправлять отчет"));
                return null;
            }
        } else {
            telegramBot.execute(new EditMessageText(chatId, messageId,
                    "Вы еще не зарегистрированы как клиент приюта."));
            return null;
        }

        return null;
    }

    @Override
    public ReportsCatsDto receiveFeeling(Update update) {

        Long chatId = update.message().chat().id();
        Integer date = update.message().date();
        String message = update.message().text();
        Integer messageId = update.message().messageId();

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            if ((ownerCat.get().getCatId()) != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long catId = ownerCat.get().getCatId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getFeeling() == null) { // If Client exist, has a cat, and report has been created, check-up if report has Feeling field not empty

                        CatsReport catsReport = new CatsReport();
                        catsReport.setFeeling(message);

                        reportsCatsRepository.save(catsReport); // Save feeling in report out of update

                        Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                                .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                        ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                        reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                        reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                        reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                        reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                        reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                        reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                        reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                        reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                        return reportsCatsDto;
                    }
                } else {
                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(chatId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setFeeling(message);

                    reportsCatsRepository.save(catsReport);

                    Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                            .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                    ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                    reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                    reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                    reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                    reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                    reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                    reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                    reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                    reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                    return reportsCatsDto;
                }
            } else {
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        "У Вас еще нет домашнего питомца и Вы не можете отправлять отчет"));
                return null;
            }
        } else {
            telegramBot.execute(new EditMessageText(chatId, messageId,
                    "Вы еще не зарегистрированы как клиент приюта."));
            return null;
        }

        return null;
    }


    @Override
    public ReportsCatsDto receiveChanges(Update update) {

        Long chatId = update.message().chat().id();
        Integer date = update.message().date();
        String message = update.message().text();
        Integer messageId = update.message().messageId();

        LocalDateTime localDateTime = Instant.ofEpochSecond(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Optional<OwnerCat> ownerCat = ownersCatsRepository.findByChatId(chatId);

        if (ownerCat.isPresent()) { // Check-up if Client with such chatId is present in the Clients_cats database
            if ((ownerCat.get().getCatId()) != null) { // If Client exist, check-up if Client has a cat in the Cats database
                Long catId = ownerCat.get().getCatId();
                Optional<CatsReport> catsReportOptional = reportsCatsRepository
                        .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now()); // Return CatsReport for today (optional)
                if (catsReportOptional.isPresent()) { // If Client exist, Client has a cat, check-up if CatsReport exist in Report_cats database
                    if (catsReportOptional.get().getChanges() == null) { // If Client exist, has a cat, and report has been created, check-up if report has Ration field not empty

                        CatsReport catsReport = new CatsReport();
                        catsReport.setChanges(message);

                        reportsCatsRepository.save(catsReport); // Save ration in report out of update

                        Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                                .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                        ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                        reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                        reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                        reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                        reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                        reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                        reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                        reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                        reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                        return reportsCatsDto;
                    }
                } else {
                    CatsReport catsReport = new CatsReport();

                    catsReport.setClientId(chatId);
                    catsReport.setAnimalId(catId);
                    catsReport.setCreatedAt(localDateTime);
                    catsReport.setChanges(message);

                    reportsCatsRepository.save(catsReport);

                    Optional<CatsReport> catsReporAfterRenewalOptional = reportsCatsRepository
                            .findByClientIdAndAnimalIdAndCreatedAt(chatId, catId, LocalDate.now());

                    ReportsCatsDto reportsCatsDto = new ReportsCatsDto(); // Create reportDto after saving Ration

                    reportsCatsDto.setClientId(catsReporAfterRenewalOptional.get().getClientId());
                    reportsCatsDto.setAnimalId(catsReporAfterRenewalOptional.get().getAnimalId());
                    reportsCatsDto.setPath(catsReporAfterRenewalOptional.get().getPath());
                    reportsCatsDto.setCreatedAt(catsReporAfterRenewalOptional.get().getCreatedAt());
                    reportsCatsDto.setUpdatedAt(catsReporAfterRenewalOptional.get().getUpdatedAt());
                    reportsCatsDto.setRation(catsReporAfterRenewalOptional.get().getRation());
                    reportsCatsDto.setFeeling(catsReporAfterRenewalOptional.get().getFeeling());
                    reportsCatsDto.setChanges(catsReporAfterRenewalOptional.get().getChanges());

                    return reportsCatsDto;
                }
            } else {
                telegramBot.execute(new EditMessageText(chatId, messageId,
                        "У Вас еще нет домашнего питомца и Вы не можете отправлять отчет"));
                return null;
            }
        } else {
            telegramBot.execute(new EditMessageText(chatId, messageId,
                    "Вы еще не зарегистрированы как клиент приюта."));
            return null;
        }

        return null;
    }

}



