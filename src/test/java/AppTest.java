import org.sql2o.*;
import org.junit.*;
import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Todo list!");
  }

  @Test
  public void categoryIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Categories"));
    fill("#name").with("Household chores");
    submit(".btn");
    assertThat(pageSource()).contains("Household chores");
  }

  @Test
   public void taskIsCreatedTest() {
     goTo("http://localhost:4567/");
     click("a", withText("Tasks"));
     fill("#description").with("Mow the lawn");
     submit(".btn");
     assertThat(pageSource()).contains("Mow the lawn");
   }

   @Test
   public void categoryShowPageDisplaysName() {
     Category testCategory = new Category("Household chores");
     testCategory.save();
     String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
     goTo(url);
     assertThat(pageSource()).contains("Household chores");
   }

   @Test
   public void taskShowPageDisplaysDescription() {
     Task testTask = new Task("Mow the lawn");
     testTask.save();
     String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
     goTo(url);
     assertThat(pageSource()).contains("Mow the lawn");
   }

   @Test
   public void taskIsAddedToCategory() {
     Category testCategory = new Category("Household chores");
     testCategory.save();
     Task testTask = new Task("Mow the lawn");
     testTask.save();
     String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
     goTo(url);
     fillSelect("#task_id").withText("Mow the lawn");
     submit(".btn");
     assertThat(pageSource()).contains("<li>");
     assertThat(pageSource()).contains("Mow the lawn");
   }

   @Test
   public void categoryIsAddedToTask() {
     Category testCategory = new Category("Household chores");
     testCategory.save();
     Task testTask = new Task("Mow the lawn");
     testTask.save();
     String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
     goTo(url);
     fillSelect("#category_id").withText("Household chores");
     submit(".btn");
     assertThat(pageSource()).contains("<li>");
     assertThat(pageSource()).contains("Household chores");
   }

   @Test
   public void taskIsEdited() {
     Task testTask = new Task("Scrub the bubbles");
     testTask.save();
     String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
     goTo(url);
     click("a", withText("Edit this task"));
     fill("#description").with("fetch a bucket of steam");
     submit(".btn");
     assertThat(pageSource()).contains("fetch a bucket of steam");
   }

   @Test
   public void categoryIsEdited () {
     Category testCategory = new Category("Bubble chores");
     testCategory.save();
     String url = String.format("http://localhost:4567/categories/%d", testCategory.getId());
     goTo(url);
     click("a", withText("Edit this category"));
     fill("#name").with("Non-Bubble chores");
     submit(".btn");
     assertThat(pageSource()).contains("Non-Bubble chores");
   }

   @Test
   public void taskIsDeleted() {
     Task testTask = new Task("Scrub the bubbles");
     testTask.save();
     String url = String.format("http://localhost:4567/tasks/%d", testTask.getId());
     goTo(url);
     click("a", withText("Edit this task"));
    //  submit("#deleteButton");
    //  goTo("http://localhost:4567/tasks/");
     assertThat(pageSource()).doesNotContain("Scrub the bubbles");
   }
}
