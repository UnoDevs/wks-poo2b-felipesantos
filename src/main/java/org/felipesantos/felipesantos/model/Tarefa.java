package org.felipesantos.felipesantos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity()
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String title;

    private String description;
    @CreationTimestamp
    private LocalDateTime createDate;

    private LocalDate dueDate;
    private Status status;
    private Prioridade priority;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public Prioridade getPriority() {
        return priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPriority(Prioridade priority) {
        this.priority = priority;
    }
}
