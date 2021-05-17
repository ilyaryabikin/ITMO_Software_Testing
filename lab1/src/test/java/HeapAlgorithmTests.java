import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.ifmo.testing.HeapSort;

class HeapAlgorithmTests {

  private static final String NO_SWAPS_LOG = "No swaps";
  private static final List<Integer> defaultArrayToSort =
      List.of(35, 33, 42, 10, 14, 19, 27, 44, 26, 31);
  private static final List<Integer> sortedArray = List.of(10, 14, 19, 26, 27, 31, 33, 35, 42, 44);

  @Test
  void shouldNotAcceptNullValue() {
    final HeapSort heapSort = new HeapSort();
    assertThrows(NullPointerException.class, () -> heapSort.sort(null));
  }

  @Test
  void shouldSortCorrectly() {
    final List<Integer> array = new ArrayList<>(defaultArrayToSort);
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertIterableEquals(sortedArray, array);
  }

  @Test
  void shouldDoCorrectSwaps() {
    final String log =
        "31 -> 14, 44 -> 10, 44 -> 33, 44 -> 35 || 14 -> 44, 42 -> 14, 27 -> 14, 26 -> 42, 35 -> 26, 33 -> 26, 10 -> 35, 33 -> 10, 31 -> 10, 14 -> 33, 31 -> 14, 26 -> 14, 19 -> 31, 27 -> 19, 10 -> 27, 26 -> 10, 14 -> 10, 10 -> 26, 19 -> 10, 10 -> 19, 14 -> 10, 10 -> 14, 10 -> 10";
    final List<Integer> array = new ArrayList<>(defaultArrayToSort);
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertEquals(log, heapSort.log);
  }

  @Test
  void shouldNotDoSwapsWithEmptyArray() {
    final List<Integer> array = new ArrayList<>();
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertEquals(NO_SWAPS_LOG, heapSort.log);
  }

  @Test
  void shouldDoCorrectSwapsWithSortedArray() {
    final String log =
        "44 -> 27, 42 -> 26, 33 -> 19, 44 -> 14, 27 -> 14, 44 -> 10, 42 -> 10, 35 -> 10 || 14 -> 44, 42 -> 14, 35 -> 14, 26 -> 14, 14 -> 42, 35 -> 14, 27 -> 14, 10 -> 35, 33 -> 10, 31 -> 10, 19 -> 33, 31 -> 19, 10 -> 31, 27 -> 10, 26 -> 10, 14 -> 27, 26 -> 14, 10 -> 26, 19 -> 10, 10 -> 19, 14 -> 10, 10 -> 14, 10 -> 10";
    final List<Integer> array = new ArrayList<>(sortedArray);
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertEquals(log, heapSort.log);
  }

  @Test
  void shouldDoCorrectSwapsWithReverseSortedArray() {
    final String log =
        "10 -> 44, 42 -> 10, 33 -> 10, 19 -> 10, 14 -> 42, 35 -> 14, 27 -> 14, 10 -> 35, 33 -> 10, 31 -> 10, 26 -> 33, 31 -> 26, 14 -> 31, 27 -> 14, 10 -> 27, 26 -> 10, 19 -> 10, 10 -> 26, 19 -> 10, 14 -> 19, 10 -> 14, 10 -> 10";
    final List<Integer> array = new ArrayList<>(sortedArray);
    array.sort(Collections.reverseOrder());
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertEquals(log, heapSort.log);
  }

  @Test
  void shouldNotDoSwapsWithOneElementArray() {
    final String log = "5 -> 5";
    final List<Integer> array = new ArrayList<>(Arrays.asList(5));
    final HeapSort heapSort = new HeapSort();
    heapSort.sort(array);
    assertEquals(log, heapSort.log);
  }
}
