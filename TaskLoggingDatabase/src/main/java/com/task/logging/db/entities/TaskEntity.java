package com.task.logging.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name="LOGGING_TASK")
public class TaskEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String name;
    @ManyToOne( fetch = FetchType.EAGER)
    private GroupEntity logGroup;
    @ManyToOne( fetch = FetchType.EAGER)
    private AssigneeEntity assignee;
    @OneToMany(mappedBy="logTask", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LogEntity> logs;
    @OneToMany(mappedBy="parent", fetch = FetchType.EAGER)
    private Set<TaskEntity> subTasks;
    @ManyToOne
    @JoinColumn(name="task_id")
    private TaskEntity parent;
    private boolean isFinished;

    public TaskEntity(String name, GroupEntity group, AssigneeEntity assignee) {
        this.name = name;
        this.logGroup = group;
        this.assignee = assignee;
    }

    public TaskEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addLog(LogEntity log) {
        if(logs == null){
            logs = new ArrayList<>();
        }
        logs.add(log);
    }

    public void removeSubTask(TaskEntity subTask){
        if(subTasks == null){
            subTasks = new HashSet<>();
        }
        subTasks.remove(subTask);
    }

    public void addSubTask(TaskEntity subTask){
        if(subTasks == null){
            subTasks = new HashSet<>();
        }
        subTasks.add(subTask);
    }

    public boolean isSubTasksIsFinished(){
        if(subTasks == null){
            subTasks = new HashSet<>();
        }
        return subTasks.stream().allMatch(task -> task.isFinished);
    }

    public void setAssignee(AssigneeEntity assignee) {
        this.assignee = assignee;
    }

    public String getName() {
        return name;
    }

    public GroupEntity getGroup() {
        return logGroup;
    }

    public AssigneeEntity getAssignee() {
        return assignee;
    }

    public List<LogEntity> getLogs() {
        if(logs == null){
            logs = new ArrayList<>();
        }
        return logs;
    }

    public Set<TaskEntity> getSubTasks() {
        return subTasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogs(List<LogEntity> logs) {
        this.logs = logs;
    }

    public void setSubTasks(Set<TaskEntity> subTasks) {
        this.subTasks = subTasks;
    }

    public void setGroup(GroupEntity group) {
        this.logGroup = group;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public TaskEntity getParent() {
        return parent;
    }

    public void setParent(TaskEntity parent) {
        this.parent = parent;
    }
}
