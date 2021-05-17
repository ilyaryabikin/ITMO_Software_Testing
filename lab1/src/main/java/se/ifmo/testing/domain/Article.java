package se.ifmo.testing.domain;

public class Article {

  private final String theme;
  private final String body;

  public Article(final String theme, final String body) {
    this.theme = theme;
    this.body = body;
  }

  public String getTheme() {
    return theme;
  }

  public String getBody() {
    return body;
  }
}
