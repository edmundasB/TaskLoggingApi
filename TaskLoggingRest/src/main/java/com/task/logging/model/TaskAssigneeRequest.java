package com.task.logging.model;

public class TaskAssigneeRequest {
    private String taskName;
    private String assigneeName;

    public TaskAssigneeRequest(String taskName, String assigneeName) {
        this.taskName = taskName;
        this.assigneeName = assigneeName;
    }

    public TaskAssigneeRequest() {
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }
}
