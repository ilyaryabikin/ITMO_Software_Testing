package se.ifmo.testing.domain;

public class Ford {

  private boolean isTriedToSleep = false;
  private boolean isSucceededInWork = true;
  private Location currentLocation = new Location("В каюте");
  private WorkingInstrument workingInstrument;

  public void sleep() {
    isTriedToSleep = true;
  }

  public String getMood() {
    if (!isTriedToSleep) {
      return "Форд не пытался уснуть";
    } else {
      return "Форд отказался от попыток уснуть";
    }
  }

  public void setWorkingInstrument(final WorkingInstrument workingInstrument) {
    this.workingInstrument = workingInstrument;
  }

  public Location getLocation() {
    return currentLocation;
  }

  public Article writeArticle(final String articleTheme) {
    if (!currentLocation.getLocationName().equals("За компьютером")
        && !(workingInstrument instanceof Computer)) {
      throw new IllegalStateException("Форд не может писать статью не за компьютером");
    }
    if (articleTheme.equals("Статья о вогонах для Путеводителя")) {
      final Article article =
          new Article(
              articleTheme,
              "Форд посидел за компьютером, пытаясь сочинить новую статью о вогонах "
                  + "для Путеводителя, но не смог выдумать ничего достаточно едкого и бросил");
      isSucceededInWork = false;
      currentLocation = new Location("Надел халат и вышел на мостик");
      return article;
    }
    throw new IllegalArgumentException("Форд не хочет писать статью на эту тему");
  }
}
