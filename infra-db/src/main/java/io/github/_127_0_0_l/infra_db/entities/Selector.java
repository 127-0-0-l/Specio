package io.github._127_0_0_l.infra_db.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
public class Selector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String selector;

    @Setter
    private List<Selector> innerSelectors = new ArrayList<>();

    @Column(name = "is_multiple")
    private Boolean isMultiple;

    @Setter
    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Setter
    @Column(name = "content_type")
    private SelectorDataType contentType;

    public void setIsMultiple(Boolean isMultiple){
        if (isMultiple == null){
            this.isMultiple = false;
        } else {
            this.isMultiple = isMultiple;
        }
    }
}
