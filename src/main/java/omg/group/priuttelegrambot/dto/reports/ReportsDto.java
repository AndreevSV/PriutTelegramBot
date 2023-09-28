package omg.group.priuttelegrambot.dto.reports;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public abstract class ReportsDto {
    private Long id;
    private String fileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String ration;
    private String feeling;
    private String changes;
    private LocalDate dateOfReport;
    private Integer hashCodeOfPhoto;
}
