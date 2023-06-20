package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.reports.ReportsDto;
import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class ReportsCatsService{

    @Autowired
    private final ReportsCatsRepository reportsCatsRepository;

    public void saveRation(String ration) {

        ReportsDto reportsDto = new ReportsDto();



//        reportsCatsRepository.save(ration);

    }


}
