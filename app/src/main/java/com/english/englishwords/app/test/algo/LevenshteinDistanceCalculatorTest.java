package com.english.englishwords.app.test.algo;

import android.test.InstrumentationTestCase;

import com.english.englishwords.app.algo.LevenshteinDistanceCalculator;

public class LevenshteinDistanceCalculatorTest extends InstrumentationTestCase {
  public void test() throws Exception {
    LevenshteinDistanceCalculator calculator = new LevenshteinDistanceCalculator();
    assertEquals(0, calculator.GetDistance("test", "test"));
    assertEquals(4, calculator.GetDistance("test", ""));
    assertEquals(4, calculator.GetDistance("", "test"));
    assertEquals(0, calculator.GetDistance("", ""));
    assertEquals(1, calculator.GetDistance("winner", "wonner"));
    assertEquals(5, calculator.GetDistance("father", "grandfather"));
    assertEquals(3, calculator.GetDistance("shallow", "mellow"));
  }
}