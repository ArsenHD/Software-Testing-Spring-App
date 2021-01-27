package ru.itmo.softwaredesign.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.softwaredesign.todo.model.TodoItem;
import ru.itmo.softwaredesign.todo.model.TodoList;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    TodoList findBySlug(String slug);

    @Modifying
    @Transactional
    @Query("update TodoList set title = :title where id = :id")
    int updateTitleById(@Param("id") long id, @Param("title") String title);

    @Query("select title from TodoList where id = :id")
    String getTitleById(long id);

    @Query("select description from TodoList where title = :title")
    List<String> getDescriptionByTitle(@Param("title") String title);

    @Query("select items from TodoList where title = :title")
    List<TodoItem> getAllTasksByTitle(@Param("title") String title);
}
