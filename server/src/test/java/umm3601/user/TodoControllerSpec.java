package umm3601.user;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.javalin.http.Context;
import umm3601.todo.TodoController;
import umm3601.Main;
import umm3601.todo.Todo;
import umm3601.todo.TodoDatabase;



public class TodoControllerSpec {

  private TodoController todoController;
  private static TodoDatabase db;

  @Mock
  private Context ctx;

  @Captor
  private ArgumentCaptor<Todo[]> todoArrayCaptor;


  @BeforeEach
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    db = new TodoDatabase(Main.TODO_DATA_FILE);
    todoController = new TodoController(db);
  }

  @Test
  public void canGetTodo() throws IOException {
    db.getTodo("58895985186754887e0381f5");
    verify(ctx).json(todoArrayCaptor.capture());
    assertEquals(db.size(), todoArrayCaptor.getValue().length);
  }


  @Test
  public void canGetTodos() throws IOException {
    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    assertEquals(db.size(), todoArrayCaptor.getValue().length);
  }



  @Test
  public void canFilterStatusTodos() throws IOException {
    Map<String, List<String>> queryParams = new HashMap<>();
    queryParams.put("status", Arrays.asList(new String[] {"complete"}));
    when(ctx.queryParamMap()).thenReturn(queryParams);

    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertEquals(true, todo.status);
    }
  }

  @Test
  public void canFilterStatusTodo() throws IOException {
    db.getTodo("58895985186754887e0381f5");
    verify(ctx).json(todoArrayCaptor.capture());
    for (Todo todo : todoArrayCaptor.getValue()) {
      assertEquals(true, todo.status);
    }
    assertEquals(1, todoArrayCaptor.getValue().length);
  }
}
