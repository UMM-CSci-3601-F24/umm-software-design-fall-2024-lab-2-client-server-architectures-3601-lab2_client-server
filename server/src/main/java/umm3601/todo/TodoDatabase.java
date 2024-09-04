package umm3601.todo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TodoDatabase {

  private Todo[] allTodos;

  public TodoDatabase(String todoDataFile) throws IOException {

    InputStream resourceAsStream = getClass().getResourceAsStream(todoDataFile);
    if (resourceAsStream == null) {
      throw new IOException("Could not find " + todoDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourceAsStream);
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

    if (queryParamMap.containsKey("category")) {
      String ownerParam = queryParamMap.get("category").get(0);
      filteredTodos = filterTodosByCategory(filteredTodos, ownerParam);
    }

    if (queryParamMap.containsKey("status")) {
      String statusParam = queryParamMap.get("status").get(0);
      if (statusParam.equals("complete")) {
      filteredTodos = filterTodosByStatus(filteredTodos, true);
      } else {
      filteredTodos = filterTodosByStatus(filteredTodos, false);
      }
    }

    return filteredTodos;
  }

  public Todo[] filterTodosByOwner(Todo[] todos, String targetOwner) {
    return Arrays.stream(todos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Todo[] todos, Boolean targetStatus) {
    return Arrays.stream(todos).filter(x -> x.status == targetStatus).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByCategory(Todo[] todos, String targetCategory) {
    return Arrays.stream(todos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

}
