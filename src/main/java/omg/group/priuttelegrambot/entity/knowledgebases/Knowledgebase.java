package omg.group.priuttelegrambot.entity.knowledgebases;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Knowledgebase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "command")
    private String command;
    @Column(name = "command_description")
    private String commandDescription;
    @Column(name = "message")
    private String message;
}
