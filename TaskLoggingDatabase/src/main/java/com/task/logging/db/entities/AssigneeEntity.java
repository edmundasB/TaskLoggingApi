package com.task.logging.db.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="TASK_ASSIGNEE")
public class AssigneeEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @OneToMany(mappedBy="assignee", fetch = FetchType.EAGER)
    private List<TaskEntity> tasks;

    public AssigneeEntity(String name) {
        this.name = name;
    }

    public AssigneeEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
