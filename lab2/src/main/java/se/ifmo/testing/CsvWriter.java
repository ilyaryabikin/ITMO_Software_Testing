package se.ifmo.testing;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;

public class CsvWriter {

  public static void write(
      final String filename,
      final SeriesExpandableFunction function,
      final BigDecimal from,
      final BigDecimal to,
      final BigDecimal step,
      final BigDecimal precision)
      throws IOException {
    final var path = Paths.get(filename);
    final var file = new File(path.toUri());
    if (file.exists()) {
      file.delete();
    }
    file.createNewFile();
    final var printWriter = new PrintWriter(file);
    for (var current = from; current.compareTo(to) <= 0; current = current.add(step)) {
      printWriter.println(current + "," + function.calculate(current, precision));
    }
    printWriter.close();
  }

  public static void write(
      final String filename, final List<BigDecimal> arguments, final List<BigDecimal> values)
      throws IOException {
    final var path = Paths.get(filename);
    final var file = new File(path.toUri());
    if (file.exists()) {
      file.delete();
    }
    file.createNewFile();
    final var printWriter = new PrintWriter(file);
    for (int i = 0; i < arguments.size() && i < values.size(); i++) {
      printWriter.println(arguments.get(i) + "," + values.get(i));
    }
    printWriter.close();
  }
}
