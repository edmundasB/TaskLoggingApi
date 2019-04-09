package com.task.logging.db.routes;

import com.task.logging.db.service.TaskLoggingDbService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.*;

@Component
public class TaskLoggingDBRouting extends RouteBuilder {
    @Autowired
    private TaskLoggingDbService dbService;

    @Override
    public void configure() throws Exception {
        from("direct:"+PERSIST_TASK.getDirection()).bean(dbService, PERSIST_TASK.getMethod());

        from("direct:"+ GET_TASK_BY_NAME.getDirection()).bean(dbService, GET_TASK_BY_NAME.getMethod());

        from("direct:"+ REMOVE_TASK.getDirection()).bean(dbService, REMOVE_TASK.getMethod());

        from("direct:"+ GET_ASSIGNEE_BY_NAME.getDirection()).bean(dbService, GET_ASSIGNEE_BY_NAME.getMethod());

        from("direct:"+GET_GROUP_BY_NAME.getDirection()).bean(dbService, GET_GROUP_BY_NAME.getMethod());

        from("direct:"+GET_ASSIGNEE_TASKS.getDirection()).bean(dbService, GET_ASSIGNEE_TASKS.getMethod());
    }
}
