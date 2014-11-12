package com.english.englishwords.app.test.learning_manager;

import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.WordStats;

import java.util.List;

class DummyWordStatsDAO implements WordStatsDAO {
  private final List<WordStats> stats;

  DummyWordStatsDAO(List<WordStats> stats) {
    this.stats = stats;
  }

  @Override
  public void update(WordStats newWordStats) {
    getStats(newWordStats.word).copyFrom(newWordStats);
  }

  @Override
  public WordStats getStats(String word) {
    for (WordStats wordStats : stats) {
      if (wordStats.word.equals(word)) {
        return wordStats;
      }
    }
    // If stats doesn't have it - create empty and add it to stats.
    WordStats wordStats = new WordStats(word);
    stats.add(wordStats);
    return wordStats;
  }
}
