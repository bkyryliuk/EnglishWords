package com.english.englishwords.app.learning_manager;

import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;

import java.util.Comparator;
import java.util.Date;

public class WordPriorityComparator implements Comparator<String> {
  public final long ONE_MINUTE = 60000000; // in milliseconds.
  public final long ONE_HOUR = 60 * ONE_MINUTE;
  public final long ONE_DAY = 24 * ONE_HOUR;

  private LearningManager learningManager;

  public WordPriorityComparator(LearningManager learningManager) {
    this.learningManager = learningManager;
  }

  private long whenToLearn(WordStats wordStats) {
    long rightNow = new Date().getTime();

    // New word.
    if (wordStats.history.size() == 0) {
      // All new words should be scheduled for right now.
      return rightNow;
    }

    // Only one successful test in word stats.
    Pair<Date, Boolean> lastRepetition = wordStats.history.get(wordStats.history.size() - 1);
    if (wordStats.history.size() == 1 && lastRepetition.second) {
      return lastRepetition.first.getTime() + 24 * 60 * ONE_MINUTE;
    }

    // The last test was unsuccessful.
    if (!lastRepetition.second) {
      // Try to test word again regardless of its level to ensure users didn't forget it
      // completely.
      return lastRepetition.first.getTime() + 4 * ONE_MINUTE;
    }

    // We have more than 2 tests for this word and last test was successful.
    return lastRepetition.first.getTime() + getWordMemorizationDelay(wordStats);
  }

  // Calculated as weighted average of historic time spans of 3 previous tests (for unsuccessful
  // test weight is 0).
  private int getWordMemorizationDelay(WordStats wordStats) {
    double retentionApproximation = 0;
    double[] coefs = new double[] {5, 3, 1};
    double[] coefsPartialSums = new double[] {5, 8, 9};
    int history_size = wordStats.history.size();
    for (int i = history_size - 1; i >= Math.max(history_size - 3, 0); i--) {
      Pair<Date, Boolean> wordStat = wordStats.history.get(i);
      if (wordStat.second) {
        long timeBetweenExercises =
            wordStat.first.getTime() - wordStats.history.get(i - 1).first.getTime();
        retentionApproximation += timeBetweenExercises * coefs[history_size - i - 1];
      }
    }
    double normalization = coefsPartialSums[Math.max(history_size - 1, 2)] / 2.5;
    return (int)(retentionApproximation/normalization);
  }

  @Override
  public int compare(String word1, String word2) {
    if (word1.equals(word2)) {
      return 0;
    }
    WordStats wordStats1 = learningManager.getStats(word1);
    WordStats wordStats2 = learningManager.getStats(word2);
    long when1 = whenToLearn(wordStats1);
    long when2 = whenToLearn(wordStats2);
    if (when1 == when2) {
      // Compare priority according to the original list.
      return
          learningManager.getPositionInUsageFrequencyList(word1) <
              learningManager.getPositionInUsageFrequencyList(word2)
          ? -1 : 1;
    }
    return when1 < when2 ? -1 : 1;
  }
}
