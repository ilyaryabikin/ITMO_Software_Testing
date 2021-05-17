import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.ifmo.testing.domain.Computer;
import se.ifmo.testing.domain.Ford;
import se.ifmo.testing.domain.Location;
import se.ifmo.testing.domain.WorkingInstrument;

class DomainModelTests {

  private Ford ford;
  private WorkingInstrument computer;

  @BeforeEach
  public void init() {
    this.computer = new Computer(new Location("Угол каюты"));
    this.ford = new Ford();
  }

  @Test
  void fordShouldNotSleepInitially() {
    assertEquals("Форд не пытался уснуть", ford.getMood());
  }

  @Test
  void fordShouldBeInCabinInitially() {
    assertEquals("В каюте", ford.getLocation().getLocationName());
  }

  @Test
  void fordFailsToSleep() {
    ford.sleep();
    assertEquals("Форд отказался от попыток уснуть", ford.getMood());
  }

  @Test
  void fordShouldProduceCorrectArticle() {
    ford.setWorkingInstrument(computer);
    final var article = ford.writeArticle("Статья о вогонах для Путеводителя");
    assertEquals(
        "Форд посидел за компьютером, пытаясь сочинить новую статью о вогонах для Путеводителя, "
            + "но не смог выдумать ничего достаточно едкого и бросил",
        article.getBody());
  }

  @Test
  void fordShouldGoToBridgeAfterArticle() {
    final String locationName = "Надел халат и вышел на мостик";
    assertNotEquals(locationName, ford.getLocation().getLocationName());
    ford.setWorkingInstrument(computer);
    ford.writeArticle("Статья о вогонах для Путеводителя");
    assertEquals(locationName, ford.getLocation().getLocationName());
  }

  @Test
  void fordShouldNotWriteArticleWithoutComputer() {
    assertThrows(IllegalStateException.class, () -> ford.writeArticle("Статья"));
  }

  @Test
  void fordShouldNotWriteArticleWithIncorrectTheme() {
    ford.setWorkingInstrument(computer);
    assertThrows(
        IllegalArgumentException.class, () -> ford.writeArticle("Топ анекдотов Галактики"));
  }
}
