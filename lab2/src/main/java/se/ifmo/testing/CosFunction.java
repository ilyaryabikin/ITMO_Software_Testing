package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public class CosFunction extends LimitedIterationsExpandableFunction {

  private final SinFunction sin;

  public CosFunction() {
    super();
    this.sin = new SinFunction();
  }

  public CosFunction(final int maxIterations) {
    super(maxIterations);
    this.sin = new SinFunction();
  }

  public CosFunction(final SinFunction sin) {
    super();
    this.sin = sin;
  }

  public CosFunction(final SinFunction sin, final int maxIterations) {
    super(maxIterations);
    this.sin = sin;
  }

  @Override
  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
      throws ArithmeticException {
    checkValidity(x, precision);

    final var mc = new MathContext(DECIMAL128.getPrecision(), HALF_EVEN);
    final var correctedX = x.remainder(BigDecimalMath.pi(mc).multiply(new BigDecimal(2)));

    if (correctedX.compareTo(ZERO) == 0) {
      return ONE;
    }

    final var result =
        sin.calculate(
            BigDecimalMath.pi(mc)
                .divide(new BigDecimal(2), DECIMAL128.getPrecision(), HALF_EVEN)
                .subtract(correctedX),
            precision);
    return result.setScale(precision.scale(), HALF_EVEN);
  }
}
