//package omg.group.priuttelegrambot.service.impl;
//
//
//import omg.group.priuttelegrambot.Exception.OwnerCatNotFoundException;
//import omg.group.priuttelegrambot.entity.report.Report;
//import omg.group.priuttelegrambot.repository.ReportRepository;
//import omg.group.priuttelegrambot.service.ReportService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
///**
// * Class of ReportDataService.
// *
// * @author OMGgroup
// * @version 1.0.0
// */
//@Service
//@Transactional
//public class ReportServiceImpl implements ReportService {
//
//
//    private final ReportRepository reportRepository;
//
//    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
//
//    public ReportServiceImpl(ReportRepository reportRepository) {
//        this.reportRepository = reportRepository;
//    }
//
//    /**
//     * Method to uploadReportData.
//     *
//     * @param personId
//     * @param pictureFile
//     * @param file
//     * @param ration
//     * @param health
//     * @param habits
//     * @param filePath
//     * @param dateSendMessage
//     * @param timeDate
//     * @param daysOfReports
//     * @throws IOException
//     * @see ReportService
//     */
//    public void uploadReportData(Long personId, byte[] pictureFile, File file, String ration, String health,
//                                 String habits, String filePath, Date dateSendMessage, Long timeDate, long daysOfReports) throws IOException {
//        logger.info("Was invoked method to uploadReport");
//
//        Report report = new Report();
//
//        report.setLastMessage(dateSendMessage);
//        report.setDays(daysOfReports);
//        report.setFilePath(filePath);
//        report.setFileSize(file.length());
//        report.setLastMessageMs(timeDate);
//        report.setChatId(personId);
//        report.setData(pictureFile);
//        report.setRation(ration);
//        report.setHealth(health);
//        report.setHabits(habits);
//        this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to uploadReport.
//     *
//     * @param personId
//     * @param pictureFile
//     * @param file
//     * @param caption
//     * @param filePath
//     * @param dateSendMessage
//     * @param timeDate
//     * @param daysOfReports
//     * @throws IOException
//     * @see ReportService
//     */
//    public void uploadReport(Long personId, byte[] pictureFile, File file,
//                             String caption, String filePath, Date dateSendMessage, Long timeDate, long daysOfReports) throws IOException {
//        logger.info("Was invoked method to uploadReport");
//
//        Report report = new Report();//findById(personId);
//        report.setLastMessage(dateSendMessage);
//        report.setDays(daysOfReports);
//        report.setFilePath(filePath);
//        report.setChatId(personId);
//        report.setFileSize(file.length());
//        report.setData(pictureFile);
//        report.setCaption(caption);
//        report.setLastMessageMs(timeDate);
//        this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to get a report by id.
//     *
//     * @param id
//     * @return {@link ReportRepository#findById(Object)}
//     * @throws OwnerCatNotFoundException
//     * @see ReportService
//     */
////    public Report findById(Long id) {
////        logger.info("Was invoked method to get a report by id={}", id);
////
////        return this.reportRepository.findById(id)
////                .orElseThrow(ReportNotFoundException::new);
////    }
//
//    /**
//     * Method to get a report by chatId.
//     *
//     * @param chatId
//     * @return {@link ReportRepository#findByChatId(Long)}
//     * @see ReportService
//     */
//    public Report findByChatId(Long chatId) {
//        logger.info("Was invoked method to get a report by chatId={}", chatId);
//
//        return this.reportRepository.findByChatId(chatId);
//    }
//
//    /**
//     * Method to findListByChatId a report by id.
//     *
//     * @param chatId
//     * @return {@link ReportRepository#findListByChatId(Long)}
//     * @see ReportService
//     */
//    public Collection<Report> findListByChatId(Long chatId) {
//        logger.info("Was invoked method to findListByChatId a report by id={}", chatId);
//
//        return this.reportRepository.findListByChatId(chatId);
//    }
//
//    /**
//     * Method to save a report.
//     *
//     * @param report
//     * @return {@link ReportRepository#findListByChatId(Long)}
//     * @see ReportService
//     */
//    public Report save(Report report) {
//        logger.info("Was invoked method to save a report");
//
//        return this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to remove a report by id.
//     *
//     * @param id
//     * @see ReportService
//     */
//    public void remove(Long id) {
//        logger.info("Was invoked method to remove a report by id={}", id);
//
//        this.reportRepository.deleteById(id);
//    }
//
//    /**
//     * Method to get all reports.
//     *
//     * @return {@link ReportRepository#findAll()}
//     * @see ReportService
//     */
//    public List<Report> getAll() {
//        logger.info("Was invoked method to get all reports");
//
//        return this.reportRepository.findAll();
//    }
//
//    /**
//     * Method to get all reports.
//     *
//     * @param pageNumber
//     * @param pageSize
//     * @return {@link ReportRepository#findAll()}
//     * @see ReportService
//     */
//    public List<Report> getAllReports(Integer pageNumber, Integer pageSize) {
//        logger.info("Was invoked method to get all reports");
//
//        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
//        return this.reportRepository.findAll(pageRequest).getContent();
//    }
//
//    /**
//     * Method to getExtensions.
//     *
//     * @param fileName
//     * @see ReportService
//     */
//    private String getExtensions(String fileName) {
//        logger.info("Was invoked method to getExtensions");
//
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
//}
