package com.english.englishwords.app.algo;

import com.english.englishwords.app.data_model.Word;

import java.util.List;

/**
 * The abstract class that takes provides the interface to pick the closest words to the given one
 */
abstract class WordSelector {
  protected DistanceCalculatorInterface distanceCalculator;

  WordSelector(DistanceCalculatorInterface distanceCalculator) {
    this.distanceCalculator = distanceCalculator;
  }

  public abstract List<Word> SelectNClosestWords(
      Word word, List<Word> allWords, int number_of_words);
}
