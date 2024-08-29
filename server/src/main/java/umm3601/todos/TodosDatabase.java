package umm3601.todos;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.http.BadRequestResponse;

/**
 * A fake "database" of user info
 * <p>
 * Since we don't want to complicate this lab with a real database, we're going
 * to instead just read a bunch of user data from a specified JSON file, and
 * then provide various database-like methods that allow the `UserController` to
 * "query" the "database".
 */
public class TodosDatabase {

  private Todos[] allTodos;

  public TodosDatabase(String todosDataFile) throws IOException {
    // The `.getResourceAsStream` method searches for the given resource in
    // the classpath, and returns `null` if it isn't found. We want to throw
    // an IOException if the data file isn't found, so we need to check for
    // `null` ourselves, and throw an IOException if necessary.
    InputStream resourceAsStream = getClass().getResourceAsStream(todosDataFile);
    if (resourceAsStream == null) {
      throw new IOException("Could not find " + todosDataFile);
    }
    InputStreamReader reader = new InputStreamReader(resourceAsStream);
    // A Jackson JSON mapper knows how to parse JSON into sensible 'User'
    // objects.
    ObjectMapper objectMapper = new ObjectMapper();
    // Read our user data file into an array of User objects.
    allTodos = objectMapper.readValue(reader, Todos[].class);
  }

  public int size() {
    return allTodos.length;
  }

  /**
   * Get the single user specified by the given ID. Return `null` if there is no
   * user with that ID.
   *
   * @param id the ID of the desired user
   * @return the user with the given ID, or null if there is no user with that ID
   */
  public Todos getTodos(String id) {
    return Arrays.stream(allTodos).filter(x -> x._id.equals(id)).findFirst().orElse(null);
  }

  /**
   * Get an array of all the users satisfying the queries in the params.
   *
   * @param queryParams map of key-value pairs for the query
   * @return an array of all the users matching the given criteria
   */
  public Todos[] listTodos(Map<String, List<String>> queryParams) {
    Todos[] filteredTodos = allTodos;

    // Filter age if defined
    if (queryParams.containsKey("age")) {
      String ageParam = queryParams.get("age").get(0);
      try {
        int targetAge = Integer.parseInt(ageParam);
        filteredTodos = filterTodosByAge(filteredTodos, targetAge);
      } catch (NumberFormatException e) {
        throw new BadRequestResponse("Specified age '" + ageParam + "' can't be parsed to an integer");
      }
    }
    // Filter company if defined
    if (queryParams.containsKey("company")) {
      String targetCompany = queryParams.get("company").get(0);
      filteredTodos = filterTodosByCompany(filteredTodos, targetCompany);
    }
    // Process other query parameters here...

    return filteredTodos;
  }

  /**
   * Get an array of all the users having the target age.
   *
   * @param users     the list of users to filter by age
   * @param targetAge the target age to look for
   * @return an array of all the users from the given list that have the target
   *         age
   */
  public Todos[] filterTodosByAge(Todos[] Todos, int targetAge) {
    return Arrays.stream(Todos).filter(x -> x.age == targetAge).toArray(Todos[]::new);
  }

  /**
   * Get an array of all the users having the target company.
   *
   * @param users         the list of users to filter by company
   * @param targetCompany the target company to look for
   * @return an array of all the users from the given list that have the target
   *         company
   */
  public Todos[] filterTodosByCompany(Todos[] Todos, String targetCompany) {
    return Arrays.stream(Todos).filter(x -> x.company.equals(targetCompany)).toArray(Todos[]::new);
  }

}
