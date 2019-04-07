package com.task.logging.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="TASK_GROUP")
public class GroupEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String groupName;
    @OneToMany(mappedBy="logGroup")
    private List<TaskEntity> tasks;

    public GroupEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public GroupEntity(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
