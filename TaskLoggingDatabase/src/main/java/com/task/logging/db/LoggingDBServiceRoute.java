package com.task.logging.db;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoggingDBServiceRoute extends RouteBuilder {
    @Autowired
    private TaskLoggingDbService dbService;

    @Override
    public void configure() throws Exception {
        from("direct:persistTask")
                .bean(dbService, "persistTask");
        from("direct:getTaskByName")
                .bean(dbService, "getTaskByName");
        from("direct:removeTask")
                .bean(dbService, "deleteTask");
        from("direct:getAssigneeByName")
                .bean(dbService, "getAssigneeByName");
        from("direct:getGroupByName")
                .bean(dbService, "getGroupByName");
    }
}
