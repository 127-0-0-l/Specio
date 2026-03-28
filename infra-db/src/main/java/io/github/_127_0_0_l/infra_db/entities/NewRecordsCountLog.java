package io.github._127_0_0_l.infra_db.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
    @Column(name = "first_week_day", nullable = false)
    private LocalDate firstWeekDay;

    @Setter
    @Column(name = "week_seconds_from", nullable = false)
    @Min(value = 0, message = "value cannot be negative")
    private int weekSecondsFrom;

    @Setter
    @Column(name = "week_seconds_to", nullable = false)
    @Min(value = 0, message = "value cannot be negative")
    private int weekSecondsTo;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_source_id", referencedColumnName = "id", nullable = false)
    private ContentSource contentSource;

    @Setter
    @Column(name = "records_count", nullable = false)
    private int recordsCount;
}
