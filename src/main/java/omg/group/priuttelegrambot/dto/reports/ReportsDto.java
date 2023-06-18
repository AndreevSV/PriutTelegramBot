package omg.group.priuttelegrambot.dto.reports;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportsDto {

    private Long id;

    private Long clientId;

    private Long animalId;

    private String path;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String ration;

    private String feeling;

    private String changes;
}
