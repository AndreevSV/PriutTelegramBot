//package omg.group.priuttelegrambot.service.impl;
//
//import com.pengrad.telegrambot.model.Update;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import omg.group.priuttelegrambot.dto.reports.ReportsDto;
//import omg.group.priuttelegrambot.entity.report.CatsReport;
//import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Data
//public class ReportsCatsService {
//
//    @Autowired
//    private final ReportsCatsRepository reportsCatsRepository;
//
//    public void savePhoto(Update update) {
//        if (update.message().photo() != null) {
//
//            reportsCatsRepository.save(new CatsReport());
//        }
//
//    }
//
//    public void saveRation(String ration) {
//
//        ReportsDto reportsDto = new ReportsDto();
//
//
//
////        reportsCatsRepository.save(ration);
//
//    }
//
//    public void saveFeeling() {
//
//    }
//
//    public void saveChenges() {
//
//    }
//
//
//}
