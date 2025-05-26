package oth.ics.wtp.todo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.FetchType.EAGER;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
public class TodoList {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(fetch = EAGER)

    private List<Todo> todo_tasks;

    public TodoList() {
    }

    public TodoList(Long id, String name) {
        this.id = id;
        this.name = name;
        todo_tasks = new ArrayList<>();
    }

    public TodoList(String name) {
        this.name = name;
        todo_tasks = new ArrayList<>();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy="todolist")
    public List<Todo> getTodo_tasks() {
        return todo_tasks;
    }

    public void setTodo_tasks(List<Todo> todo_tasks) {
        this.todo_tasks = todo_tasks;
    }
}
