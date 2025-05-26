package oth.ics.wtp.todo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.OnDelete;

import java.time.Instant;

import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class Todo {
    @Id
    @GeneratedValue
    private Long id;
    private String task;
    private Instant createdAt;
    private Status status;
    @ManyToOne      // i think this is a many to many
    @OnDelete(action = CASCADE)
    private TodoList todoList;

    public Todo() {
    }

    public Todo(TodoList todoList, String task) {
        this.todoList = todoList;
        this.task = task;
        this.createdAt = Instant.now();
        this.status = Status.PENDING;
    }

    public Todo(String task) {
        this.task = task;
        this.createdAt = Instant.now();
        this.status = Status.PENDING;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String name) {
        this.task = name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TodoList getTodoList() {
        return todoList;
    }

    public void setTodoList(TodoList todolist) {
        this.todoList = todolist;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", name='" + task + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", todolist=" + todoList +
                '}';
    }

}