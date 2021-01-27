package ru.itmo.softwaredesign.todo.integration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itmo.softwaredesign.todo.repositories.TodoListRepository;
import ru.itmo.softwaredesign.todo.service.RandomNameGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RandomNameGeneratorTests {
    @Autowired
    TodoListRepository todoListRepository;

    @Test
    public void testUpdateTodoListNameWithRandomName() {
        RandomNameGenerator mock = Mockito.mock(RandomNameGenerator.class);
        Mockito.when(mock.name()).thenReturn("New list");

        String prevTitle = todoListRepository.getTitleById(1);
        assertEquals("Homeworks", prevTitle);
        todoListRepository.updateTitleById(1, mock.name());
        String newTitle = todoListRepository.getTitleById(1);
        assertEquals("New list", newTitle);
    }
}
