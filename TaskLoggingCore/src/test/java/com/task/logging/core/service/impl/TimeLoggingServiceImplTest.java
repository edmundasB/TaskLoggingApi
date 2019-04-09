package com.task.logging.core.service.impl;

import com.task.logging.core.model.Log;
import com.task.logging.db.entities.TaskEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TimeLoggingServiceImplTest extends AbstractLoggingTest {
    @InjectMocks
    private TimeLoggingServiceImpl service;

    @Test
    public void whenAddLogToTaskWhenSuccess() throws Exception {

        mockGetTaskByName("10", "t name");

        service.addLog(new Log("t name", 1));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("t name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test(expected = Exception.class)
    public void whenAddLogToTaskWhenTaskNotFound() throws Exception {
        when(producer.requestBody(eq("direct:getTaskByName"), eq("10"), eq(TaskEntity.class))).thenReturn(null);

        service.addLog(new Log("10", 1));

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("10"), eq(TaskEntity.class));
        verify(producer, times(0))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenCheckIfSubTasksIsFinishedWhenAllFinished() throws Exception {
        mockGetTaskByName("10", "name");

        boolean isFinished = service.isSubTasksFinished("name");

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        Assert.assertTrue(isFinished);
    }

    @Test
    public void whenSetTaskFinishedWhenFinishedSet() throws Exception {
        mockGetTaskByName("10", "name");

        service.markFinished("name");

        verify(producer, times(1))
                .requestBody(eq("direct:getTaskByName"), eq("name"), eq(TaskEntity.class));
        verify(producer, times(1))
                .sendBody(eq("direct:persistTask"), any(TaskEntity.class));
    }

    @Test
    public void whenGetTotalLoggedTimeWhenTotalReturned() throws Exception {
        mockGetTaskByName("10", "task name");
        int spentTime = service.getTotalTime("task name");
        Assert.assertEquals(60, spentTime);
    }

}