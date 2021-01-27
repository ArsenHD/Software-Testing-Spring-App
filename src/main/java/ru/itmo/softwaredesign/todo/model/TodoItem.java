package ru.itmo.softwaredesign.todo.model;

import javax.persistence.*;

@Entity
@Table(name = "TodoItems")
public class TodoItem {
    public TodoItem(String task) {
        this.task = task;
    }

    public String task;
    public String status = "In progress";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id = null;

    public TodoItem() {

    }
}