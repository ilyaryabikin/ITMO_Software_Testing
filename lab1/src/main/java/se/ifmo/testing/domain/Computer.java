package se.ifmo.testing.domain;

public class Computer implements WorkingInstrument {

  private final Location location;

  public Computer(final Location location) {
    this.location = location;
  }

  public String getSize() {
    return "Маленький";
  }

  public Location getLocation() {
    return location;
  }
}
