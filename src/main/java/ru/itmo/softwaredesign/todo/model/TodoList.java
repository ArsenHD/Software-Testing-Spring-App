package ru.itmo.softwaredesign.todo.model;

//import ru.itmo.softwaredesign.todo.extensions.toSlug;
import javax.persistence.*;
import java.util.Collections;
import java.util.List;

import static ru.itmo.softwaredesign.todo.extensions.Extensions.toSlug;

@Entity
@Table(name = "TodoLists")
public class TodoList {
    public TodoList(String title, String description) {
        this(title, description, Collections.emptyList());
    }

    public TodoList(String title, String description, List<TodoItem> items) {
        this.title = title;
        this.description = description;
        this.items = items;
        this.slug = toSlug(this.title);
    }

    public String title;
    public String description;
    @OneToMany
    public List<TodoItem> items;
    public String slug;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id = null;

    public TodoList() {

    }
}