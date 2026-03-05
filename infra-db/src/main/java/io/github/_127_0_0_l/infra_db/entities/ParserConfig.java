package io.github._127_0_0_l.infra_db.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parser_configs")
@Getter
@NoArgsConstructor
public class ParserConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_source_id", referencedColumnName = "id")
    private ContentSource contentSource;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parser_config_id")
    private List<Selector> selectors = new ArrayList<>();
}
