package com.english.englishwords.app.algo;

import com.english.englishwords.app.data_model.Word;

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
      String word, List<String> allWords, int number_of_words);
}
