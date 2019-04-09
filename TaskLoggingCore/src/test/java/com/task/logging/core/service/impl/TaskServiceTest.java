package com.task.logging.core.service.impl;

import com.task.logging.core.model.*;
import com.task.logging.core.service.TaskService;
import com.task.logging.core.service.impl.AbstractLoggingTest;
import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskServiceTest extends AbstractLoggingTest {
    @InjectMocks
    private TaskServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCreateNewTaskThenNewTaskEntityCreated(){
        Task request = new Task("Test task name", "group1", "test user");
        service.create(request);
        verify(producer, times(1)).sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenCreateNewTaskThenGroupExistAndNewTaskEntityCreated(){
        Task request = new Task("Test task name", "existingGroup", "test user");
        service.create(request);
        verify(producer, times(1)).requestBody(eq("direct:getGroupByName"), anyString(), eq(GroupEntity.class));
        verify(producer, times(1)).sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenCreateNewTaskThenAssigneeExistAndNewTaskEntityCreated(){
        Task request = new Task("Test task name", "existingGroup", "test user");
        service.create(request);
        verify(producer, times(1))
                .requestBody(eq("direct:getAssigneeByName"), anyString(), eq(AssigneeEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenCreateNewTaskThenAssigneeAndGroupNotExistAndNewTaskEntityCreated(){
        when(producer.requestBody(eq("direct:getAssigneeByName"), anyString(), eq(AssigneeEntity.class))).thenReturn(null);
        when(producer.requestBody(eq("direct:getGroupByName"), anyString(), eq(GroupEntity.class))).thenReturn(null);

        Task request = new Task("Test task name", "existingGroup", "test user");
        service.create(request);

        verify(producer, times(1))
                .requestBody(eq("direct:getGroupByName"), anyString(), eq(GroupEntity.class));

        verify(producer, times(1))
                .requestBody(eq("direct:getAssigneeByName"), anyString(), eq(AssigneeEntity.class));

        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }


    @Test
    public void whenRemoveSubTaskThenSubTaskRemoved() throws Exception {
        mockGetTaskByName("10", "name");
        mockGetTaskByName("12", "subTaskName");

        service.removeSubTask(new SubTask("name", "subTaskName"));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("subTaskName"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenAddSubTaskThenSubTaskAdded() throws Exception {
        mockGetTaskByName("10", "name");
        mockGetTaskByName("12", "subTaskName");

        service.addSubTask(new SubTask("name", "subTaskName"));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("subTaskName"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }


    @Test
    public void whenDeleteTaskWhenTaskDeleted() throws Exception {
        mockGetTaskByName("10", "name");
        service.deleteTask("name");
        verify(producer, times(1))
                .sendBody(eq("direct:removeTask"), any(long.class));
    }

    @Test
    public void whenGetTaskWhenReturned(){
        mockGetTaskByName("10", "name");
        Task result = service.findTask("name");
        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        Assert.assertNotNull(result);
    }

}
