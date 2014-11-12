package com.english.englishwords.app.dao;

import com.english.englishwords.app.data_model.WordStats;

import java.util.List;

public interface WordStatsDAO {
  void update(WordStats wordStats);

  WordStats getStats(String word);
}
