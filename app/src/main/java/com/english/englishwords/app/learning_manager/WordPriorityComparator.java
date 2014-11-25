package com.english.englishwords.app.learning_manager;

import android.util.Log;
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

  // Algo description:
  // https://docs.google.com/document/d/1DnkMy293buvIxM4AfJgHH7xy4ckPNZrSvTY3pcMAIWA/edit#
  private long whenToLearn(WordStats wordStats) {
    long rightNow = new Date().getTime();

    // New word (no tests in history).
    if (wordStats.history.size() == 0) {
      // All new words should be scheduled for right now.
      return rightNow;
    }

    // Check if all exercises (even the first one) were successful - maybe users knows the word.
    Pair<Long, Boolean> lastRepetition = wordStats.history.get(wordStats.history.size() - 1);
    if (wordStats.allExercisesAreSuccessful()) {
      if (wordStats.history.size() == 1) {
        return lastRepetition.first + 6 * DAY;
      }
      else if (wordStats.history.size() == 2) {
        return lastRepetition.first + 21 * DAY;
      } else {
        Log.w(
            getClass().getCanonicalName(),
            "There can be no word with more than 2 total exercises all of which are successful. " +
                "Successful exercises: " + wordStats.history.size());
      }
    }

    // Last exercise wasn't successful.
    if (!lastRepetition.second) {
      // Try to test word ASAP regardless of its level to ensure users didn't forget it completely.
      return lastRepetition.first + 4 * MINUTE;
    }

    // At this point there are 2+ exercises in history.
    if (wordStats.history.size() < 2) {
      throw new IllegalStateException("History size can't be less then 2 events at this point");
    }

    // TODO(krasikov): take into account word complexity
    // Here http://www.supermemo.com/english/ol/sm2.htm word distance between successful repetitions
    // is in [1.3 .. 2.5] depending on word complexity.
    // Besides word length complexity can be determined on time spend answering exercise.

    return lastRepetition.first + getNextExerciseDelay(wordStats);
  }

  // Calculated as weighted average of historic time spans of 3 previous tests (for unsuccessful
  // test weight is 0).
  private long getNextExerciseDelay(WordStats wordStats) {
    int history_size = wordStats.history.size() - 1;
    long timespan =
        wordStats.history.get(history_size).first - wordStats.history.get(history_size - 1).first;
    // TODO(krasikov): maybe add 2 hours delay for last 5 min successful exercise.
    if (timespan < 8 * HOUR) {
      return DAY;
    } else if (timespan < 3 * DAY) {
      return 6 * DAY;
    } else if (timespan < 6 * DAY) {
      return 12 * DAY;
    } else if (timespan < 12 * DAY) {
      return 24 * DAY;
    } else if (timespan < 24 * DAY) {
      return 48 * DAY;
    } else if (timespan < 48 * DAY) {
      return 96 * DAY;
    }
    Log.w(
        getClass().getCanonicalName(),
        "There can be no unlearned word which has successful exercise after more than 48 days " +
            "after another successful exercise. Consider it as learned. Word: " + wordStats.word);
    return 96 * DAY;
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
