import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_EVEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import se.ifmo.testing.ArcsinFunction;
import se.ifmo.testing.SeriesExpandableFunction;

class ArcsinFunctionTests {

  @Test
  void shouldNotAcceptNullArgument() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal precision = BigDecimal.valueOf(0.1);
    assertThrows(NullPointerException.class, () -> arcsin.calculate(null, precision));
  }

  @Test
  void shouldNotAcceptNullPrecision() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    assertThrows(NullPointerException.class, () -> arcsin.calculate(ZERO, null));
  }

  @ParameterizedTest
  @MethodSource("outOfBoundsArguments")
  void shouldNotAcceptIllegalArgument(final BigDecimal x) {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal precision = BigDecimal.valueOf(0.1);
    assertThrows(ArithmeticException.class, () -> arcsin.calculate(x, precision));
  }

  @ParameterizedTest
  @MethodSource("illegalPrecisions")
  void shouldNotAcceptIllegalPrecision(final BigDecimal precision) {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    assertThrows(ArithmeticException.class, () -> arcsin.calculate(ZERO, precision));
  }

  @Test
  void shouldCalculateCorrectlyForZero() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal precision = BigDecimal.valueOf(0.1);
    assertEquals(
        ZERO.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(0), precision));
  }

  @ParameterizedTest
  @MethodSource("precisions")
  void shouldCalculateCorrectlyForZeroWithDifferentPrecisions(final BigDecimal precision) {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    assertEquals(
        ZERO.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(0), precision));
  }

  @Test
  void shouldCalculateCorrectlyForUpperBound() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(1.5708);
    final BigDecimal precision = BigDecimal.valueOf(0.0001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN), arcsin.calculate(ONE, precision));
  }

  @Test
  void shouldCalculateCorrectlyForLowerBound() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(-1.5708);
    final BigDecimal precision = BigDecimal.valueOf(0.0001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(ONE.negate(), precision));
  }

  @Test
  void shouldCalculateCorrectlyForHalfPositiveValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(0.52359);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(0.5), precision));
  }

  @Test
  void shouldCalculateCorrectlyForUpperHalfPositiveValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(0.8633);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(0.76), precision));
  }

  @Test
  void shouldCalculateCorrectlyForLowerHalfPositiveValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(0.24236);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(0.24), precision));
  }

  @Test
  void shouldCalculateCorrectlyForHalfNegativeValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(-0.52359);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(-0.5), precision));
  }

  @Test
  void shouldCalculateCorrectlyForUpperHalfNegativeValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(-0.6944);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(-0.64), precision));
  }

  @Test
  void shouldCalculateCorrectlyForLowerHalfNegativeValue() {
    final SeriesExpandableFunction arcsin = new ArcsinFunction();
    final BigDecimal expectedValue = BigDecimal.valueOf(-0.3897);
    final BigDecimal precision = BigDecimal.valueOf(0.001);
    assertEquals(
        expectedValue.setScale(precision.scale(), HALF_EVEN),
        arcsin.calculate(BigDecimal.valueOf(-0.38), precision));
  }

  private static Stream<Arguments> outOfBoundsArguments() {
    return Stream.of(
        Arguments.of(BigDecimal.valueOf(5)),
        Arguments.of(BigDecimal.valueOf(-5)),
        Arguments.of(BigDecimal.valueOf(1.01)),
        Arguments.of(BigDecimal.valueOf(-1.01)));
  }

  private static Stream<Arguments> illegalPrecisions() {
    return Stream.of(
        Arguments.of(BigDecimal.valueOf(1)),
        Arguments.of(BigDecimal.valueOf(0)),
        Arguments.of(BigDecimal.valueOf(1.01)),
        Arguments.of(BigDecimal.valueOf(-0.01)));
  }

  private static Stream<Arguments> precisions() {
    return Stream.of(
        Arguments.of(BigDecimal.valueOf(0.1)),
        Arguments.of(BigDecimal.valueOf(0.01)),
        Arguments.of(BigDecimal.valueOf(0.00001)));
  }
}
