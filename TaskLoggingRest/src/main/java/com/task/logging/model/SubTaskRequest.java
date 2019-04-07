package com.task.logging.model;

public class SubTaskRequest {
    private String taskName;
    private String subTaskName;

    public SubTaskRequest(String taskName, String subTaskName) {
        this.taskName = taskName;
        this.subTaskName = subTaskName;
    }

    public SubTaskRequest() {
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }
}
