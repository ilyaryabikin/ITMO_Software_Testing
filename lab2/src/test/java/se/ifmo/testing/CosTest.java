package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CosTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

  @Mock private SinFunction mockSin;
  @Spy private SinFunction spySin;

  @Test
  void shouldCallSinFunction() {
    final var cos = new CosFunction(spySin);
    cos.calculate(new BigDecimal(6), new BigDecimal("0.001"));

    verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  void shouldCalculateWithMockSin() {
    final var arg = new BigDecimal(5);
    final var mc = new MathContext(DECIMAL128.getPrecision());
    final var correctedArg =
        BigDecimalMath.pi(mc)
            .divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN)
            .subtract(arg);
    final var sinResult = new BigDecimal("0.283662");

    when(mockSin.calculate(eq(correctedArg), any(BigDecimal.class))).thenReturn(sinResult);

    final var cos = new CosFunction(mockSin);

    assertEquals(sinResult, cos.calculate(arg, new BigDecimal("0.000001")));
  }

  @Test
  void shouldCalculateForZero() {
    final var cos = new CosFunction();
    assertEquals(ONE, cos.calculate(ZERO, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPiDividedByTwo() {
    final var cos = new CosFunction();
    final var mc = new MathContext(DECIMAL128.getPrecision());
    final var arg =
        BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
    assertEquals(
        ZERO.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), cos.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForOne() {
    final var cos = new CosFunction();
    final var expected = new BigDecimal("0.5403");
    assertEquals(expected, cos.calculate(ONE, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPeriodic() {
    final var cos = new CosFunction();
    final var expected = new BigDecimal("-0.8797");
    assertEquals(expected, cos.calculate(new BigDecimal(-543), DEFAULT_PRECISION));
  }
}
