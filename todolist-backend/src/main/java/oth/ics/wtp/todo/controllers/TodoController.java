package oth.ics.wtp.todo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.todo.dtos.TodoCreateDto;
import oth.ics.wtp.todo.dtos.TodoDto;
import oth.ics.wtp.todo.dtos.TodoUpdateDto;
import oth.ics.wtp.todo.service.TodoService;

@RestController


public class TodoController {
    private final TodoService todoService;

    @Autowired public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "{todolistID}/todos",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public TodoDto createTodo(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID, @RequestBody TodoCreateDto todoCreate){
        return todoService.create( todolistID,todoCreate);
    }

    @GetMapping(value = "{todolistID}/todos/{todoID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto getTodo(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID, @PathVariable("todoID") long todoID){
      return todoService.get(todolistID,todoID);

    }
    @PutMapping(value = "{todolistID}/todos/{todoID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoDto updateTodo(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID, @PathVariable("todoID") long todoID
                                , @RequestBody TodoUpdateDto todoUpdate){
        return todoService.update(todolistID,todoID,todoUpdate);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{todolistID}/todos/{todoID}")
    public void deleteTodo(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID, @PathVariable("todoID") long todoID){
        todoService.deleteTodo(todolistID,todoID);
    }

}
