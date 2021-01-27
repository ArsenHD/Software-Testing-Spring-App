package ru.itmo.softwaredesign.todo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("todo-lists")
public class TodoListsProperties {
    public TodoListsProperties(String title) {
        this.title = title;
    }

    public String title;
}