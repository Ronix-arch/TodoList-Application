package oth.ics.wtp.todo.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.todo.controllers.TodoController;
import oth.ics.wtp.todo.controllers.TodoListController;
import oth.ics.wtp.todo.dtos.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoControllerTest {
    @Autowired
    private TodoListController listController;
    @Autowired
    private TodoController controller;

    private TodoListDto list1;
    private TodoListDto list2;

    @BeforeEach
    public void setUp() {
        list1 = listController.createTodoList(new TodoListCreateDto("list1"));
        list2 = listController.createTodoList(new TodoListCreateDto("list2"));
    }

    @Test public void createList() {
        TodoDto todo11 = controller.createTodo(list1.id(), new TodoCreateDto("task11"));
        TodoDto todo12 = controller.createTodo(list1.id(), new TodoCreateDto("task12"));
        TodoDto todo21 = controller.createTodo(list2.id(), new TodoCreateDto("task21"));
        TodoListDto ldto1 = listController.getTodoList(list1.id());
        assertEquals(2, ldto1.todos().size());
        assertEquals("task11", ldto1.todos().getFirst().task());
        assertEquals(todo11.id(), ldto1.todos().getFirst().id());
        assertEquals(StatusDto.PENDING, ldto1.todos().getFirst().status());
        assertTrue(Instant.now().isAfter(ldto1.todos().getFirst().createdAt()));
        assertEquals("task12", ldto1.todos().get(1).task());
        assertEquals(todo12.id(), ldto1.todos().get(1).id());
        assertEquals(StatusDto.PENDING, ldto1.todos().get(1).status());
        assertTrue(Instant.now().isAfter(ldto1.todos().get(1).createdAt()));
        TodoListDto ldto2 = listController.getTodoList(list2.id());
        assertEquals(1, ldto2.todos().size());
        assertEquals("task21", ldto2.todos().getFirst().task());
        assertEquals(todo21.id(), ldto2.todos().getFirst().id());
        assertEquals(StatusDto.PENDING, ldto2.todos().getFirst().status());
        assertTrue(Instant.now().isAfter(ldto2.todos().getFirst().createdAt()));
    }

    @Test public void createGet() {
        long todo11id = controller.createTodo(list1.id(), new TodoCreateDto("task11")).id();
        long todo12id = controller.createTodo(list1.id(), new TodoCreateDto("task12")).id();
        long todo21id = controller.createTodo(list2.id(), new TodoCreateDto("task21")).id();
        TodoDto todo11 = controller.getTodo(list1.id(), todo11id);
        TodoDto todo12 = controller.getTodo(list1.id(), todo12id);
        TodoDto todo21 = controller.getTodo(list2.id(), todo21id);
        assertThrows(ResponseStatusException.class, () -> controller.getTodo(list2.id(), todo11id));
        assertEquals("task11", todo11.task());
        assertEquals(todo11id, todo11.id());
        assertEquals(StatusDto.PENDING, todo11.status());
        assertTrue(Instant.now().isAfter(todo11.createdAt()));
        assertEquals("task12", todo12.task());
        assertEquals(todo12id, todo12.id());
        assertEquals(StatusDto.PENDING, todo12.status());
        assertTrue(Instant.now().isAfter(todo11.createdAt()));
        assertEquals("task21", todo21.task());
        assertEquals(todo21id, todo21.id());
        assertEquals(StatusDto.PENDING, todo21.status());
        assertTrue(Instant.now().isAfter(todo11.createdAt()));
    }

    @Test public void createUpdateGet() {
        long taskId = controller.createTodo(list1.id(), new TodoCreateDto("task")).id();
        long unrelatedId = controller.createTodo(list1.id(), new TodoCreateDto("unrelated")).id();
        TodoDto updated = controller.updateTodo(list1.id(), taskId, new TodoUpdateDto("tosk", StatusDto.DONE));
        assertEquals("tosk", updated.task());
        assertEquals(StatusDto.DONE, updated.status());
        TodoDto read = controller.getTodo(list1.id(), taskId);
        assertEquals(updated, read);
        TodoDto unrelated = controller.getTodo(list1.id(), unrelatedId);
        assertEquals("unrelated", unrelated.task());
        assertEquals(StatusDto.PENDING, unrelated.status());
    }

    @Test public void createDeleteGet() {
        long task1id = controller.createTodo(list1.id(), new TodoCreateDto("task1")).id();
        long task2id = controller.createTodo(list1.id(), new TodoCreateDto("task2")).id();
        controller.deleteTodo(list1.id(), task1id);
        assertThrows(ResponseStatusException.class, () -> controller.getTodo(list1.id(), task1id));
        TodoDto task2 = controller.getTodo(list1.id(), task2id);
        assertEquals("task2", task2.task());
        TodoListDto list = listController.getTodoList(list1.id());
        assertEquals(1, list.todos().size());
        assertEquals(task2id, list.todos().getFirst().id());
        assertThrows(ResponseStatusException.class, () -> controller.deleteTodo(list1.id(), task1id));
    }

    @Test public void createWithSameTask() {
        TodoDto todo = controller.createTodo(list1.id(), new TodoCreateDto("todo1"));
        assertThrows(ResponseStatusException.class, () -> controller.createTodo(list1.id(), new TodoCreateDto("todo1")));
        TodoListDto list = listController.getTodoList(list1.id());
        assertEquals(1, list.todos().size());
        assertEquals(todo.id(), list.todos().getFirst().id());
    }

    @Test public void accessInvalidId() {
        assertThrows(ResponseStatusException.class, () -> controller.getTodo(123, 456));
        assertThrows(ResponseStatusException.class, () -> controller.updateTodo(123, 456, new TodoUpdateDto("nope", StatusDto.PENDING)));
        assertThrows(ResponseStatusException.class, () -> controller.deleteTodo(123, 456));
    }
}
