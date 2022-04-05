// Taylor Merwin

public class Die {

  private int sides;

  public Die(int sides) {
    this.sides = sides;
  }

  public int getSides() {
    return sides;
  }

  public int roll() {
    return 1 + (int) (Math.random() * sides);
  }
}
