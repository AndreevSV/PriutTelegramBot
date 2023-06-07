package omg.group.priuttelegrambot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import omg.group.priuttelegrambot.entity.addresses.Addresses;

import java.time.LocalDateTime;

public class ClientDto {

    private Long id;
    @NotBlank(message = "Field Name couldn't be blank")
    @NotNull(message = "Field Name couldn't be Null")
    private String userName;
    private String name;
    private String surname;
    private String patronymic;
    @Pattern(regexp = "", message = "Date in format: dd:MM:yyyy") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private LocalDateTime birthday;
    @Pattern(regexp = "", message = "Date in format: dd:MM:yyyy") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private int telephone;
    @Email(message = "This is email field and you have to write correct email")
    private String email;
    private Addresses address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime dateIncome;
    private LocalDateTime dateOutcome;
    private Boolean becameClient;
    private Boolean volunteer;
    private Long catId; // !!!!!!!!!!!!!!!!!!
    private Boolean firstProbation;
    @Pattern(regexp = "") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private LocalDateTime probationStarts;
    @Pattern(regexp = "") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private LocalDateTime probationEnds;
    private Boolean passedProbation;


}
