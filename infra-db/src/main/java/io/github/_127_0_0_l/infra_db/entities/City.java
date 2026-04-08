package io.github._127_0_0_l.infra_db.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cities")
@Getter
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "region_id", nullable = false)
    private Long regionId;

    @Setter
    @Column(unique = true, nullable = false)
    private String name;

    public City (String name){
        id = null;
        this.name = name;
    }
}
