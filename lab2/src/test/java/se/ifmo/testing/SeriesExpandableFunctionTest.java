package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SeriesExpandableFunctionTest {

  private static final BigDecimal DEFAULT_PRECISION = new BigDecimal("0.000001");

  @ParameterizedTest
  @MethodSource("functions")
  void shouldNotAcceptNullArgument(final SeriesExpandableFunction function) {
    assertThrows(NullPointerException.class, () -> function.calculate(null, DEFAULT_PRECISION));
  }

  @ParameterizedTest
  @MethodSource("functions")
  void shouldNotAcceptNullPrecision(final SeriesExpandableFunction function) {
    assertThrows(NullPointerException.class, () -> function.calculate(ONE, null));
  }

  @ParameterizedTest
  @MethodSource("functions")
  void shouldNotAcceptIncorrectPrecision(final SeriesExpandableFunction function) {
    assertThrows(ArithmeticException.class, () -> function.calculate(ONE, new BigDecimal(5)));
  }

  private static Stream<Arguments> functions() {
    return Stream.of(
        Arguments.of(new SinFunction()),
        Arguments.of(new CosFunction()),
        Arguments.of(new TanFunction()),
        Arguments.of(new LnFunction()),
        Arguments.of(new LogFunction(3)),
        Arguments.of(new LogFunction(5)),
        Arguments.of(new LogFunction(10)));
  }
}
