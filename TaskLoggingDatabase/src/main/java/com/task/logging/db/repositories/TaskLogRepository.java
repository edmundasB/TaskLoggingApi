package com.task.logging.db.repositories;

import com.task.logging.db.entities.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLogRepository extends JpaRepository<LogEntity, Long> {
}
