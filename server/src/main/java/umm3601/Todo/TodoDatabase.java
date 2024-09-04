package umm3601.Todo;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.BadRequestResponse;


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

  public Todo getTodo(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }
  public Todo[] listTodos(Map<String, List<String>> queryParams) {
Todo[] filteredTodos = allTodos;


    if (queryParams.containsKey("age")) {
      String ageParam = queryParams.get("age").get(0);
      try {
        String targetOwner = queryParams.get("owner").get(0);
        filteredTodos = filterTodosByOwner(targetOwner);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified age '" + ageParam + "' can't be parsed to an integer");
      }
    }

    // if (queryParams.containsKey("Body")) {
    //   String targetBody = queryParams.get("Body").get(0);
    //   filteredTodos = filterTodosByBody(filteredTodos, targetBody);
    // }


    return filteredTodos;
  }
  public Todo[] filterTodosByOwner(String targetOwner) {
    return Arrays.stream(allTodos).filter(x -> x.owner == targetOwner).toArray(Todo[]::new);
  }



}

