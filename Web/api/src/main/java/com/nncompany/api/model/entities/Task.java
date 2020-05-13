package com.nncompany.api.model.entities;

import com.nncompany.api.model.enums.TaskStatus;
import com.nncompany.api.model.enums.TaskType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Date deadline;

    @ManyToOne()
    @JoinColumn(name = "creator")
    private User creator;

    @ManyToOne()
    @JoinColumn(name = "executor")
    private User executor;





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }
}
