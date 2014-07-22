package com.english.englishwords.app.algo;

import android.util.Pair;

import com.english.englishwords.app.data_model.Word;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The class that selects the n closest words to the given one using distance function.
 * TODO(Bogdan) add tests
 * TODO(Bogdan) refactor the code to do it once upon initialization
 */


public class DistanceWordSelector extends WordSelector {
  public DistanceWordSelector(DistanceCalculatorInterface distanceCalculator) {
    super(distanceCalculator);
  }

  @Override
  public List<String> SelectNClosestWords(
      String word, List<String> allWords, int number_to_select) {
    // TODO(Bogdan) replace 10000 with the constant defining the size of the dictionary
    // krasikov: Why PriorityQueue? why not ArrayList + sorting?
    PriorityQueue<Pair<String, Integer>> priorityQueue =
        new PriorityQueue<Pair<String, Integer>>(
            10000, new Comparator<Pair<String, Integer>>() {
          @Override
          public int compare(Pair<String, Integer> lhs, Pair<String, Integer> rhs) {
            return lhs.second - rhs.second;
          }
        }
        );
    for (String w : allWords) {
      Pair<String, Integer> wordWithDistance =
          new Pair<String, Integer>(w, distanceCalculator.GetDistance(w, word));
      priorityQueue.add(wordWithDistance);
    }

    List<String> selectedWords = new ArrayList<String>();
    for (int i = 0; i < number_to_select; i++) {
      Pair<String, Integer> pair = priorityQueue.poll();
      // eliminate the words with the same spelling
      // krasikov: I guess this can be moved outside of the for loop.
      while (pair.second == 0) {
        pair = priorityQueue.poll();
      }
      selectedWords.add(pair.first);
    }
    return selectedWords;
  }
}
