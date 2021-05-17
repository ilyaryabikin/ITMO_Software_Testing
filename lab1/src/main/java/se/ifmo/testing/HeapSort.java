package se.ifmo.testing;

import static java.util.Collections.swap;

import java.util.List;
import java.util.Objects;

public class HeapSort {

  public String log;
  private StringBuilder builder;

  public HeapSort() {
    this.log = "";
    this.builder = new StringBuilder();
  }

  public void sort(final List<Integer> array) {
    Objects.requireNonNull(array, "Array can not be null");

    int size = array.size();

    for (int i = size / 2 - 1; i >= 0; i--) {
      heapify(array, size, i);
    }

    builder.append("|| ");
    if (builder.length() != 3) {
      builder.deleteCharAt(builder.lastIndexOf(","));
    } else {
      builder.delete(builder.indexOf("|"), builder.length());
    }

    for (int i = size - 1; i >= 0; i--) {
      swap(array, 0, i);
      builder.append(array.get(0)).append(" -> ").append(array.get(i)).append(", ");
      heapify(array, i, 0);
    }

    if (builder.length() > 3) {
      builder.delete(builder.lastIndexOf(", "), builder.length());
      log = builder.toString();
    } else {
      log = "No swaps";
    }
  }

  public void heapify(final List<Integer> array, final int n, final int i) {
    Objects.requireNonNull(array, "Array can not be null");

    int largest = i;
    int leftChild = 2 * i + 1;
    int rightChild = 2 * i + 2;

    if (leftChild < n && array.get(leftChild) > array.get(largest)) {
      largest = leftChild;
    }

    if (rightChild < n && array.get(rightChild) > array.get(largest)) {
      largest = rightChild;
    }

    if (largest != i) {
      swap(array, i, largest);
      builder.append(array.get(i)).append(" -> ").append(array.get(largest)).append(", ");
      heapify(array, n, largest);
    }
  }
}
