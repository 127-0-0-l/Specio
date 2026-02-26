package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tg_chats")
@Getter
@NoArgsConstructor
public class TgChat {
    @Id
    private Long id;

    @Setter
    @Column(nullable = false)
    private String state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filters_id", referencedColumnName = "id")
    @Setter
    private Filters filters;

    @Column(name = "last_message_id")
    @Setter
    private Long lastMessageId;

    public TgChat(long id){
        this.id = id;
    }
}
