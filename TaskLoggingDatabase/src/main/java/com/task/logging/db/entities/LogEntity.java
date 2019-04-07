package com.task.logging.db.entities;


import javax.persistence.*;
import java.io.Serializable;

@Entity(name="TASK_LOG")
public class LogEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private int time;
    @ManyToOne
    @JoinColumn(name="TASK_ID")
    private TaskEntity logTask;

    public LogEntity(int time) {
        this.time = time;
    }

    public LogEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public TaskEntity getTask() {
        return logTask;
    }

    public void setTask(TaskEntity task) {
        this.logTask = task;
    }
}
