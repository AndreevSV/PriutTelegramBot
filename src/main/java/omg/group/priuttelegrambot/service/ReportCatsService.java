package omg.group.priuttelegrambot.service;

import omg.group.priuttelegrambot.dto.animals.CatDto;
import omg.group.priuttelegrambot.dto.reports.ReportsCatsDto;
import omg.group.priuttelegrambot.entity.animals.Cat;
import omg.group.priuttelegrambot.entity.report.CatsReport;
import org.springframework.http.HttpStatus;

import java.util.List;

public interface ReportCatsService {

    HttpStatus add(ReportsCatsDto reportsCatsDto);

    HttpStatus updateById(Long id, ReportsCatsDto reportsCatsDto);

    List<ReportsCatsDto> getAll();

    default CatsReport constructCatsReportFromReportsCatsDto(ReportsCatsDto reportsCatsDto) {

        CatsReport catsReport = new CatsReport();
        catsReport.setAnimalId(reportsCatsDto.getAnimalId());
        catsReport.setChanges(reportsCatsDto.getChanges());
        catsReport.setFeeling(reportsCatsDto.getFeeling());
        catsReport.setRation(reportsCatsDto.getRation());
        catsReport.setClientId(reportsCatsDto.getClientId());
        catsReport.setCreatedAt(reportsCatsDto.getCreatedAt());
        catsReport.setUpdatedAt(reportsCatsDto.getUpdatedAt());
        catsReport.setPath(reportsCatsDto.getPath());
        return catsReport;
    }

    default ReportsCatsDto constructReportsCatsDtoFromCatsReport(CatsReport catsReport) {

        ReportsCatsDto reportsCatsDto = new ReportsCatsDto();

        reportsCatsDto.setId(catsReport.getId());



        return reportsCatsDto;
    }
}
