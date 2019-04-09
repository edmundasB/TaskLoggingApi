package com.task.logging.core.service.impl;

import com.task.logging.core.model.ChangeGroup;
import com.task.logging.core.service.GroupService;
import com.task.logging.db.entities.TaskEntity;
import org.springframework.stereotype.Service;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.PERSIST_TASK;

@Service
public class GroupServiceImpl extends AbstractLogging implements GroupService {
    @Override
    public void changeGroup(ChangeGroup changeGroup) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(changeGroup.getTaskName());
        taskEntity.setGroup(resolveTaskGroup(changeGroup.getGroupName()));
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }
}
