package com.task.logging.db.routes;

public enum  TaskLoggingDBRoutes {
    PERSIST_TASK("persistTask", "persistTask"),
    GET_TASK_BY_NAME("getTaskByName", "getTaskByName"),
    REMOVE_TASK("removeTask", "deleteTask"),
    GET_ASSIGNEE_BY_NAME("getAssigneeByName", "getAssigneeByName"),
    GET_GROUP_BY_NAME("getGroupByName", "getGroupByName"),
    GET_ASSIGNEE_TASKS("getAssigneeTasks", "getAssigneeTasks");

    private String direction;
    private String method;

    TaskLoggingDBRoutes(String direction, String method) {
        this.direction = direction;
        this.method = method;
    }

    public String getDirection() {
        return direction;
    }

    public String getMethod() {
        return method;
    }
}
