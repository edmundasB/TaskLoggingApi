package com.task.logging.core;

import com.task.logging.core.model.*;
import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.LogEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
public class TaskLoggingProcessor {
    @Autowired
    private ProducerTemplate producer;

    public void create(Task request) {
        TaskEntity taskEntity =
                        new TaskEntity(request.getName(),
                        resolveTaskGroup(request.getGroupName()),
                        resolveTaskAssignee(request.getAssigneeName()));
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void addLog(Log taskLog) {
        TaskEntity taskEntity = resolveExistingTask(taskLog.getTaskName());
        taskEntity.addLog(new LogEntity(taskLog.getTime()));

        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void changeGroup(ChangeGroup changeGroup) {
        TaskEntity taskEntity = resolveExistingTask(changeGroup.getTaskName());
        taskEntity.setGroup(resolveTaskGroup(changeGroup.getGroupName()));
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void removeSubTask(SubTask subTask) {
        TaskEntity taskEntity = resolveExistingTask(subTask.getTaskName());
        TaskEntity subTaskEntity = resolveExistingTask(subTask.getSubTaskName());
        taskEntity.removeSubTask(subTaskEntity);
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void addSubTask(SubTask subTask) {
        TaskEntity taskEntity = resolveExistingTask(subTask.getTaskName());
        TaskEntity subTaskEntity = resolveExistingTask(subTask.getSubTaskName());
        taskEntity.addSubTask(subTaskEntity);
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public boolean isSubTasksFinished(String taskName) {
        TaskEntity taskEntity = resolveExistingTask(taskName);
        return taskEntity.isSubTasksIsFinished();
    }

    public void markFinished(String taskName) {
        TaskEntity taskEntity = resolveExistingTask(taskName);
        taskEntity.setFinished(true);
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void changeAssignee(ChangeAssignee changeAssignee) {
        TaskEntity taskEntity = resolveExistingTask(changeAssignee.getTaskName());
        taskEntity.setAssignee(resolveTaskAssignee(changeAssignee.getAssigneeName()));
        producer.sendBody("direct:persistTask", taskEntity);
    }

    public void deleteTask(String name) {
        TaskEntity taskEntity = resolveExistingTask(name);
        producer.sendBody("direct:removeTask", taskEntity.getId());
    }

    public Task findTask(String name) {
        TaskEntity entity = resolveExistingTask(name);
        Task task = new Task(entity.getName(), entity.getGroup().getGroupName(),entity.getAssignee().getName());
        task.setId(entity.getId());
        task.setFinished(entity.isFinished());
        return task;
    }

    private TaskEntity resolveExistingTask(String taskName) {
        TaskEntity taskEntity =
                producer.requestBody("direct:getTaskByName", taskName, TaskEntity.class);
        if(taskEntity == null){
            throw new InvalidParameterException("Error while performing action task: " + taskName + " not exists");
        }
        return taskEntity;
    }

    private AssigneeEntity resolveTaskAssignee(String assigneeName) {
        AssigneeEntity assigneeEntity = producer.requestBody("direct:getAssigneeByName", assigneeName, AssigneeEntity.class);
        return assigneeEntity != null ? assigneeEntity : new AssigneeEntity(assigneeName);
    }

    private GroupEntity resolveTaskGroup(String groupName) {
        GroupEntity groupEntity = producer.requestBody("direct:getGroupByName", groupName, GroupEntity.class);
        return groupEntity != null ? groupEntity : new GroupEntity(groupName);
    }

}
