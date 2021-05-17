package se.ifmo.testing;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public class SinFunction extends LimitedIterationsExpandableFunction {

  public SinFunction() {
    super();
  }

  public SinFunction(final int maxIterations) {
    super(maxIterations);
  }

  @Override
  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision)
      throws ArithmeticException {
    checkValidity(x, precision);

    final var mc = new MathContext(DECIMAL128.getPrecision(), HALF_EVEN);
    final var correctedX = x.remainder(BigDecimalMath.pi(mc).multiply(new BigDecimal(2)));

    if (correctedX.compareTo(ZERO) == 0) {
      return ZERO;
    }

    BigDecimal sum = ZERO;
    BigDecimal previousSum;
    int i = 0;
    do {
      if (i > maxIterations) {
        throw new ArithmeticException(
            "Precision can not be reached with specified max iterations. Max value is " + sum);
      }
      previousSum = sum;
      sum =
          sum.add(
              ONE.negate()
                  .pow(i)
                  .multiply(correctedX.pow(2 * i + 1))
                  .divide(factorial(2L * i + 1), DECIMAL128.getPrecision(), HALF_EVEN));
      i++;
    } while (sum.subtract(previousSum).abs().compareTo(precision) >= 0);
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
