package omg.group.priuttelegrambot.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.dto.reports.ReportsDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.report.CatsReport;
import omg.group.priuttelegrambot.entity.report.Report;
import omg.group.priuttelegrambot.repository.ReportsCatsRepository;
import omg.group.priuttelegrambot.service.ReportCatsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class ReportsCatsServiceImpl implements ReportCatsService {

//    @Autowired
    private final ReportsCatsRepository reportsCatsRepository;

    public void saveRation(String ration) {

        ReportsDto reportsDto = new ReportsDto();



//        reportsCatsRepository.save(ration);

    }


    @Override
    public HttpStatus add(ReportsCatsDto reportsCatsDto) {
        CatsReport catsReport = constructCatsReportFromReportsCatsDto(reportsCatsDto);
        catsReport.setCreatedAt(LocalDateTime.now());

        reportsCatsRepository.save(catsReport);
        return HttpStatus.CREATED;
    }

    @Override
    public HttpStatus updateById(Long id, ReportsCatsDto reportsCatsDto) {
        return null;
    }

    @Override
    public List<ReportsCatsDto> getAll() {
        List<CatsReport> reportsCatsList = reportsCatsRepository.findAll();
        return reportsCatsList.stream()
                .map(this::constructReportsCatsDtoFromCatsReport)
                .collect(Collectors.toList());
    }
}
