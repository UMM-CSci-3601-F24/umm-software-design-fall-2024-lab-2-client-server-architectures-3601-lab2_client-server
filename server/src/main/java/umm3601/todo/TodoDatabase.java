package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.BadRequestResponse;

public class TodoDatabase {

  public Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException{

    InputStream resourseAsStream = getClass().getResourceAsStream(todoDataFile);
    if (resourseAsStream == null) {
      throw new IOException("Could not find " + todoDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourseAsStream);
    ObjectMapper objectMapper = new ObjectMapper();
    allTodos = objectMapper.readValue(reader, Todo[].class);
  }

  public int size() {
    return allTodos.length;
  }
  /**
   * @param id
   * @return
   */
  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  public Todo[] listTodos(Map<String, List<String>> queryParamMap) {
    Todo[] filteredTodos = allTodos;

    if (queryParamMap.containsKey("owner")) {
      String ownerParam = queryParamMap.get("owner").get(0);
      filteredTodos = filterTodosByOwner(filteredTodos, ownerParam);

    }

    if (queryParamMap.containsKey("status")) {
      String statusParam = queryParamMap.get("status").get(0);
      if (statusParam.equals("complete")) {
      filteredTodos = filterTodosByStatus(filteredTodos, true);
      }
      else {
      filteredTodos = filterTodosByStatus(filteredTodos, false);
      }

    }
    return filteredTodos;
  }

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'filterUsersByOwner'");
    return Arrays.stream(todos).filter(x -> x.owner == targetOwner).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, Boolean targetStatus) {

    return Arrays.stream(todos).filter(x -> x.status == targetStatus).toArray(Todo[]::new);
  }

}
