package com.task.logging.core.model;

public class ChangeAssignee {
    private String taskName;
    private String assigneeName;

    public ChangeAssignee(String taskName, String assigneeName) {
        this.taskName = taskName;
        this.assigneeName = assigneeName;
    }

    public ChangeAssignee() {
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }
}
