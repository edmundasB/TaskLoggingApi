package com.task.logging.core.service;

import com.task.logging.core.model.ChangeAssignee;
import com.task.logging.core.model.Task;

import java.util.List;

public interface AssigneeService {
    void changeAssignee(ChangeAssignee changeAssignee) throws Exception;
    List<Task> fetchAssigneeTasks(String assigneeName);

}
