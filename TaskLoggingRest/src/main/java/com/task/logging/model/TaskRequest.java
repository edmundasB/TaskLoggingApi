package com.task.logging.model;

public class TaskRequest {
    private String name;
    private String groupName;
    private String assigneeName;

    public TaskRequest(String name, String groupName, String assigneeName) {
        this.name = name;
        this.groupName = groupName;
        this.assigneeName = assigneeName;
    }

    public TaskRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }
}
