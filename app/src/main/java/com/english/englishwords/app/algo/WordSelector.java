package com.english.englishwords.app.algo;

import java.util.List;

/**
 * The abstract class that takes provides the interface to pick the closest words to the given one
 */
public abstract class WordSelector {
  protected DistanceCalculatorInterface distanceCalculator;

  WordSelector(DistanceCalculatorInterface distanceCalculator) {
    this.distanceCalculator = distanceCalculator;
  }

  public abstract List<String> SelectNClosestWords(
      String word, String[] allWords, int number_of_words);
}
