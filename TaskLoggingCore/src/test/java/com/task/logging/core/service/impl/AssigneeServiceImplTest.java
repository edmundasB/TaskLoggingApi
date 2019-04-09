package com.task.logging.core.service.impl;

import com.task.logging.core.model.ChangeAssignee;
import com.task.logging.core.model.Task;
import com.task.logging.core.service.AssigneeService;
import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssigneeServiceImplTest extends AbstractLoggingTest {
    @InjectMocks
    private AssigneeServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<TaskEntity> foundEntities = new ArrayList<>();
        foundEntities.add(new TaskEntity("test1", new GroupEntity("name"), new AssigneeEntity("A name")));
        foundEntities.add(new TaskEntity("test2", new GroupEntity("name2"), new AssigneeEntity("A name")));

        when(producer.requestBody(eq("direct:getAssigneeTasks"),
                eq("A name"),
                eq(ArrayList.class))).thenReturn((ArrayList) foundEntities);
    }

    @Test
    public void whenChangeAssigneeWhenAssigneeChanged() throws Exception {
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
    public void whenFetchAssigneeTasksThenAllTasksFetched(){
        List<Task> allAssigneeTasks =
                service.fetchAssigneeTasks("A name");
        Assert.assertEquals(2, allAssigneeTasks.size());
    }

}