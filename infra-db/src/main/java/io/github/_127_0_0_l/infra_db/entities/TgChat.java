package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;

@Entity
public class TgChat {
    @Id
    public int id;

    public long chatId;

    public int stateId;

    public int filtersId;

    public int lastMessageId;
}
