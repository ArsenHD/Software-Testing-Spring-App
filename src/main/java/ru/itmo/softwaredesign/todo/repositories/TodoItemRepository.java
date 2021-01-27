package ru.itmo.softwaredesign.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.softwaredesign.todo.model.TodoItem;

import java.util.List;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    @Query("select id from TodoItem where status = :status")
    List<Long> getIdsByStatus(@Param("status") String status);
}
