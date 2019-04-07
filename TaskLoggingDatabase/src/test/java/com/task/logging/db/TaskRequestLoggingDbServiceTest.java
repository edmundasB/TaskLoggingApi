package com.task.logging.db;

import com.task.logging.db.config.ApplicationConfiguration;
import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import com.task.logging.db.repositories.TaskRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class TaskRequestLoggingDbServiceTest {

    @Autowired
    private TaskLoggingDbService service;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void whenSaveTaskThenTaskGroupUserIsSaved(){
        TaskEntity entity = new TaskEntity("taskName", new GroupEntity("name"), new AssigneeEntity("A name"));

        service.persistTask(entity);

        Assert.assertNotNull(service.getTaskByName("taskName"));
    }

    @Test
    public void whenDeleteTaskThenTaskDeleted(){
        TaskEntity entity = new TaskEntity("task For Removal", new GroupEntity("name"), new AssigneeEntity("A name"));

        service.persistTask(entity);
        TaskEntity taskFromDatabase = service.getTaskByName("task For Removal");


        service.deleteTask(taskFromDatabase.getId());

        Assert.assertNull(service.getTaskByName("task For Removal"));
    }

    @Test
    public void whenGetAssigneeByNameThenAssigneeFound(){
        TaskEntity entity = new TaskEntity("task", new GroupEntity("name"), new AssigneeEntity("assignee name"));

        service.persistTask(entity);

        Assert.assertNotNull(service.getAssigneeByName("assignee name"));
    }

    @Test
    public void whenGetGroupByNameThenGroupFound(){
        TaskEntity entity = new TaskEntity("task", new GroupEntity("group name"), new AssigneeEntity("assignee name"));

        service.persistTask(entity);

        Assert.assertNotNull(service.getGroupByName("group name"));
    }
}
