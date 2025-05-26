package oth.ics.wtp.todo.dtos;

import java.util.List;

public record TodoListDto(long id , String name , List<TodoDto> todos) {
}
