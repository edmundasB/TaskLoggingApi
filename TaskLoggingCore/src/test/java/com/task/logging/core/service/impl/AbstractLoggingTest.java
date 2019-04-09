package com.task.logging.core.service.impl;

import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.LogEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AbstractLoggingTest   {
    @Mock
    public ProducerTemplate producer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public void mockGetTaskByName(String taskId, String name) {
        TaskEntity taskEntity = new TaskEntity(name, new GroupEntity("name"), new AssigneeEntity("A name"));
        taskEntity.setId(Long.valueOf(taskId));
        taskEntity.addLog(new LogEntity(50));

        TaskEntity subTask1 = new TaskEntity(name + "sub", new GroupEntity("name"), new AssigneeEntity("A name"));
        subTask1.setId(Long.valueOf(taskId));
        subTask1.addLog(new LogEntity(10));
        subTask1.setFinished(true);
        taskEntity.addSubTask(subTask1);

        when(producer.requestBody(eq("direct:getTaskByName"), eq(name), eq(TaskEntity.class))).thenReturn(taskEntity);
    }
}
