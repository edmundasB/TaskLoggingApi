package com.task.logging.core.service.impl;

import com.task.logging.core.model.*;
import com.task.logging.core.service.TaskService;
import com.task.logging.db.entities.TaskEntity;
import org.springframework.stereotype.Service;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.PERSIST_TASK;
import static com.task.logging.db.routes.TaskLoggingDBRoutes.REMOVE_TASK;

@Service
public class TaskServiceImpl extends AbstractLogging implements TaskService {
    @Override
    public void create(Task request) {
        TaskEntity taskEntity =
                        new TaskEntity(request.getName(),
                        resolveTaskGroup(request.getGroupName()),
                        resolveTaskAssignee(request.getAssigneeName()));
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

    public void deleteTask(String name) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(name);
        producer.sendBody("direct:"+ REMOVE_TASK.getDirection(), taskEntity.getId());
    }

    @Override
    public Task findTask(String name) {
        TaskEntity entity;
        try {
            entity = resolveExistingTask(name);
        } catch (Exception e) {
            return null;
        }
        Integer timeLoged = entity.getLogs().stream().mapToInt( e -> e.getTime()).sum();
        return  new Task(entity.getId(),
                entity.getName(),
                entity.getGroup().getGroupName(),
                entity.getAssignee().getName(),
                timeLoged.toString(),
                entity.isFinished()
        );
    }

    @Override
    public void removeSubTask(SubTask subTask) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(subTask.getTaskName());
        TaskEntity subTaskEntity = resolveExistingTask(subTask.getSubTaskName());
        taskEntity.removeSubTask(subTaskEntity);
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

    @Override
    public void addSubTask(SubTask subTask) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(subTask.getTaskName());
        TaskEntity subTaskEntity = resolveExistingTask(subTask.getSubTaskName());
        taskEntity.addSubTask(subTaskEntity);
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

}
