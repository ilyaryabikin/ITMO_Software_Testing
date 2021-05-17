package se.ifmo.testing;

import static java.lang.String.format;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import java.math.BigDecimal;

public class TanFunction extends LimitedIterationsExpandableFunction {

  private final SinFunction sin;
  private final CosFunction cos;

  public TanFunction(final SinFunction sin, final CosFunction cos) {
    super();
    this.sin = sin;
    this.cos = cos;
  }

  public TanFunction(final SinFunction sin, final CosFunction cos, final int maxIterations) {
    super(maxIterations);
    this.sin = sin;
    this.cos = cos;
  }

  public TanFunction() {
    super();
    this.sin = new SinFunction();
    this.cos = new CosFunction();
  }

  public TanFunction(final int maxIterations) {
    super(maxIterations);
    this.sin = new SinFunction();
    this.cos = new CosFunction();
  }

  @Override
  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
      throws ArithmeticException {
    checkValidity(x, precision);

    final var sinValue = sin.calculate(x, precision);
    final var cosValue = cos.calculate(x, precision);

    if (cosValue.compareTo(ZERO) == 0) {
      throw new ArithmeticException(format("Function value for argument %s doesn't exist", x));
    }

    final var result = sinValue.divide(cosValue, DECIMAL128.getPrecision(), HALF_EVEN);
    return result.setScale(precision.scale(), HALF_EVEN);
  }
}
