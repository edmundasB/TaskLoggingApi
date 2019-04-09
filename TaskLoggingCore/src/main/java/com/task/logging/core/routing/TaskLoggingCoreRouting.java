package com.task.logging.core.routing;

import com.task.logging.core.service.impl.AssigneeServiceImpl;
import com.task.logging.core.service.impl.GroupServiceImpl;
import com.task.logging.core.service.impl.TaskServiceImpl;
import com.task.logging.core.service.impl.TimeLoggingServiceImpl;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.task.logging.core.routing.TaskLoggingCoreRoutes.*;

@Component
public class TaskLoggingCoreRouting extends RouteBuilder {
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private AssigneeServiceImpl assigneeService;
    @Autowired
    private GroupServiceImpl groupService;
    @Autowired
    private TimeLoggingServiceImpl timeLoggingService;

    @Override
    public void configure() throws Exception {
        from("direct:" + CREATE_TASK.getDirection()).bean(taskService, CREATE_TASK.getMethod());

        from("direct:"+ ADD_LOG.getDirection()).bean(timeLoggingService, ADD_LOG.getMethod());

        from("direct:" + CHANGE_GROUP.getDirection()).bean(groupService, CHANGE_GROUP.getMethod());

        from("direct:"+ DELETE_SUB_TASK.getDirection()).bean(taskService, DELETE_SUB_TASK.getMethod());

        from("direct:" +ADD_SUB_TASK.getDirection()).bean(taskService, ADD_SUB_TASK.getMethod());

        from("direct:" +CHECK_IF_ALL_SUBTASK_FINISHED.getDirection()).bean(timeLoggingService, CHECK_IF_ALL_SUBTASK_FINISHED.getMethod());

        from("direct:"+MARK_TASK_FINISHED.getDirection()).bean(timeLoggingService, MARK_TASK_FINISHED.getMethod());

        from("direct:"+CHANGE_ASSIGNEE.getDirection()).bean(assigneeService, CHANGE_ASSIGNEE.getMethod());

        from("direct:"+GET_ASSIGNEE_TASKS.getDirection()).bean(assigneeService, GET_ASSIGNEE_TASKS.getMethod());

        from("direct:"+DELETE_TASK.getDirection()).bean(taskService, DELETE_TASK.getMethod());

        from("direct:"+FIND_TASK.getDirection()).bean(taskService, FIND_TASK.getMethod());

        from("direct:"+GET_TOTAL_TIME.getDirection()).bean(timeLoggingService, GET_TOTAL_TIME.getMethod());

    }
}
