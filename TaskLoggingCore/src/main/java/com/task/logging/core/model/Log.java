package com.task.logging.core.model;

public class Log {
    private String taskName;
    private int time;

    public Log(String taskName, int time) {
        this.taskName = taskName;
        this.time = time;
    }

    public Log() {
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTaskName() {
        return taskName;
    }

    public int getTime() {
        return time;
    }
}
