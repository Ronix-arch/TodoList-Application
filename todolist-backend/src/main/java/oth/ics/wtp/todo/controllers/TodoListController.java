package oth.ics.wtp.todo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import oth.ics.wtp.todo.dtos.TodoListCreateDto;
import oth.ics.wtp.todo.dtos.TodoListBriefDto;
import oth.ics.wtp.todo.dtos.TodoListDto;
import oth.ics.wtp.todo.dtos.TodoListUpdateDto;
import oth.ics.wtp.todo.service.TodoListService;

import java.util.List;

@RestController



public class TodoListController {
    private final TodoListService todoListService;

    @Autowired
    public TodoListController(TodoListService todoListService) {
        this.todoListService = todoListService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "lists",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoListDto createTodoList(/*HttpServletRequest request, */@RequestBody TodoListCreateDto createTodoList ){
        return todoListService.create(createTodoList);
    }
    @GetMapping(value = "lists",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TodoListBriefDto>  listTodoLists(/*HttpServletRequest request, */) {
        return todoListService.todolistsBrief();
    }

    @GetMapping(value = "lists/{todolistID}",produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoListDto getTodoList(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID){
        return todoListService.getodolist(todolistID);
    }

    @PutMapping(value = "lists/{todolistID}",consumes =MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public TodoListDto updateTodoList(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID,@RequestBody TodoListUpdateDto todoListUpdate){
        return todoListService.update(todolistID, todoListUpdate);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "lists/{todolistID}")
    public void deleteTodoList(/*HttpServletRequest request, */@PathVariable("todolistID") long todolistID){
        todoListService.delete(todolistID);
    }

}
