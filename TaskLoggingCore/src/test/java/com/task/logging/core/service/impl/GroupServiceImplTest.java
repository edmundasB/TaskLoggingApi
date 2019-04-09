package com.task.logging.core.service.impl;

import com.task.logging.core.model.ChangeGroup;
import com.task.logging.core.service.GroupService;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import org.junit.Test;
import org.mockito.InjectMocks;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GroupServiceImplTest extends AbstractLoggingTest {
    @InjectMocks
    private GroupServiceImpl service;

    @Test
    public void whenChangeGroupThenGroupIsChanged() throws Exception {
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

}