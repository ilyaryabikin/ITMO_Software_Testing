package se.ifmo.testing;

import static java.math.BigDecimal.ZERO;
import static java.math.MathContext.DECIMAL128;
import static java.math.RoundingMode.HALF_EVEN;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;

public class FunctionsSystem {

  private final SinFunction sin;
  private final TanFunction tan;
  private final SecFunction sec;
  private final LnFunction ln;
  private final LogFunction log3;
  private final LogFunction log5;
  private final LogFunction log10;

  public FunctionsSystem() {
    this.sin = new SinFunction();
    this.tan = new TanFunction();
    this.sec = new SecFunction();
    this.ln = new LnFunction();
    this.log3 = new LogFunction(3);
    this.log5 = new LogFunction(5);
    this.log10 = new LogFunction(10);
  }

  public FunctionsSystem(
      final SinFunction sin,
      final TanFunction tan,
      final SecFunction sec,
      final LnFunction ln,
      final LogFunction log3,
      final LogFunction log5,
      final LogFunction log10) {
    this.sin = sin;
    this.tan = tan;
    this.sec = sec;
    this.ln = ln;
    this.log3 = log3;
    this.log5 = log5;
    this.log10 = log10;
  }

  public BigDecimal calculate(final BigDecimal x, final BigDecimal precision) {
    if (x.compareTo(ZERO) <= 0) {
      final var mc = new MathContext(DECIMAL128.getPrecision(), HALF_EVEN);
      final var correctedX = x.remainder(BigDecimalMath.pi(mc).multiply(new BigDecimal(2)));
      return tan.calculate(correctedX, precision)
          .add(tan.calculate(correctedX, precision))
          .divide(tan.calculate(correctedX, precision), DECIMAL128.getPrecision(), HALF_EVEN)
          .multiply(
              sin.calculate(correctedX, precision).subtract(tan.calculate(correctedX, precision)))
          .add(sec.calculate(correctedX, precision).pow(3))
          .setScale(precision.scale(), HALF_EVEN);
    } else {
      return log10
          .calculate(x, precision)
          .add(log3.calculate(x, precision))
          .multiply(
              ln.calculate(x, precision)
                  .divide(log5.calculate(x, precision), DECIMAL128.getPrecision(), HALF_EVEN))
          .pow(3)
          .pow(3)
          .pow(3)
          .setScale(precision.scale(), HALF_EVEN);
    }
  }
}
