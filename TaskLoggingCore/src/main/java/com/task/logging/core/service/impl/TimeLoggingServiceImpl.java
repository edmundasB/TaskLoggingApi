package com.task.logging.core.service.impl;

import com.task.logging.core.model.Log;
import com.task.logging.core.service.TimeLoggingService;
import com.task.logging.db.entities.LogEntity;
import com.task.logging.db.entities.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.task.logging.db.routes.TaskLoggingDBRoutes.PERSIST_TASK;

@Service
public class TimeLoggingServiceImpl extends AbstractLogging implements TimeLoggingService {
    @Override
    public void addLog(Log taskLog) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(taskLog.getTaskName());
        taskEntity.addLog(new LogEntity(taskLog.getTime()));

        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

    @Override
    public boolean isSubTasksFinished(String taskName) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(taskName);
        return taskEntity.isSubTasksIsFinished();
    }

    @Override
    public void markFinished(String taskName) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(taskName);
        taskEntity.setFinished(true);
        producer.sendBody("direct:"+PERSIST_TASK.getDirection(), taskEntity);
    }

    @Override
    public int getTotalTime(String taskName) throws Exception {
        TaskEntity taskEntity = resolveExistingTask(taskName);

        List<Integer> totalLogs = taskEntity.getLogs().stream().map(t-> t.getTime()).collect(Collectors.toList());
        Set<TaskEntity> tasksToCheck = new HashSet<>();

        tasksToCheck.addAll(taskEntity.getSubTasks());
        while(!tasksToCheck.isEmpty()){
            for(TaskEntity entity : tasksToCheck){
                totalLogs.addAll(entity.getLogs().stream().map(t-> t.getTime()).collect(Collectors.toList()));
                tasksToCheck.remove(entity);
                if(entity.getSubTasks() != null && !entity.getSubTasks().isEmpty()) {
                    tasksToCheck.addAll(entity.getSubTasks());
                }
            }
        }
        return  calculateTotalTime(totalLogs);
    }

    private int calculateTotalTime(List<Integer> totalLogs) {
        int totaltimeSpent = 0;
        for(Integer time : totalLogs){
            totaltimeSpent+=time;
        }
        return totaltimeSpent;
    }
}
