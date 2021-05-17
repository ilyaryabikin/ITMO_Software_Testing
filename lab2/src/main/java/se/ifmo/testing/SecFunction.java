package se.ifmo.testing;

import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

public class SecFunction extends LimitedIterationsExpandableFunction {

  private final CosFunction cos;

  public SecFunction(final CosFunction cos) {
    super();
    this.cos = cos;
  }

  public SecFunction(final CosFunction cos, final int maxIterations) {
    super(maxIterations);
    this.cos = cos;
  }

  public SecFunction() {
    super();
    this.cos = new CosFunction();
  }

  public SecFunction(final int maxIterations) {
    super(maxIterations);
    this.cos = new CosFunction();
  }

  @Override
  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
      throws ArithmeticException {
    checkValidity(x, precision);

    final var cosValue = cos.calculate(x, precision);

    if (cosValue.compareTo(ZERO) == 0) {
      throw new ArithmeticException(format("Function value for argument %s doesn't exist", x));
    }

    final var result = ONE.divide(cosValue, DECIMAL128.getPrecision(), HALF_EVEN);
    return result.setScale(precision.scale(), HALF_EVEN);
  }
}
