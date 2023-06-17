package omg.group.priuttelegrambot.dto.owners;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class OwnerDto {

    private Long id;

    @NotBlank(message = "Field Name couldn't be blank")
    @NotNull(message = "Field Name couldn't be Null")
    private String userName;

    private String name;

    private String surname;

    private String patronymic;

    @Pattern(regexp = "", message = "Date in format: dd:MM:yyyy") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Date birthday;

    @Pattern(regexp = "", message = "Date in format: dd:MM:yyyy") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private String telephone;

    @Email(message = "This is email field and you have to write correct email")
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime dateIncome;

    private LocalDateTime dateOutcome;

    private Boolean becameClient;

    private Boolean isVolunteer;

    private Boolean firstProbation;

    @Pattern(regexp = "") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private LocalDateTime probationStarts;

    @Pattern(regexp = "") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private LocalDateTime probationEnds;

    private Boolean passedProbation;

}
