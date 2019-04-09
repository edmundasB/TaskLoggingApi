package com.task.logging.core.model;

public class Task {
    private long id;
    private String name;
    private String groupName;
    private String assigneeName;
    private String logedTime;
    private boolean isFinished;

    public Task() {
    }

    public Task(String name, String groupName, String assigneeName) {
        this.name = name;
        this.groupName = groupName;
        this.assigneeName = assigneeName;
    }

    public Task(long id, String name, String groupName, String assigneeName, String logedTime, boolean isFinished) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
        this.assigneeName = assigneeName;
        this.logedTime = logedTime;
        this.isFinished = isFinished;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getName() {
        return name;
    }

    public String getLogedTime() {
        return logedTime;
    }

    public void setLogedTime(String logedTime) {
        this.logedTime = logedTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }
}
