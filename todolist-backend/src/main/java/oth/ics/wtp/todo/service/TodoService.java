package oth.ics.wtp.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.todo.ClientErrors;
import oth.ics.wtp.todo.dtos.StatusDto;
import oth.ics.wtp.todo.dtos.TodoCreateDto;
import oth.ics.wtp.todo.dtos.TodoDto;
import oth.ics.wtp.todo.dtos.TodoUpdateDto;
import oth.ics.wtp.todo.entities.Status;
import oth.ics.wtp.todo.entities.Todo;
import oth.ics.wtp.todo.entities.TodoList;
import oth.ics.wtp.todo.repositories.TodoListRepository;
import oth.ics.wtp.todo.repositories.TodoRespository;

import java.util.Optional;

@Service
public class TodoService {
    private final TodoListRepository todoListRepository;
    private final TodoRespository todoRespository;

    @Autowired public TodoService(TodoListRepository todoListRepository, TodoRespository todoRespository) {
        this.todoListRepository = todoListRepository;
        this.todoRespository = todoRespository;
    }

    public TodoDto create(long todolistID, TodoCreateDto todoCreate) {
        TodoList todoList = todoListRepository.findById(todolistID).orElseThrow(()-> ClientErrors.todoListNotFound(todolistID));
        if(todoRespository.findByTask(todoCreate.task()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task with this name already exists");}
        Todo entity  = toEntity(todolistID,todoCreate);// schau mal wieder

        todoList.getTodo_tasks().add(entity);

        todoRespository.save(entity);
        todoListRepository.save(todoList);
        return toDto(entity);
    }

    private TodoDto toDto(Todo entity) {
        return new TodoDto(entity.getId(),entity.getCreatedAt(),entity.getTask(),StatusDto.valueOf(entity.getStatus().name())

        );
    }
    private TodoCreateDto toDto3(Todo entity) {
        return new TodoCreateDto(entity.getTask());
    }
//    todo1(entity.getStatus()
//    private StatusDto todo1(Status status) {
//   StatusDto statusDto = StatusDto.valueOf(status.name());
//
//        return statusDto;
//    }



    private Todo toEntity(long todolistID, TodoCreateDto todoCreate) {
        Todo newtodo =  new Todo(todoListRepository.findById(todolistID).orElseThrow(()->ClientErrors.todoListNotFound(todolistID)), todoCreate.task());
        return newtodo;
    }

    public TodoDto get(long todolistID, long todoID){
        if(!todoListRepository.existsById(todolistID)){
            throw ClientErrors.todoListNotFound(todolistID);
        }
        return todoRespository.findByTodoListIdAndId(todolistID,todoID).map(this::toDto)
                .orElseThrow(() -> ClientErrors.todoNotFound(todoID));
    }

    public TodoDto update(long todolistID, long todoID, TodoUpdateDto todoUpdate){
        if(!todoListRepository.existsById(todolistID)){
            throw ClientErrors.todoListNotFound(todolistID);
        }
        Todo todo = todoRespository.findByTodoListIdAndId(todolistID,todoID).orElseThrow(()-> ClientErrors.todoNotFound(todoID));
        todo.setTask(todoUpdate.task());
        todo.setStatus(Status.valueOf(todoUpdate.status().name()));
        return  toDto(todoRespository.save(todo));

    }

    public void deleteTodo(long todolistID, long todoID){
        if(!todoListRepository.existsById(todolistID)){
            throw ClientErrors.todoListNotFound(todolistID);
        }
        if(!todoRespository.existsById(todoID)) {
            throw ClientErrors.todoListNotFound(todoID);
        }
       Todo todo = todoRespository.findById(todoID).orElseThrow(()-> ClientErrors.todoListNotFound(todoID));
      TodoList todoList = todoListRepository.findById(todolistID).orElseThrow(()-> ClientErrors.todoListNotFound(todolistID));
        for(Todo atodo:todoList.getTodo_tasks() ){
           if(atodo.getId() == todoID){
               todoList.getTodo_tasks().remove(atodo);
           }
  }

       todoListRepository.save(todoList);
        todoRespository.deleteById(todoID);



    }

}
