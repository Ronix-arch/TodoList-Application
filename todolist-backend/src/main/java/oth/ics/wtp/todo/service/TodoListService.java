package oth.ics.wtp.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.todo.ClientErrors;
import oth.ics.wtp.todo.dtos.*;
import oth.ics.wtp.todo.entities.TodoList;
import oth.ics.wtp.todo.repositories.TodoListRepository;
import oth.ics.wtp.todo.repositories.TodoRespository;

import java.util.List;
import java.util.Optional;

@Service
public class TodoListService {
    private final TodoListRepository todoListRepository;
    private final TodoRespository todoRespository;

    @Autowired
    public TodoListService(TodoListRepository todoListRepository,TodoRespository todoRespository) {
        this.todoListRepository = todoListRepository;
        this.todoRespository = todoRespository;
    }

    public TodoListDto create(TodoListCreateDto createTodoList) {
        if(todoListRepository.findByName(createTodoList.name()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "TodoList with this name already exists");
        }
        TodoList entity = toEntity(createTodoList);
        todoListRepository.save(entity);
        return toDto(entity);


    }

  private TodoListDto toDto(TodoList todoList) {
      return new TodoListDto(todoList.getId(),todoList.getName(),  todoList.getTodo_tasks().stream()
               .map(todo -> new TodoDto(todo.getId(),todo.getCreatedAt(),todo.getTask(),StatusDto.valueOf(todo.getStatus().name()))).toList());
   }
private TodoListCreateDto toDto3(TodoList todoList) {
    return new TodoListCreateDto(todoList.getName());
}

    private TodoList toEntity(TodoListCreateDto createTodoList) {
        return new TodoList(createTodoList.name());
    }

    public List<TodoListBriefDto> todolistsBrief() {
      return todoListRepository.findAll().stream().map(this::toDto2).toList();
    }

    private TodoListBriefDto toDto2(TodoList todoList) {
        return new TodoListBriefDto(todoList.getId(),todoList.getName());
    }

    public TodoListDto getodolist(long todolistID) {
      if(!todoListRepository.existsById(todolistID)){
       throw ClientErrors.todoListNotFound(todolistID);}
        return todoListRepository.findById(todolistID).map(this::toDto).orElseThrow(() -> ClientErrors.todoNotFound(todolistID));

    }

    public TodoListDto update(long todolistID, TodoListUpdateDto todoListUpdate) {
        if (!todoListRepository.existsById(todolistID)) {
            throw ClientErrors.todoListNotFound(todolistID);}
            TodoList todoList = todoListRepository.findById(todolistID).orElseThrow(() -> ClientErrors.todoListNotFound(todolistID));
            todoList.setName(todoListUpdate.name());
            return toDto(todoListRepository.save(todoList));


    }


    public void delete(long todolistID) {
      if(!todoListRepository.existsById(todolistID)){
           throw ClientErrors.todoListNotFound(todolistID);}
            Optional<TodoList> todoList =todoListRepository.findById(todolistID);
            todoList.ifPresent(todoListRepository::delete);
    }



}
