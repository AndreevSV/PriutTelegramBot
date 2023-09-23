package omg.group.priuttelegrambot.dto.reports;

import omg.group.priuttelegrambot.dto.owners.OwnerCatMapper;
import omg.group.priuttelegrambot.dto.pets.CatsMapper;
import omg.group.priuttelegrambot.entity.reports.ReportCat;

public class ReportsCatsMapper {

    public static ReportsCatsDto toDto(ReportCat report) {
        ReportsCatsDto dto = new ReportsCatsDto();
        dto.setId(report.getId());
        dto.setOwnerDto(OwnerCatMapper.toDto(report.getOwner()));
        dto.setPetDto(CatsMapper.toDto(report.getPet()));
        dto.setFileId(report.getFileId());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        dto.setRation(report.getRation());
        dto.setFeeling(report.getFeeling());
        dto.setChanges(report.getChanges());
        dto.setDateOfReport(report.getDateOfReport());
        dto.setDateOfLastReport(report.getDateOfLastReport());
        dto.setHashCodeOfPhoto(report.getHashCodeOfPhoto());
        return dto;
    }

    public static ReportCat toEntity(ReportsCatsDto dto) {
        ReportCat report = new ReportCat();
        report.setId(dto.getId());
        report.setOwner(OwnerCatMapper.toEntity(dto.getOwnerDto()));
        report.setPet(CatsMapper.toEntity(dto.getPetDto()));
        report.setFileId(dto.getFileId());
        report.setCreatedAt(dto.getCreatedAt());
        report.setUpdatedAt(dto.getUpdatedAt());
        report.setRation(dto.getRation());
        report.setFeeling(dto.getFeeling());
        report.setChanges(dto.getChanges());
        report.setDateOfReport(dto.getDateOfReport());
        report.setDateOfLastReport(dto.getDateOfLastReport());
        report.setHashCodeOfPhoto(dto.getHashCodeOfPhoto());
        return report;
    }


}
