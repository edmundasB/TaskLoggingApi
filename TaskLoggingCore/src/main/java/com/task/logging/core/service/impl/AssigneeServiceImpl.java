package com.task.logging.core.service.impl;

import com.task.logging.core.model.ChangeAssignee;
import com.task.logging.core.model.Task;
import com.task.logging.core.service.AssigneeService;
import com.task.logging.db.entities.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.GET_ASSIGNEE_TASKS;
import static com.task.logging.db.routes.TaskLoggingDBRoutes.PERSIST_TASK;

@Service
public class AssigneeServiceImpl extends AbstractLogging implements AssigneeService {
    @Override
    public void changeAssignee(ChangeAssignee changeAssignee) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(changeAssignee.getTaskName());
        taskEntity.setAssignee(resolveTaskAssignee(changeAssignee.getAssigneeName()));
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

    @Override
    public List<Task> fetchAssigneeTasks(String assigneeName) {
        List<TaskEntity> assigneeTasksEntities =
                producer.requestBody("direct:"+GET_ASSIGNEE_TASKS.getDirection(), assigneeName, ArrayList.class);
        List<Task> assigneeTasks = new ArrayList<>();

        for(TaskEntity entity : assigneeTasksEntities){
            Integer timeLoged = entity.getLogs().stream().mapToInt( e->e.getTime()).sum();
            assigneeTasks.add(new Task(entity.getId(),
                    entity.getName(),
                    entity.getGroup().getGroupName(),
                    entity.getAssignee().getName(),
                    timeLoged.toString(),
                    entity.isFinished()
            ));
        }
        return  assigneeTasks;
    }
}
