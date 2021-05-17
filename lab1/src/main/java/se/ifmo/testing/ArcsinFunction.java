package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;

public class ArcsinFunction implements SeriesExpandableFunction {

  private static final int DEFAULT_MAX_ITERATIONS = 1000;

  private final int maxIterations;

  public ArcsinFunction() {
    this.maxIterations = DEFAULT_MAX_ITERATIONS;
  }

  public ArcsinFunction(final int maxIterations) {
    this.maxIterations = maxIterations;
  }

  @Override
  public BigDecimal calculate(BigDecimal x, final BigDecimal precision) {
    Objects.requireNonNull(x, "Function argument can not be null");
    Objects.requireNonNull(precision, "Precision can not be null");
    if (x.abs().compareTo(ONE) > 0) {
      throw new ArithmeticException("Function argument can not be more than |1|");
    }
    if (precision.compareTo(ZERO) <= 0 || precision.compareTo(ONE) >= 0) {
      throw new ArithmeticException("Precision must be less than one and more than zero");
    }
    if (x.compareTo(ONE) == 0) {
      final var mc = new MathContext(precision.scale(), HALF_EVEN);
      return BigDecimalMath.pi(mc)
          .divide(valueOf(2), HALF_EVEN)
          .setScale(precision.scale(), HALF_EVEN);
    }
    if (x.compareTo(ONE.negate()) == 0) {
      final var mc = new MathContext(precision.scale(), HALF_EVEN);
      return BigDecimalMath.pi(mc)
          .divide(valueOf(2), HALF_EVEN)
          .negate()
          .setScale(precision.scale(), HALF_EVEN);
    }

    BigDecimal sum = new BigDecimal(x.toString());
    BigDecimal previousSum = sum.add(precision);
    for (int i = 1; sum.subtract(previousSum).abs().compareTo(precision) >= 0; i++) {
      if (i > maxIterations) {
        throw new ArithmeticException(
            "Precision can not be reached with specified max iterations. Max value is " + sum);
      }
      previousSum = sum;
      sum =
          sum.add(
              factorial(2L * i)
                  .divide(
                      factorial(i).multiply(valueOf(Math.pow(2, i))).pow(2),
                      DECIMAL128.getPrecision(),
                      HALF_EVEN)
                  .multiply(
                      x.pow(2 * i + 1)
                          .divide(valueOf(2L * i + 1), DECIMAL128.getPrecision(), HALF_EVEN)));
    }
    return sum.setScale(precision.scale(), HALF_EVEN);
  }

  private BigDecimal factorial(final long n) {
    if (n == 0) {
      return ONE;
    }
    BigDecimal result = ONE;
    for (long i = 1; i <= n; i++) {
      result = result.multiply(valueOf(i));
    }
    return result;
  }
}
