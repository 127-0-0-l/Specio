package io.github._127_0_0_l.infra_db.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github._127_0_0_l.infra_db.entities.LastRecord;

@Repository
public interface LastRecordRepository extends JpaRepository<LastRecord, Long> {
    Optional<LastRecord> findByContentSourceId (Long contentSourceId);
}
