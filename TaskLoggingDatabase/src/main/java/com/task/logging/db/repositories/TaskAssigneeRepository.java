package com.task.logging.db.repositories;

import com.task.logging.db.entities.AssigneeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskAssigneeRepository extends JpaRepository<AssigneeEntity, Long> {
}
