package com.english.englishwords.app.learning_manager;

import com.english.englishwords.app.data_model.WordStats;

public interface WordStatsRepository {
  WordStats getStats(String word);
}
