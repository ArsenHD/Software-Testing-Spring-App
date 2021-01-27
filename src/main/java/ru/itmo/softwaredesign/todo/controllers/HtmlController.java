package ru.itmo.softwaredesign.todo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import ru.itmo.softwaredesign.todo.model.TodoItem;
import ru.itmo.softwaredesign.todo.model.TodoList;
import ru.itmo.softwaredesign.todo.properties.TodoListsProperties;
import ru.itmo.softwaredesign.todo.repositories.TodoItemRepository;
import ru.itmo.softwaredesign.todo.repositories.TodoListRepository;
import ru.itmo.softwaredesign.todo.service.RandomNameGenerator;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HtmlController {
    private TodoListRepository listsRepository;
    private TodoItemRepository itemsRepository;
    private TodoListsProperties properties;

    public HtmlController(
            TodoListRepository listsRepository,
            TodoItemRepository itemsRepository,
            TodoListsProperties properties
    ) {
        this.listsRepository = listsRepository;
        this.itemsRepository = itemsRepository;
        this.properties = properties;
    }

    @GetMapping("/")
    String todoLists(Model model) {
        model.asMap().put("title", properties.title);
        model.asMap().put(
                "todoLists",
                listsRepository.findAll()
                        .stream()
                        .map(this::render)
                        .collect(Collectors.toList())
        );
        return "home";
    }

    @GetMapping("/to-do-lists/{slug}")
    String todoList(@PathVariable String slug, Model model) {
        RenderedTodoList todoList = render(listsRepository.findBySlug(slug));
        if (todoList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This to-do list does not exist");
        }
        model.asMap().put("title", todoList.title);
        model.asMap().put("todoList", todoList);
        return "todoList";
    }

    @PostMapping("/add-list")
    String addList(
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        if (title.isEmpty()) {
            title = new RandomNameGenerator().name();
        }
        TodoList todoList = new TodoList(title, description);
        listsRepository.save(todoList);
        return "redirect:/";
    }

    @PostMapping("/delete-list")
    String deleteList(@RequestParam("slug") String slug) {
        TodoList todoList = listsRepository.findBySlug(slug);
        if (todoList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "To-do list not found");
        }
        listsRepository.delete(todoList);
        return "redirect:/";
    }

    @PostMapping("/to-do-lists/{slug}/add-item")
    String addItem(
            @PathVariable String slug,
            @RequestParam("task") String task
    ) {
        TodoItem todoItem = new TodoItem(task);
        itemsRepository.save(todoItem);
        TodoList todoList = listsRepository.findBySlug(slug);
        if (todoList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "To-do list not found");
        }
        todoList.items.add(todoItem);
        listsRepository.save(todoList);
        return "redirect:/";
    }

    @PostMapping("/mark-as-complete")
    String markAsComplete(@RequestParam("id") Long itemId) {
        TodoItem item = itemsRepository.findById(itemId).get();
        item.status = "Complete";
        itemsRepository.save(item);
        return "redirect:/";
    }

    RenderedTodoList render(TodoList todoList) {
        return new RenderedTodoList(
                todoList.slug,
                todoList.title,
                todoList.description,
                todoList.items
        );
    }

    static class RenderedTodoList {
        public RenderedTodoList(
                String slug,
                String title,
                String description,
                List<TodoItem> items
        ) {
            this.slug = slug;
            this.title = title;
            this.description = description;
            this.items = items;
        }

        String slug;
        String title;
        String description;
        List<TodoItem> items;
    }
}