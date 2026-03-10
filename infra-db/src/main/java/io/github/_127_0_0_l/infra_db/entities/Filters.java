package io.github._127_0_0_l.infra_db.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

import io.github._127_0_0_l.core.constants.ValidationConstants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "filters")
@Getter
@NoArgsConstructor
public class Filters {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "filter_regions",
        joinColumns = @JoinColumn(name = "filter_id"),
        inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    private Set<Region> regions = new HashSet<>();

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "filter_cities",
        joinColumns = @JoinColumn(name = "filter_id"),
        inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private Set<City> cities = new HashSet<>();

    @Setter
    @Min(value = 0, message = "price cannot be negative")
    private Integer priceFrom;

    @Setter
    @Min(value = 0, message = "price cannot be negative")
    private Integer priceTo;

    @Min(value = ValidationConstants.MIN_YEAR_VALUE)
    private Integer yearFrom;

    @Min(value = ValidationConstants.MIN_YEAR_VALUE)
    private Integer yearTo;

    public void setYearFrom (Integer yearFrom){
        if (yearFrom == null){
            this.yearFrom = yearFrom;
            return;
        }

        Assert.isTrue(yearFrom <= LocalDate.now().getYear(), "year cannot be greater than current year");

        this.yearFrom = yearFrom;
    }

    public void setYearTo (Integer yearTo){
        if (yearTo == null){
            this.yearTo = yearTo;
            return;
        }

        Assert.isTrue(yearTo <= LocalDate.now().getYear(), "year cannot be greater than current year");

        this.yearTo = yearTo;
    }

    @PrePersist
    @PreUpdate
    private void validateConsistency() {
        if (priceFrom != null && priceTo != null) {
            Assert.isTrue(priceFrom <= priceTo, "invalid price range: from " + priceFrom + " to " + priceTo);
        }

        if (yearFrom != null && yearTo != null) {
            Assert.isTrue(yearFrom <= yearTo, "invalid year range: from " + yearFrom + " to " + yearTo);
        }
    }
}
