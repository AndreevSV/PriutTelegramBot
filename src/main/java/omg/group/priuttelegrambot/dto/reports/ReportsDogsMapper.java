package omg.group.priuttelegrambot.dto.reports;


import omg.group.priuttelegrambot.dto.owners.OwnerDogMapper;
import omg.group.priuttelegrambot.dto.pets.DogsMapper;
import omg.group.priuttelegrambot.entity.reports.ReportDog;

public class ReportsDogsMapper {
    public static ReportsDogsDto toDto(ReportDog report) {
        ReportsDogsDto dto = new ReportsDogsDto();
        dto.setId(report.getId());
        dto.setOwnerDto(OwnerDogMapper.toDto(report.getOwner()));
        dto.setPetDto(DogsMapper.toDto(report.getPet()));
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

    public static ReportDog toEntity(ReportsDogsDto dto) {
        ReportDog report = new ReportDog();
        report.setId(dto.getId());
        report.setOwner(OwnerDogMapper.toEntity(dto.getOwnerDto()));
        report.setPet(DogsMapper.toEntity(dto.getPetDto()));
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
