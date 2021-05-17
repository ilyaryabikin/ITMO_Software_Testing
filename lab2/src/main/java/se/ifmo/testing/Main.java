package se.ifmo.testing;

import java.io.IOException;
import java.math.BigDecimal;

public class Main {

  public static void main(String[] args) throws IOException {
    final var cos = new CosFunction();
    CsvWriter.write(
        "cos.csv",
        cos,
        new BigDecimal(-1),
        new BigDecimal(1),
        new BigDecimal("0.1"),
        new BigDecimal("0.001"));

    final var sin = new SinFunction();
    CsvWriter.write(
            "sin.csv",
            sin,
            new BigDecimal(-1),
            new BigDecimal(1),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var tan = new TanFunction();
    CsvWriter.write(
            "tan.csv",
            tan,
            new BigDecimal(-1),
            new BigDecimal(1),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var sec = new SecFunction();
    CsvWriter.write(
            "sec.csv",
            sec,
            new BigDecimal(-1),
            new BigDecimal(1),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var ln = new LnFunction();
    CsvWriter.write(
            "ln.csv",
            ln,
            new BigDecimal(1),
            new BigDecimal(20),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var log3 = new LogFunction(3);
    CsvWriter.write(
            "log3.csv",
            log3,
            new BigDecimal(1),
            new BigDecimal(20),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var log5 = new LogFunction(5);
    CsvWriter.write(
            "log5.csv",
            log5,
            new BigDecimal(1),
            new BigDecimal(20),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));

    final var log10 = new LogFunction(10);
    CsvWriter.write(
            "log10.csv",
            log10,
            new BigDecimal(1),
            new BigDecimal(20),
            new BigDecimal("0.1"),
            new BigDecimal("0.001"));
  }
}
