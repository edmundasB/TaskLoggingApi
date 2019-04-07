package com.task.logging.core;

import com.task.logging.core.model.*;
import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.security.InvalidParameterException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggingServiceTest {
    @InjectMocks
    private TaskLoggingProcessor service;
    @Mock
    private ProducerTemplate producer;

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
    public void whenAddLogToTaskWhenSuccess(){

        mockGetTaskByName("10", "t name");

        service.addLog(new Log("t name", 1));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("t name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test(expected = InvalidParameterException.class)
    public void whenAddLogToTaskWhenTaskNotFound(){
        when(producer.requestBody(eq("direct:getTaskByName"), eq("10"), eq(TaskEntity.class))).thenReturn(null);

        service.addLog(new Log("10", 1));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("10"), eq(TaskEntity.class));
        verify(producer, times(0))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenChangeGroupThenGroupIsChanged(){
        when(producer.requestBody(eq("direct:getGroupByName"), anyString(), eq(GroupEntity.class))).thenReturn(null);

        mockGetTaskByName("10", "task name");

        service.changeGroup(new ChangeGroup("task name", "group new"));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("task name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .requestBody(eq("direct:getGroupByName"), anyString(), eq(GroupEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenRemoveSubTaskThenSubTaskRemoved(){
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
    public void whenAddSubTaskThenSubTaskAdded(){
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
    public void whenCheckIfSubTasksIsFinishedWhenAllFinished(){
        mockGetTaskByName("10", "name");

        boolean isFinished = service.isSubTasksFinished("name");

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        Assert.assertTrue(isFinished);
    }

    @Test
    public void whenSetTaskFinishedWhenFinishedSet(){
        mockGetTaskByName("10", "name");

        service.markFinished("name");

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenChangeAssigneeWhenAssigneeChanged(){
        mockGetTaskByName("10", "name");

        service.changeAssignee(new ChangeAssignee("name", "some user"));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .requestBody(eq("direct:getAssigneeByName"), anyString(), eq(AssigneeEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenDeleteTaskWhenTaskDeleted(){
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

    private void mockGetTaskByName(String taskId, String name) {
        TaskEntity taskEntity = new TaskEntity(name, new GroupEntity("name"), new AssigneeEntity("A name"));
        taskEntity.setId(Long.valueOf(taskId));

        TaskEntity subTask1 = new TaskEntity(name + "sub", new GroupEntity("name"), new AssigneeEntity("A name"));
        subTask1.setId(Long.valueOf(taskId));
        subTask1.setFinished(true);
        taskEntity.addSubTask(subTask1);

        when(producer.requestBody(eq("direct:getTaskByName"), eq(name), eq(TaskEntity.class))).thenReturn(taskEntity);
    }
}
