package se.ifmo.testing;

import java.math.BigDecimal;

@FunctionalInterface
public interface SeriesExpandableFunction {

  BigDecimal calculate(final BigDecimal x, final BigDecimal precision) throws ArithmeticException;
}
