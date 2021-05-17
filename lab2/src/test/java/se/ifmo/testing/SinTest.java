package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.jupiter.api.Test;

class SinTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

  @Test
  void shouldCalculateForZero() {
    final var sin = new SinFunction();
    assertEquals(ZERO, sin.calculate(ZERO, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPiDividedByTwo() {
    final var sin = new SinFunction();
    final var mc = new MathContext(DECIMAL128.getPrecision());
    final var arg =
        BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
    assertEquals(
        ONE.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), sin.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForOne() {
    final var sin = new SinFunction();
    final var expected = new BigDecimal("0.8415");
    assertEquals(expected, sin.calculate(ONE, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPeriodic() {
    final var sin = new SinFunction();
    final var expected = new BigDecimal("0.0972");
    assertEquals(expected, sin.calculate(new BigDecimal(-113), DEFAULT_PRECISION));
  }
}
