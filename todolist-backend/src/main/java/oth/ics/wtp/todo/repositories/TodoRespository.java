package oth.ics.wtp.todo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oth.ics.wtp.todo.entities.Todo;

import java.util.Optional;

@Repository
public interface TodoRespository extends JpaRepository<Todo,Long> {
    Optional<Todo> findByTodoListIdAndId(long todolistID, long todoID);

    Optional<Todo> findByTask(String task);
    // void deleteByTodoListIdAndId(long todolistID, long todoID);



}
