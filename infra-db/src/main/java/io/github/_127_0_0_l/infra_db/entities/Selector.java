package io.github._127_0_0_l.infra_db.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "selectors")
@Getter
@NoArgsConstructor
public class Selector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String selector;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Selector parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Selector> innerSelectors = new ArrayList<>();

    @Setter
    @Column(name = "is_multiple")
    private Boolean isMultiple = false;

    @Setter
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Setter
    @ManyToOne
    @JoinColumn(name = "selector_data_types_id", referencedColumnName = "id")
    private SelectorDataType contentType;

    public void addInnerSelector(Selector child){
        innerSelectors.add(child);
        child.setParent(this);
    }
}
