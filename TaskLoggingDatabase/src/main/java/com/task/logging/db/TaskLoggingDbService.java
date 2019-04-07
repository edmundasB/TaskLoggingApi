package com.task.logging.db;

import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import com.task.logging.db.repositories.TaskAssigneeRepository;
import com.task.logging.db.repositories.TaskGroupRepository;
import com.task.logging.db.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskLoggingDbService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskAssigneeRepository taskAssigneeRepository;
    @Autowired
    private TaskGroupRepository taskGroupRepository;

    public void persistTask(TaskEntity taskEntity) {
        taskAssigneeRepository.save(taskEntity.getAssignee());
        taskGroupRepository.save(taskEntity.getGroup());
        taskRepository.save(taskEntity);
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

    private Example<GroupEntity> getGroupNameExample(String name) {
        GroupEntity groupExample = new GroupEntity(name);
        ExampleMatcher customExampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withIgnoreNullValues()
                .withIgnorePaths("id");
        return Example.of(groupExample, customExampleMatcher);
    }
}
