package ru.itmo.softwaredesign.todo.integration;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.itmo.softwaredesign.todo.model.TodoItem;
import ru.itmo.softwaredesign.todo.model.TodoList;
import ru.itmo.softwaredesign.todo.repositories.TodoItemRepository;
import ru.itmo.softwaredesign.todo.repositories.TodoListRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = IntegrationTests.Initializer.class)
public class IntegrationTests {
    @ClassRule
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("circleci/postgres:9.4")
                    .withDatabaseName("test-db")
                    .withUsername("an")
                    .withPassword("an");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(applicationContext.getEnvironment());
        }
    }

    @Autowired
    private TodoItemRepository todoItemsRepository;

    @Autowired
    private TodoListRepository todoListsRepository;

    @Test
    public void testGetSoftwareTestingHomeworksListDescription() {
        withData(() -> {
            List<String> descriptions = todoListsRepository.getDescriptionByTitle("List software testing homeworks");
            assertEquals(1, descriptions.size());
            String description = descriptions.get(0);
            assertEquals("List of databases homeworks", description);
        });
    }

    @Test
    public void testGetDatabasesHomeworksListDescription() {
        withData(() -> {
            List<String> descriptions = todoListsRepository.getDescriptionByTitle("Databases Homeworks");
            assertEquals(1, descriptions.size());
            String description = descriptions.get(0);
            assertEquals("List of databases homeworks", description);
        });
    }

    @Test
    public void testCountAllInProgressTasks() {
        withData(() -> {
            List<Long> inProgressIds = todoItemsRepository.getIdsByStatus("In progress");
            assertEquals(11, inProgressIds.size());
        });
    }

    @Test
    public void testCountAllInProgressSoftwareTestingTasks() {
        withData(() -> {
            List<TodoItem> tasks = todoListsRepository.getAllTasksByTitle("Software Testing Homeworks");
            long count = tasks.stream().filter(task -> task.status.equals("In progress")).count();
            assertEquals(6, count);
        });
    }

    @Test
    public void testCountAllInProgressDatabasesTasks() {
        withData(() -> {
            List<TodoItem> tasks = todoListsRepository.getAllTasksByTitle("Databases Homeworks");
            long count = tasks.stream().filter(task -> task.status.equals("In progress")).count();
            assertEquals(5, count);
        });
    }

    private void withData(Runnable test) {
        insertData();
        test.run();
    }

    private void insertData() {
        List<TodoItem> stHomeworks = Arrays.asList(
                new TodoItem("HW1"),
                new TodoItem("HW2"),
                new TodoItem("HW3"),
                new TodoItem("HW4"),
                new TodoItem("HW5"),
                new TodoItem("HW6")
        );
        List<TodoItem> dbHomeworks = Arrays.asList(
                new TodoItem("HW1"),
                new TodoItem("HW2"),
                new TodoItem("HW3"),
                new TodoItem("HW4"),
                new TodoItem("HW5")
        );

        for (TodoItem homework : stHomeworks) {
            todoItemsRepository.save(homework);
        }
        for (TodoItem homework: dbHomeworks) {
            todoItemsRepository.save(homework);
        }

        List<TodoList> todoLists = Arrays.asList(
                new TodoList(
                        "Software Testing Homeworks",
                        "List of software testing homeworks",
                        stHomeworks
                ),
                new TodoList(
                        "Databases Homeworks",
                        "List of databases homeworks",
                        dbHomeworks
                )
        );
        for (TodoList list : todoLists) {
            todoListsRepository.save(list);
        }
    }
}
