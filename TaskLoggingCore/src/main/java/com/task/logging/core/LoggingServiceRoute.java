package com.task.logging.core;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoggingServiceRoute extends RouteBuilder {
    @Autowired
    private TaskLoggingProcessor loggingService;

    @Override
    public void configure() throws Exception {
        from("direct:create")
                .bean(loggingService, "create");
        from("direct:addLog")
                .bean(loggingService, "addLog");
        from("direct:changeGroup")
                .bean(loggingService, "changeGroup");
        from("direct:removeSubTask")
                .bean(loggingService, "removeSubTask");
        from("direct:addSubTask")
                .bean(loggingService, "addSubTask");
        from("direct:isSubTasksFinished")
                .bean(loggingService, "isSubTasksFinished");
        from("direct:markFinished")
                .bean(loggingService, "markFinished");
        from("direct:changeAssignee")
                .bean(loggingService, "changeAssignee");
        from("direct:deleteTask")
                .bean(loggingService, "deleteTask");
        from("direct:findTask")
                .bean(loggingService, "findTask");

    }
}
