package umm3601.Todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.javalin.Javalin;
// import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
// import io.javalin.http.NotFoundResponse;
import umm3601.Main;


@SuppressWarnings({ "MagicNumber" })
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
 public void canBuildController() throws IOException {
  TodoController controller = TodoController.buildTodoController(Main.TODO_DATA_FILE);
  Javalin mockServer = Mockito.mock(Javalin.class);
  controller.addRoutes(mockServer);
  verify(mockServer, Mockito.atLeast(2)).get(any(), any());
}

@Test
public void buildControllerFailsWithIllegalDbFile() {
  Assertions.assertThrows(IOException.class, () -> {
    TodoController.buildTodoController("this is not a legal file name");
  });
}


@Test
  public void canGetAllTodos() throws IOException {
    todoController.getTodos(ctx);
    verify(ctx).json(todoArrayCaptor.capture());
    assertEquals(db.size(), todoArrayCaptor.getValue().length);
  }

  @Test
  public void canGetCertain_IDTodo() throws IOException {
    String _id = "58895985f0a4bbea24084abf";
    Todo todo = db.getTodo(_id);

    when(ctx.pathParam("id")).thenReturn(_id);

    todoController.getTodo(ctx);

    verify(ctx).json(todo);
    verify(ctx).status(HttpStatus.OK);
}

@Test
public void canGetTodosWithOwner() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("owner", Arrays.asList(new String[] {"Blanche"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);

  todoController.getTodos(ctx);

  // Confirm that all the users passed to `json` work for OHMNET.
  verify(ctx).json(todoArrayCaptor.capture());
  for (Todo todo : todoArrayCaptor.getValue()) {
    assertEquals("Blanche", todo.owner);
  }
}




@Test
public void canGetTodoByIncomplete() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("status", Arrays.asList(new String[] {"incomplete"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);

  todoController.getTodos(ctx);

  verify(ctx).json(todoArrayCaptor.capture());
  for (Todo todo : todoArrayCaptor.getValue()) {
    assertEquals(false, todo.status);
  }
}

@Test
public void canGetTodoByComplete() throws IOException {
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
public void canGetTodosByBody() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("contains", Arrays.asList(new String[] {"tempor"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);
  todoController.getTodos(ctx);
  verify(ctx).json(todoArrayCaptor.capture());
  for (Todo todo : todoArrayCaptor.getValue()) {
    assertTrue(todo.body.contains("tempor"), "Body <" + todo.body + "> didn't contain 'tempor'.");
  }
}

@Test
public void ableToLimitByFive() throws IOException {
  Map<String, List<String>> queryParams = new HashMap<>();
  queryParams.put("limit", Arrays.asList(new String[] {"5"}));
  when(ctx.queryParamMap()).thenReturn(queryParams);
  todoController.getTodos(ctx);
  verify(ctx).json(todoArrayCaptor.capture());
  assertEquals(5, todoArrayCaptor.getValue().length);
}

}
