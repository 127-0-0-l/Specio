package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tg_chats")
@Getter
@Setter
@NoArgsConstructor
public class TgChat {
    @Id
    private Long id;

    @Column(nullable = false)
    private String state;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "filters_id", referencedColumnName = "id")
    private Filters filters;

    @Column(name = "last_message_id")
    private Long lastMessageId;
}
