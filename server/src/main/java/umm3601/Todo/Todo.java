package umm3601.Todo;

@SuppressWarnings({"VisibilityModifier"})
public class Todo {

  @SuppressWarnings({"MemberName"})
  public String _id;
  public String owner;
  public boolean status;
  public String body;
  public String category;

  @Override
  public String toString(){
    return category;
  }
}

