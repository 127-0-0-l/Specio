package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "last_records")
@Getter
@NoArgsConstructor
public class LastRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne
    @JoinColumn(name = "content_source_id", referencedColumnName = "id")
    private ContentSource contentSource;

    @Setter
    @Column(name = "record_identifier")
    private String recordIdentifier;
}
