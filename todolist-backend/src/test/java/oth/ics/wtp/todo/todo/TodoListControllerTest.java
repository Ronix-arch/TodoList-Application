package oth.ics.wtp.todo.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;
import oth.ics.wtp.todo.controllers.TodoListController;
import oth.ics.wtp.todo.dtos.TodoListBriefDto;
import oth.ics.wtp.todo.dtos.TodoListCreateDto;
import oth.ics.wtp.todo.dtos.TodoListDto;
import oth.ics.wtp.todo.dtos.TodoListUpdateDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TodoListControllerTest {
    @Autowired private TodoListController controller;

    @Test public void initiallyEmpty() {
        assertTrue(controller.listTodoLists().isEmpty());
    }

    @Test public void createGetList() {
        TodoListDto list1 = controller.createTodoList(new TodoListCreateDto("list1"));
        assertEquals("list1", list1.name());
        TodoListDto list2 = controller.createTodoList(new TodoListCreateDto("list2"));
        assertEquals("list2", list2.name());
        List<TodoListBriefDto> briefs = controller.listTodoLists();
        assertEquals(2, briefs.size());
        assertEquals(list1.id(), briefs.getFirst().id());
        assertEquals(list1.name(), briefs.getFirst().name());
        assertEquals(list2.id(), briefs.get(1).id());
        assertEquals(list2.name(), briefs.get(1).name());
    }

    @Test public void createUpdateGet() {
        TodoListDto initial = controller.createTodoList(new TodoListCreateDto("list"));
        TodoListDto updated = controller.updateTodoList(initial.id(), new TodoListUpdateDto("last"));
        assertEquals(initial.id(), updated.id());
        assertEquals("last", updated.name());
        TodoListDto read = controller.getTodoList(initial.id());
        assertEquals(initial.id(), read.id());
        assertEquals("last", read.name());
        List<TodoListBriefDto> briefs = controller.listTodoLists();
        assertEquals(1, briefs.size());
        assertEquals(initial.id(), briefs.getFirst().id());
        assertEquals("last", briefs.getFirst().name());
    }

    @Test public void createDeleteGet() {
        TodoListDto list1 = controller.createTodoList(new TodoListCreateDto("list1"));
        TodoListDto list2 = controller.createTodoList(new TodoListCreateDto("list2"));
        controller.deleteTodoList(list1.id());
        assertThrows(ResponseStatusException.class, () -> controller.getTodoList(list1.id()));
        assertEquals(list2.id(), controller.getTodoList(list2.id()).id());
        List<TodoListBriefDto> briefs = controller.listTodoLists();
        assertEquals(1, briefs.size());
        assertEquals(list2.id(), briefs.getFirst().id());
        assertEquals(list2.name(), briefs.getFirst().name());
        assertThrows(ResponseStatusException.class, () -> controller.getTodoList(list1.id()));
    }

    @Test public void createWithSameName() {
        TodoListDto list = controller.createTodoList(new TodoListCreateDto("list"));
        assertThrows(ResponseStatusException.class, () -> controller.createTodoList(new TodoListCreateDto("list")));
        List<TodoListBriefDto> briefs = controller.listTodoLists();
        assertEquals(1, briefs.size());
        assertEquals(list.id(), briefs.getFirst().id());
    }

    @Test public void accessInvalidId() {
        assertThrows(ResponseStatusException.class, () -> controller.getTodoList(123));
        assertThrows(ResponseStatusException.class, () -> controller.updateTodoList(123, new TodoListUpdateDto("nope")));
        assertThrows(ResponseStatusException.class, () -> controller.deleteTodoList(123));
    }
}
