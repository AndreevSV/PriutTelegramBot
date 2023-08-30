package omg.group.priuttelegrambot.dto.owners;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OwnerDto {
    private Long id;
    private String userName;
    private String name;
    private String surname;
    private String patronymic;
    @Past(message = "Birthday should be in the past")
    private LocalDate birthday;
    @Pattern(regexp = "\\+\\d{11}", message = "Phone number should be in format: +79991234567")
    private String telephone;
    @Email(message = "This is email field and you have to write correct email")
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate dateIncome;
    private LocalDate dateOutcome;
    private Boolean becameClient;
    private Boolean isVolunteer;
    private Long chatId;
    private Long volunteerId;
    private Long telegramUserId;
    private Boolean volunteerChatOpened;
}
