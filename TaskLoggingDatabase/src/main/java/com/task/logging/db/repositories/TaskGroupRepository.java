package com.task.logging.db.repositories;

import com.task.logging.db.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskGroupRepository extends JpaRepository<GroupEntity, Long> {
}
