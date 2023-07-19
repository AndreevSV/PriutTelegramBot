package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String userName;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date birthday;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    @Email()
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "date_income")
    private LocalDateTime dateIncome;

    @Column(name = "date_outcome")
    private LocalDateTime dateOutcome;

    @Column(name = "became_client")
    private Boolean becameClient;

    @Column(name = "volunteer")
    private Boolean isVolunteer;

    @Column(name = "chat_id")
    private Long chatId;

//    @Column(name = "volunteer_id")
//    private Long volunteerId;

    @Column(name = "chats_opened")
    private Integer chatsOpened;


}
