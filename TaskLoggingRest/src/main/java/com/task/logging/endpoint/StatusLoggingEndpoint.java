package com.task.logging.endpoint;

import com.task.logging.core.model.*;
import com.task.logging.model.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.camel.ProducerTemplate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

import static com.task.logging.core.routing.TaskLoggingCoreRoutes.*;

@Component
@Api(value = "Logging api")
@Path("/v1/taskLog")
public class StatusLoggingEndpoint {

    @Autowired
    private ProducerTemplate producer;
    private ModelMapper mapper = new ModelMapper();

    @GET
    @Path("/ping")
    @Produces("application/json")
    @ApiOperation(value = "Verifies api running status")
    public String ping() {
        return "TaskRequest logging application is running";
    }

    @GET
    @Path("{name}")
    @ApiOperation(value = "Get task by name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Task found",response = Task.class),
            @ApiResponse(code = 404, message = "Task not found")
    })
    public ResponseEntity<Task> getTaskByName(@PathParam("name") String name){
        Task task = producer.requestBody("direct:"+FIND_TASK.getDirection(), name, Task.class);
        if(task != null) {
            return ResponseEntity.ok().body(task);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/loggedTime/{taskName}")
    @ApiOperation(value = "Get total time logged to task")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Total logged time",response = Integer.class),
            @ApiResponse(code = 404, message = "Logged time not found")
    })
    public ResponseEntity<String> getLoggedTime(@PathParam("taskName") String taskName){
        if(isTaskExist(taskName)){
            Integer time = producer.requestBody("direct:" + GET_TOTAL_TIME.getDirection(), taskName, Integer.class);
            return ResponseEntity.ok().body(String.valueOf(time));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/assignee/{name}")
    @ApiOperation(value = "Get all assignee tasks")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tasks found",response = Task.class),
            @ApiResponse(code = 404, message = "Tasks not found")
    })
    public Response get(@PathParam("name") String name){
        List<Task> assigneeTasks =
                producer.requestBody("direct:"+GET_ASSIGNEE_TASKS.getDirection(), name, ArrayList.class);
        if(assigneeTasks.isEmpty()){
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
             GenericEntity<List<Task>> entity = new GenericEntity<List<Task>>(assigneeTasks) {};
            return Response.ok(entity).build();
        }
    }

    @POST
    @Path("/create")
    @Produces("application/json")
    @ApiOperation(value = "Creates new task")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Task successfully created"),
            @ApiResponse(code = 409, message = "Task already exists")
    })
    public ResponseEntity create(TaskRequest taskRequest){
         if(isTaskExist(taskRequest.getName())){
             return ResponseEntity.status(HttpStatus.CONFLICT).body("Task already exists");
         } else {
             Task task = mapper.map(taskRequest, Task.class);
             producer.sendBody("direct:"+CREATE_TASK.getDirection(), task);
             return ResponseEntity.status(HttpStatus.CREATED).body("Task successfully created");
         }
    }

    @PUT
    @Path("/addLog")
    @Produces("application/json")
    @ApiOperation(value = "Log time for existing task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists")
    })
    public ResponseEntity addLog(LogRequest request){
        if(isTaskExist(request.getTaskName())){
            Log log = mapper.map(request, Log.class);
            producer.sendBody("direct:"+ADD_LOG.getDirection(), log);
            return ResponseEntity.status(HttpStatus.OK).body("Resource updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
    }

    @PUT
    @Path("/changeGroup")
    @Produces("application/json")
    @ApiOperation(value = "Change task group")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists")
    })
    public ResponseEntity changeGroup(ChangeGroupRequest request){
        if(isTaskExist(request.getTaskName())) {
            ChangeGroup group = mapper.map(request, ChangeGroup.class);
            producer.sendBody("direct:"+CHANGE_GROUP.getDirection(), group);
            return ResponseEntity.status(HttpStatus.OK).body("Resource updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
    }


    @PUT
    @Path("/addSubTask")
    @Produces("application/json")
    @ApiOperation(value = "Add subtask to task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists")
    })
    public ResponseEntity addSubTask(SubTaskRequest request){
        if(!isTaskExist(request.getTaskName())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
        if(!isTaskExist(request.getSubTaskName())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sub task not exists");
        }

        SubTask subTask = mapper.map(request, SubTask.class);
        producer.sendBody("direct:"+ADD_SUB_TASK.getDirection(), subTask);
        return ResponseEntity.ok().body("Resource updated successfully");
    }

    @PUT
    @Path("/setFinished/{name}")
    @Produces("application/json")
    @ApiOperation(value = "Mark task finished if all subtasks are finished")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists"),
            @ApiResponse(code = 403, message = "Task have unfinished subTasks")
    })
    public ResponseEntity setTaskFinished(@PathParam("name") String name){
        if(isTaskExist(name)){
            boolean isSubTasksFinished =
                    producer.requestBody("direct:"+CHECK_IF_ALL_SUBTASK_FINISHED.getDirection(), name, Boolean.class);
            if(isSubTasksFinished){
                producer.sendBody("direct:"+MARK_TASK_FINISHED.getDirection(), name);
                return ResponseEntity.ok().body("Resource updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Task have unfinished subTasks");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
    }

    @PUT
    @Path("/changeAssignee")
    @Produces("application/json")
    @ApiOperation(value = "Change task assignee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists")
    })
    public ResponseEntity changeAssignee(TaskAssigneeRequest request){
        if(isTaskExist(request.getTaskName())) {
            ChangeAssignee changeAssignee = mapper.map(request, ChangeAssignee.class);
            producer.sendBody("direct:"+ CHANGE_ASSIGNEE.getDirection(), changeAssignee);
            return ResponseEntity.ok().body("Resource updated successfully");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
    }

    @DELETE
    @Path("{name}")
    @ApiOperation(value = "Delete task")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 404, message = "Task not exists")
    })
    public ResponseEntity delete(@PathParam("name") String name){
        if(isTaskExist(name)) {
            producer.sendBody("direct:"+ DELETE_TASK.getDirection(), name);
            return ResponseEntity.ok().body("Resource updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not exists");
        }
    }

    private boolean isTaskExist(String name) {
        Task task = producer.requestBody("direct:"+ FIND_TASK.getDirection(), name, Task.class);
        return task != null;
    }
}
