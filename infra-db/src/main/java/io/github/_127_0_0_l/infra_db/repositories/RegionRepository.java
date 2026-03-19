package io.github._127_0_0_l.infra_db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github._127_0_0_l.infra_db.entities.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{
    
}
