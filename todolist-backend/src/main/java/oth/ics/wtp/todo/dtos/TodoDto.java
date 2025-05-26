package oth.ics.wtp.todo.dtos;


import java.time.Instant;

public record TodoDto(long id, Instant createdAt, String task,  StatusDto status) {
}
