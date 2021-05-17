package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class SecTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

  @Mock private CosFunction mockCos;
  @Spy private CosFunction spyCos;

  @Test
  void shouldCallCosFunction() {
    final var sec = new SecFunction(spyCos);
    sec.calculate(new BigDecimal(6), new BigDecimal("0.001"));

    verify(spyCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  void shouldCalculateWithMockCos() {
    final var arg = new BigDecimal(5);
    final var sinResult = new BigDecimal("0.283662");

    when(mockCos.calculate(eq(arg), any(BigDecimal.class))).thenReturn(sinResult);

    final var sec = new SecFunction(mockCos);
    final var expected = new BigDecimal("3.525322");
    assertEquals(expected, sec.calculate(arg, new BigDecimal("0.000001")));
  }

  @Test
  void shouldCalculateForZero() {
    final var sec = new SecFunction();
    assertEquals(ONE.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN), sec.calculate(ZERO, DEFAULT_PRECISION));
  }

  @Test
  void shouldNotCalculateForPiDividedByTwo() {
    final var sec = new SecFunction();
    final var mc = new MathContext(DECIMAL128.getPrecision());
    final var arg =
        BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
    assertThrows(ArithmeticException.class, () -> sec.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForOne() {
    final var sec = new SecFunction();
    final var expected = new BigDecimal("1.8508");
    assertEquals(expected, sec.calculate(ONE, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPeriodic() {
    final var sec = new SecFunction();
    final var expected = new BigDecimal("-1.1261");
    assertEquals(expected, sec.calculate(new BigDecimal(123), DEFAULT_PRECISION));
  }
}
