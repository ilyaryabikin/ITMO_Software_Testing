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
class TanTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.0001");

  @Mock private SinFunction mockSin;
  @Mock private CosFunction mockCos;
  @Spy private SinFunction spySin;

  @Test
  void shouldCallSinAndCosFunctions() {
    final var cos = new CosFunction(spySin);
    final var spyCos = spy(cos);

    final var tan = new TanFunction(spySin, spyCos);
    tan.calculate(ZERO, DEFAULT_PRECISION);

    verify(spySin, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
    verify(spyCos, atLeastOnce()).calculate(any(BigDecimal.class), any(BigDecimal.class));
  }

  @Test
  void shouldCalculateWithMockSinAndMockCos() {
    final var arg = new BigDecimal(5);

    when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("-0.95892427"));
    when(mockCos.calculate(eq(arg), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("0.28366218"));

    final var tan = new TanFunction(mockSin, mockCos);
    final var expectedResult = new BigDecimal("-3.3805");
    assertEquals(expectedResult, tan.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateWithMockSin() {
    final var arg = new BigDecimal(5);

    when(mockSin.calculate(eq(arg), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("-0.95892427"));

    final var tan = new TanFunction(mockSin, new CosFunction());
    final var expectedResult = new BigDecimal("-3.3801");
    assertEquals(expectedResult, tan.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateWithMockCos() {
    final var arg = new BigDecimal(5);

    when(mockCos.calculate(eq(arg), any(BigDecimal.class)))
        .thenReturn(new BigDecimal("0.28366218"));

    final var tan = new TanFunction(new SinFunction(), mockCos);
    final var expectedResult = new BigDecimal("-3.3804");
    assertEquals(expectedResult, tan.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForZero() {
    final var tan = new TanFunction();
    assertEquals(
        ZERO.setScale(DEFAULT_PRECISION.scale(), HALF_EVEN),
        tan.calculate(ZERO, DEFAULT_PRECISION));
  }

  @Test
  void shouldNotCalculateForPiDividedByTwo() {
    final var tan = new TanFunction();
    final var mc = new MathContext(DECIMAL128.getPrecision());
    final var arg =
        BigDecimalMath.pi(mc).divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN);
    assertThrows(ArithmeticException.class, () -> tan.calculate(arg, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForOne() {
    final var tan = new TanFunction();
    final var expected = new BigDecimal("1.5575");
    assertEquals(expected, tan.calculate(ONE, DEFAULT_PRECISION));
  }

  @Test
  void shouldCalculateForPeriodic() {
    final var tan = new TanFunction();
    final var expected = new BigDecimal("-1.9101");
    assertEquals(expected, tan.calculate(new BigDecimal(134), DEFAULT_PRECISION));
  }
}
