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
  public List<Word> SelectNClosestWords(Word word, List<Word> allWords, int number_to_select) {
    // TODO(Bogdan) replace 10000 with the constant defining the size of the dictionary
    PriorityQueue<Pair<Word, Integer>> priorityQueue =
        new PriorityQueue<Pair<Word, Integer>>(
            10000, new Comparator<Pair<Word, Integer>>() {
          @Override
          public int compare(Pair<Word, Integer> lhs, Pair<Word, Integer> rhs) {
            return lhs.second - rhs.second;
          }
        }
        );
    for (Word w : allWords) {
      Pair<Word, Integer> wordWithDistance =
          new Pair<Word, Integer>(w, distanceCalculator.GetDistance(w.getWord(), word.getWord()));
      priorityQueue.add(wordWithDistance);
    }

    List<Word> selectedWords = new ArrayList<Word>();
    for (int i = 0; i < number_to_select; i++) {
      Pair<Word, Integer> pair = priorityQueue.poll();
      // eliminate the words with the same spelling
      while (pair.second == 0) {
        pair = priorityQueue.poll();
      }
      selectedWords.add(pair.first);
    }
    return selectedWords;
  }
}
