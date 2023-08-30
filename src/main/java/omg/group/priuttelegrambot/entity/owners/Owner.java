package omg.group.priuttelegrambot.entity.owners;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
    private LocalDate birthday;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "email")
    private String email;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "date_income")
    private LocalDate dateIncome;
    @Column(name = "date_outcome")
    private LocalDate dateOutcome;
    @Column(name = "became_client")
    private Boolean becameClient;
    @Column(name = "volunteer")
    private Boolean isVolunteer;
    @Column(name = "chat_id")
    private Long chatId;
    @Column(name = "telegram_user_id")
    private Long telegramUserId;
    @Column(name = "volunteer_chat_opened")
    private Boolean volunteerChatOpened;

}
