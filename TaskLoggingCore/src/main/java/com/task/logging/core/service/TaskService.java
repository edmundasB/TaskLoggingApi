package com.task.logging.core.service;

import com.task.logging.core.model.SubTask;
import com.task.logging.core.model.Task;

public interface TaskService {
    void create(Task request);
    void deleteTask(String name) throws Exception;
    Task findTask(String name);
    void removeSubTask(SubTask subTask) throws Exception;
    void addSubTask(SubTask subTask) throws Exception;
}
