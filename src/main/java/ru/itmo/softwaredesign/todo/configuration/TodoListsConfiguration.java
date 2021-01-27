package ru.itmo.softwaredesign.todo.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.softwaredesign.todo.model.TodoItem;
import ru.itmo.softwaredesign.todo.model.TodoList;
import ru.itmo.softwaredesign.todo.repositories.TodoItemRepository;
import ru.itmo.softwaredesign.todo.repositories.TodoListRepository;

import java.util.Arrays;
import java.util.List;

@Configuration
public class TodoListsConfiguration {
    @Bean
    ApplicationRunner databaseInitializer(
            TodoListRepository todoListsRepository,
            TodoItemRepository todoItemsRepository
    ) {
        return args -> {
            List<TodoItem> homeworks = Arrays.asList(
                    new TodoItem("Software Design HW5"),
                    new TodoItem("Software Design HW6"),
                    new TodoItem("Databases HW8"),
                    new TodoItem("Machine Learning HW5"),
                    new TodoItem("Networks HW4")
            );
            List<TodoItem> sports = Arrays.asList(
                    new TodoItem("Run 10 km"),
                    new TodoItem("Lift weights"),
                    new TodoItem("50 pull-ups"),
                    new TodoItem("150 push-ups")
            );

            for (TodoItem homework : homeworks) {
                todoItemsRepository.save(homework);
            }
            for (TodoItem sport: sports) {
                todoItemsRepository.save(sport);
            }

            List<TodoList> todoLists = Arrays.asList(
                    new TodoList("Homeworks", "List of homeworks", homeworks),
                    new TodoList("Sports", "List of exercises", sports)
            );
            for (TodoList list : todoLists) {
                todoListsRepository.save(list);
            }
        };
    }
}