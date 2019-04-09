package com.task.logging.endpoint;

import com.task.logging.core.model.Task;
import com.task.logging.model.*;
import org.apache.camel.ProducerTemplate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatusLoggingEndpointTest {
    @LocalServerPort
    private String port;
    @InjectMocks
    private StatusLoggingEndpoint endpoint;
    @Mock
    private ProducerTemplate producer;

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void whenGetPingThenStatusSuccess() {
        ResponseEntity entity =
                restTemplate.getForEntity("http://localhost:"+port+"/v1/taskLog/ping", String.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(entity.getBody()).isEqualTo("TaskRequest logging application is running");
    }

    @Test
    public void whenSaveTaskThenSuccess(){
        HttpHeaders headers = getHttpHeaders();

        TaskRequest taskRequest = new TaskRequest();
        HttpEntity<TaskRequest> request = new HttpEntity<>(taskRequest, headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/create", HttpMethod.POST, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenAddLogWhenSuccess(){
        HttpHeaders headers = getHttpHeaders();
        createTaskForTest("name");
        LogRequest logRequest = new LogRequest("name", 20);
        HttpEntity<LogRequest> request = new HttpEntity<>(logRequest, headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/addLog", HttpMethod.PUT, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenAddSubTaskWhenSuccess(){
        HttpHeaders headers = getHttpHeaders();

        SubTaskRequest subtaskRequest = new SubTaskRequest();
        HttpEntity<SubTaskRequest> request = new HttpEntity<>(subtaskRequest, headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/addSubTask", HttpMethod.PUT, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenRemoveSubTaskWhenSuccess(){
        HttpHeaders headers = getHttpHeaders();
        createTaskForTest("name");
        createTaskForTest("subTask");

        SubTaskRequest subtaskRequest = new SubTaskRequest("name", "subTask");
        HttpEntity<SubTaskRequest> addSubtask = new HttpEntity<>(subtaskRequest, headers);
        restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/addSubTask", HttpMethod.PUT, addSubtask, Object.class);

        HttpEntity<SubTaskRequest> request = new HttpEntity<>(subtaskRequest, headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/removeSubTask", HttpMethod.DELETE, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenChangeGroupWhenSuccess(){
        HttpHeaders headers = getHttpHeaders();

        ChangeGroupRequest changeGroupRequest = new ChangeGroupRequest();
        HttpEntity<ChangeGroupRequest> request = new HttpEntity<>(changeGroupRequest, headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/changeGroup", HttpMethod.PUT, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenSetFinishedWhenSuccess(){
        HttpHeaders headers = getHttpHeaders();

        createTaskForTest("name");

        HttpEntity<ChangeGroupRequest> request = new HttpEntity<>(headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/setFinished/name", HttpMethod.PUT, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private void createTaskForTest(String name) {
        HttpHeaders headers = getHttpHeaders();
        TaskRequest taskRequest = new TaskRequest(name, "group name", "assignee");
        HttpEntity<TaskRequest> createNew = new HttpEntity<>(taskRequest, headers);
        restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/create", HttpMethod.POST, createNew, Object.class);
    }

    @Test
    public void whenChangeAssigneeWhenSuccess(){
        createTaskForTest("name");
        HttpHeaders headers = getHttpHeaders();

        TaskAssigneeRequest assigneeRequest = new TaskAssigneeRequest("name", "new assignee");
        HttpEntity<TaskAssigneeRequest> request = new HttpEntity<>(assigneeRequest,headers);

        ResponseEntity entity =
                restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/changeAssignee", HttpMethod.PUT, request, Object.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void whenGetTaskWhenSuccess(){
        createTaskForTest("name");

        ResponseEntity<Task> entity =
                restTemplate.getForEntity("http://localhost:"+port+"/v1/taskLog/name", Task.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
        Assert.assertTrue(entity.getBody() instanceof Task);
    }

    @Test
    public void whenGetAllAssigneeTasksWhenSuccess(){
        createTaskForTest("test0");
        createTaskForTest("test1");
        ResponseEntity<List<Task>> response = restTemplate.exchange("http://localhost:"+port+"/v1/taskLog/assignee/assignee",HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Task>>() {});

        Assert.assertEquals(4, response.getBody().size());
    }

    @Test
    public void whenGetAllTaskLoggedTimeWhenSuccess(){
        createTaskForTest("taskName");

        ResponseEntity<String> entity =
                restTemplate.getForEntity("http://localhost:"+port+"/v1/taskLog/loggedTime/taskName", String.class);
        assertThat(entity.getStatusCode().is2xxSuccessful()).isTrue();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }


}