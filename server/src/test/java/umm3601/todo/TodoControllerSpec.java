package umm3601.todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import umm3601.Main;

public class TodoControllerSpec {
  private TodoController todoController;
  private static TodoDatabase db;
  private Context ctx;

  @Test
  public void canGetTodosByID() throws IOException {
    // A specific user ID known to be in the "database".
    String id = "588935f5c668650dc77df581";
    // Get the user associated with that ID.
    Todo todo = db.getTodos(id);

    when(ctx.pathParam("id")).thenReturn(id);

    todoController.getTodos(ctx);
    verify(ctx).json(todo);
    verify(ctx).status(HttpStatus.OK);
  }

}
