package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;

@Entity
public class TgChatState {
    @Id
    public int id;

    public String state;
}
