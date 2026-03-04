package io.github._127_0_0_l.infra_db.repositories;

import io.github._127_0_0_l.infra_db.entities.Selector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectorRepository extends JpaRepository<Selector, Long> {
    
}
