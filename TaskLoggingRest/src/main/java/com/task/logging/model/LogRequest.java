package com.task.logging.model;

public class LogRequest {
    private String taskName;
    private int time;

    public LogRequest() {
    }

    public LogRequest(String taskName, int time) {
        this.taskName = taskName;
        this.time = time;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
