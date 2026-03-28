package io.github._127_0_0_l.infra_db.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "new_records_count_log")
@Getter
@NoArgsConstructor
public class NewRecordsCountLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_source_id", referencedColumnName = "id", nullable = false)
    private ContentSource contentSource;

    @Setter
    @Column(name = "records_count", nullable = false)
    private int recordsCount;
}
