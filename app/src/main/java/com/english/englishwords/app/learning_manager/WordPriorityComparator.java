package com.english.englishwords.app.learning_manager;

import android.util.Pair;

import com.english.englishwords.app.dao.WordListsDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.WordStats;

import java.util.Comparator;
import java.util.Date;

public class WordPriorityComparator implements Comparator<String> {
  public static final long MINUTE = 60000000; // in milliseconds.
  public static final long HOUR = 60 * MINUTE;
  public static final long DAY = 24 * HOUR;
  private final WordStatsDAO wordStatsDAO;
  private final WordListsDAO wordListDAO;

  public WordPriorityComparator(WordListsDAO wordListDAO, WordStatsDAO wordStatsDAO) {
    this.wordListDAO = wordListDAO;
    this.wordStatsDAO = wordStatsDAO;
  }

  private long whenToLearn(WordStats wordStats) {
    long rightNow = new Date().getTime();

    // New word.
    if (wordStats.history.size() == 0) {
      // All new words should be scheduled for right now.
      return rightNow;
    }

    // Only one successful test in word stats.
    Pair<Long, Boolean> lastRepetition = wordStats.history.get(wordStats.history.size() - 1);
    if (wordStats.history.size() == 1 && lastRepetition.second) {
      return lastRepetition.first + 24 * 60 * MINUTE;
    }

    // The last test was unsuccessful.
    if (!lastRepetition.second) {
      // Try to test word again regardless of its level to ensure users didn't forget it
      // completely.
      return lastRepetition.first + 4 * MINUTE;
    }

    // We have more than 2 tests for this word and last test was successful.
    return lastRepetition.first + getWordMemorizationDelay(wordStats);
  }

  // Calculated as weighted average of historic time spans of 3 previous tests (for unsuccessful
  // test weight is 0).
  private int getWordMemorizationDelay(WordStats wordStats) {
    double retentionApproximation = 0;
    double[] coefs = new double[]{5, 3, 1};
    double[] coefsPartialSums = new double[]{5, 8, 9};
    int history_size = wordStats.history.size();
    for (int i = history_size - 1; i >= Math.max(history_size - 3, 0); i--) {
      Pair<Long, Boolean> wordStat = wordStats.history.get(i);
      if (wordStat.second) {
        long timeBetweenExercises = wordStat.first - wordStats.history.get(i - 1).first;
        retentionApproximation += timeBetweenExercises * coefs[history_size - i - 1];
      }
    }
    double normalization = coefsPartialSums[Math.max(history_size - 1, 2)] / 2.5;
    return (int) (retentionApproximation / normalization);
  }

  @Override
  public int compare(String word1, String word2) {
    if (word1.equals(word2)) {
      return 0;
    }
    WordStats wordStats1 = wordStatsDAO.getStats(word1);
    WordStats wordStats2 = wordStatsDAO.getStats(word2);
    long when1 = whenToLearn(wordStats1);
    long when2 = whenToLearn(wordStats2);
    if (when1 == when2) {
      // Compare priority according to the original list.
      return
          wordListDAO.getPositionInUsageFrequencyList(word1) <
              wordListDAO.getPositionInUsageFrequencyList(word2)
              ? -1 : 1;
    }
    return when1 < when2 ? -1 : 1;
  }
}
