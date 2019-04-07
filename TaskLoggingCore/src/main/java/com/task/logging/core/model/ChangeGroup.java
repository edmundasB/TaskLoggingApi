package com.task.logging.core.model;

public class ChangeGroup {
    private String taskName;
    private String groupName;

    public ChangeGroup(String taskName, String groupName) {
        this.taskName = taskName;
        this.groupName = groupName;
    }

    public ChangeGroup() {
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getGroupName() {
        return groupName;
    }
}
