package com.task.logging.endpoint;

import com.task.logging.core.model.*;
import com.task.logging.model.*;
import org.apache.camel.ProducerTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Component
@Path("/v1/taskLog")
public class StatusLoggingEndpoint {

    @Autowired
    private ProducerTemplate producer;
    private ModelMapper mapper = new ModelMapper();

    @GET
    @Path("/ping")
    @Produces("application/json")
    public String ping() {
        return "TaskRequest logging application is running";
    }

    @POST
    @Path("/save")
    @Produces("application/json")
    public ResponseEntity save(TaskRequest taskRequest){
        Task task = mapper.map(taskRequest, Task.class);
        producer.sendBody("direct:create", task);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PUT
    @Path("/addLog")
    @Produces("application/json")
    public ResponseEntity addLog(LogRequest request){
        Log log = mapper.map(request, Log.class);
        producer.sendBody("direct:addLog", log);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PUT
    @Path("/changeGroup")
    @Produces("application/json")
    public ResponseEntity changeGroup(ChangeGroupRequest request){
        ChangeGroup group = mapper.map(request, ChangeGroup.class);
        producer.sendBody("direct:changeGroup", group);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DELETE
    @Path("/removeSubTask")
    @Produces("application/json")
    public void removeSubTask(SubTaskRequest request){
        SubTask subTask = mapper.map(request, SubTask.class);
        producer.sendBody("direct:removeSubTask", subTask);
    }

    @PUT
    @Path("/addSubTask")
    @Produces("application/json")
    public ResponseEntity addSubTask(SubTaskRequest request){
        SubTask subTask = mapper.map(request, SubTask.class);
        producer.sendBody("direct:addSubTask", subTask);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PUT
    @Path("/setFinished/{name}")
    @Produces("application/json")
    public ResponseEntity setTaskFinished(@PathParam("name") String name){
        Task task = producer.requestBody("direct:findTask", name, Task.class);
        if(task != null){
            boolean isSubTasksFinished = producer.requestBody("direct:isSubTasksFinished", name, Boolean.class);
            if(isSubTasksFinished){
                producer.sendBody("direct:markFinished", name);
                return new ResponseEntity(HttpStatus.CREATED);
            } else {
                return new ResponseEntity(HttpStatus.NOT_MODIFIED);
            }
        } else {
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @PUT
    @Path("/changeAssignee")
    @Produces("application/json")
    public ResponseEntity changeAssignee(TaskAssigneeRequest request){
        Task task = producer.requestBody("direct:findTask", request.getTaskName(), Task.class);
        if(task != null) {
            ChangeAssignee changeAssignee = mapper.map(request, ChangeAssignee.class);
            producer.sendBody("direct:changeAssignee", changeAssignee);
            return new ResponseEntity(HttpStatus.CREATED);
        } else{
            return new ResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }

    @DELETE
    @Path("{name}")
    public void delete(@PathParam("name") String name){
        producer.sendBody("direct:deleteTask", name);
    }

    @GET
    @Path("{name}")
    public Task get(@PathParam("name") String name){
        return producer.requestBody("direct:findTask", name, Task.class);
    }
}
