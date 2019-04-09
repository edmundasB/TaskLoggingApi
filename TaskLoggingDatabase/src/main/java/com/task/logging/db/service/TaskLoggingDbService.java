package com.task.logging.db.service;

import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.LogEntity;
import com.task.logging.db.entities.TaskEntity;
import com.task.logging.db.repositories.TaskAssigneeRepository;
import com.task.logging.db.repositories.TaskGroupRepository;
import com.task.logging.db.repositories.TaskLogRepository;
import com.task.logging.db.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskLoggingDbService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskAssigneeRepository taskAssigneeRepository;
    @Autowired
    private TaskGroupRepository taskGroupRepository;
    @Autowired
    private TaskLogRepository taskLogRepository;

    public void persistTask(TaskEntity taskEntity) {
        taskAssigneeRepository.save(taskEntity.getAssignee());
        taskGroupRepository.save(taskEntity.getGroup());
        persisLogs(taskEntity);
        taskRepository.save(taskEntity);
    }

    private void persisLogs(TaskEntity taskEntity) {
        for(LogEntity log: taskEntity.getLogs()){
            log.setTask(taskEntity);
            taskLogRepository.save(log);
        }
    }

    public TaskEntity getTaskByName(String taskName) {
        TaskEntity entity = new TaskEntity();
        entity.setName(taskName);
        List<TaskEntity> taskEntity = taskRepository.findAll(getTaskNameExample(entity));
        return taskEntity.isEmpty() ? null : taskEntity.get(0);
    }

    private Example<TaskEntity> getTaskNameExample(TaskEntity entity) {
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnoreNullValues()
                .withIgnorePaths("isFinished", "id");
        return Example.of(entity, customExampleMatcher);
    }

    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }

    public AssigneeEntity getAssigneeByName(String name) {
        List<AssigneeEntity> allFound = taskAssigneeRepository.findAll(getAssigneeNameExample(name));
        return allFound.isEmpty() ? null : allFound.get(0);
    }

    private Example<AssigneeEntity> getAssigneeNameExample(String name) {
        AssigneeEntity assigneeExample = new AssigneeEntity(name);
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnoreNullValues()
                .withIgnorePaths("id");
        return Example.of(assigneeExample, customExampleMatcher);
    }

    public GroupEntity getGroupByName(String name) {
        List<GroupEntity> allFound = taskGroupRepository.findAll(getGroupNameExample(name));
        return allFound.isEmpty() ? null : allFound.get(0);
    }

    public List<TaskEntity> getAssigneeTasks(String assigneeName) {
        AssigneeEntity assignee = getAssigneeByName(assigneeName);
        if(assignee !=null && assignee.getTasks() != null){
            return assignee.getTasks();
        } else {
            return new ArrayList<TaskEntity>();
        }
    }

    private Example<GroupEntity> getGroupNameExample(String name) {
        GroupEntity groupExample = new GroupEntity(name);
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnoreNullValues()
                .withIgnorePaths("id");
        return Example.of(groupExample, customExampleMatcher);
    }

}
