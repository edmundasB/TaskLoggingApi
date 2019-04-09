package com.task.logging.core.service;

import com.task.logging.core.model.Log;

public interface TimeLoggingService {
    void addLog(Log taskLog) throws Exception;
    boolean isSubTasksFinished(String taskName) throws Exception;
    void markFinished(String taskName) throws Exception;
    int getTotalTime(String taskName) throws Exception;
}
