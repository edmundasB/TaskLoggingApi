package com.task.logging.core.model;

public class SubTask {
    private String taskName;
    private String subTaskName;

    public SubTask() {
    }

    public SubTask(String taskName, String subTaskName) {
        this.taskName = taskName;
        this.subTaskName = subTaskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getSubTaskName() {
        return subTaskName;
    }
}
