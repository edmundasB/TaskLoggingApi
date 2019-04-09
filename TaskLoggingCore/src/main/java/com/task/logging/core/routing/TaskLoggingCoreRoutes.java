package com.task.logging.core.routing;

public enum TaskLoggingCoreRoutes {

    CREATE_TASK("create", "create"),
    DELETE_TASK("deleteTask", "deleteTask"),
    DELETE_SUB_TASK("removeSubTask", "removeSubTask"),
    ADD_SUB_TASK("addSubTask", "addSubTask"),
    FIND_TASK("findTask", "findTask"),
    ADD_LOG("addLog", "addLog"),
    CHANGE_GROUP("changeGroup", "changeGroup"),
    CHECK_IF_ALL_SUBTASK_FINISHED("isSubTasksFinished", "isSubTasksFinished"),
    MARK_TASK_FINISHED("markFinished", "markFinished"),
    CHANGE_ASSIGNEE("changeAssignee", "changeAssignee"),
    GET_TOTAL_TIME("getTotalTime", "getTotalTime"),
    GET_ASSIGNEE_TASKS("fetchAssigneeTasks", "fetchAssigneeTasks");

    private String direction;
    private String method;

    TaskLoggingCoreRoutes(String direction, String method) {
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
