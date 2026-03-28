package io.github._127_0_0_l.infra_db.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github._127_0_0_l.infra_db.entities.NewRecordsCountLog;

@Repository
public interface NewRecordsCountLogRepository extends JpaRepository<NewRecordsCountLog, Long> {
    List<NewRecordsCountLog> findAllByContentSourceId(Long sourceId);
}
