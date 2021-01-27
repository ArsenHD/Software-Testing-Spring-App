package ru.itmo.softwaredesign.todo.e2e;

import com.codeborne.selenide.*;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = E2ETests.Initializer.class)
public class E2ETests {
    @BeforeEach
    void beforeEach() {
        Configuration.browser = Browsers.CHROME;
        open("http://localhost:8080/");
    }

    @AfterEach
    void afterEach() {
    }

    @Test
    public void testHomeworks() {
        ElementsCollection titles = $$(By.cssSelector(".list-title"));
        assertFalse(titles.isEmpty());
        // click on homeworks
        open("/to-do-lists/homeworks");
        ElementsCollection statuses = $$(By.cssSelector(".status"));
        assertFalse(statuses.isEmpty());
        // all tasks are in progress
        assertTrue(statuses.stream().allMatch(element -> element.text().equals("In progress")));
        // all homeworks
        ElementsCollection tasks = $$(By.cssSelector("li"));
        // complete databases homework
        tasks.stream()
                .filter(task -> task.text().contains("Databases"))
                .findFirst()
                .ifPresent(task -> task.$(By.cssSelector("button")).click());
        open("/to-do-lists/homeworks");
        tasks = $$(By.cssSelector("li"));
        assertFalse(tasks.isEmpty());
        System.out.println("list: " + tasks.size());
        tasks.stream()
                .filter(task -> task.text().contains("Databases"))
                .findFirst()
                .ifPresent(task -> {
                    System.out.println("text: " + task.text());
                    String status = task.$(By.cssSelector(".status")).text();
                    // databases homework is now complete
                    assertEquals("Complete", status);
                });
    }

    @Test
    public void testSports() {
        ElementsCollection titles = $$(By.cssSelector(".list-title"));
        assertFalse(titles.isEmpty());
        // click on sports
        open("/to-do-lists/sports");
        ElementsCollection statuses = $$(By.cssSelector(".status"));
        assertFalse(statuses.isEmpty());
        // all tasks are in progress
        assertTrue(statuses.stream().allMatch(element -> element.text().equals("In progress")));
        // all exercises
        ElementsCollection tasks = $$(By.cssSelector("li"));
        assertFalse(tasks.isEmpty());
        // complete 10 km run
        tasks.stream()
                .filter(task -> task.text().contains("Run 10 km"))
                .findFirst()
                .ifPresent(task -> task.$(By.cssSelector("button")).click());
        open("/to-do-lists/sports");
        tasks = $$(By.cssSelector("li"));
        tasks.stream()
                .filter(task -> task.text().contains("Run 10 km"))
                .findFirst()
                .ifPresent(task -> {
                    String status = task.$(By.cssSelector(".status")).text();
                    // 10 km run is now complete
                    assertEquals("Complete", status);
                });
    }

    @Test
    public void testAddTodoList() {
        $(By.cssSelector(".title-input")).setValue("New list");
        $(By.cssSelector(".description-input")).setValue("This is a new todo list");
        $(By.cssSelector(".add-list")).click();
        for (int i = 1; i <= 5; i++) {
            open("/to-do-lists/new-list");
            $(By.cssSelector(".task-input")).setValue("Task " + i);
            $(By.cssSelector(".add-task")).click();
        }
        // check that the new list appears on the home page
        $$(By.cssSelector(".list-title")).stream()
                .filter(title -> title.text().equals("New list"))
                .findFirst()
                .orElseThrow();
        open("/to-do-lists/new-list");
        // check that the new list contains 5 tasks
        $$(By.cssSelector("li")).shouldHaveSize(5);
    }

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
}
