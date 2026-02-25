package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;

@Entity
public record TgChat (
        @Id
        long id,
        int lastMessageId
) {
}
