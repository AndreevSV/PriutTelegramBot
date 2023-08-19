package omg.group.priuttelegrambot.dto.reports;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReportsDto {
    private Long id;
    private Long clientId;
    private Long animalId;
    private String fileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ration;
    private String feeling;
    private String changes;
    private LocalDate dateOfReport;
    private LocalDate dateOfLastReport;
    private int hashCodeOfPhoto;
}
