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

// filter by owner
    if (queryParams.containsKey("owner")) {
      String ownerParam = queryParams.get("owner").get(0);
      try {
        String targetOwner = queryParams.get("owner").get(0);
        filteredTodos = filterTodosByOwner(targetOwner);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified owner '" + ownerParam + "' can't be parsed to an string");
      }


    }

// filter by body
    if (queryParams.containsKey("contains")) {
      String targetBody = queryParams.get("contains").get(0);
      filteredTodos = filterTodosByBody(targetBody);
    }

// filter by category

    if (queryParams.containsKey("category")) {
      String targetCategory = queryParams.get("category").get(0);
      filteredTodos = filterTodosByCategory(targetCategory);
    }

// filter by status

    if (queryParams.containsKey("status")) {
      Boolean targetStatus = queryParams.get("status").get(0).equals("complete");
      filteredTodos = filterTodosByStatus(targetStatus);
    }


    return filteredTodos;
  }



  public Todo[] filterTodosByOwner(String targetOwner) {
    return Arrays.stream(allTodos).filter(x -> x.owner.equals(targetOwner)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByStatus(Boolean targetStatus) {
    return Arrays.stream(allTodos).filter(x -> x.status == targetStatus).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByBody(String targetBody) {
    return Arrays.stream(allTodos).filter(x -> x.body.contains(targetBody)).toArray(Todo[]::new);
  }

  public Todo[] filterTodosByCategory(String targetCategory) {
    return Arrays.stream(allTodos).filter(x -> x.category.equals(targetCategory)).toArray(Todo[]::new);
  }

}

