package com.task.logging.core.service.impl;

import com.task.logging.db.entities.AssigneeEntity;
import com.task.logging.db.entities.GroupEntity;
import com.task.logging.db.entities.TaskEntity;
import org.apache.camel.ProducerTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.GET_ASSIGNEE_BY_NAME;
import static com.task.logging.db.routes.TaskLoggingDBRoutes.GET_GROUP_BY_NAME;
import static com.task.logging.db.routes.TaskLoggingDBRoutes.GET_TASK_BY_NAME;

public abstract class AbstractLogging {
    @Autowired
    public ProducerTemplate producer;
    public ModelMapper mapper = new ModelMapper();


    public TaskEntity resolveExistingTask(String taskName) throws Exception {

        TaskEntity taskEntity =
                producer.requestBody("direct:"+GET_TASK_BY_NAME.getDirection(), taskName, TaskEntity.class);
        if(taskEntity == null){
            throw new Exception("Error while performing action task: " + taskName + " not exists");
        }
        return taskEntity;
    }

    public AssigneeEntity resolveTaskAssignee(String assigneeName) {
        AssigneeEntity assigneeEntity = producer.requestBody("direct:"+GET_ASSIGNEE_BY_NAME.getDirection(), assigneeName, AssigneeEntity.class);
        return assigneeEntity != null ? assigneeEntity : new AssigneeEntity(assigneeName);
    }

    public GroupEntity resolveTaskGroup(String groupName) {
        GroupEntity groupEntity = producer.requestBody("direct:"+ GET_GROUP_BY_NAME.getDirection(), groupName, GroupEntity.class);
        return groupEntity != null ? groupEntity : new GroupEntity(groupName);
    }

}
