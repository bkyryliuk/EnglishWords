package com.english.englishwords.app.excercise_providers;

import android.util.Log;

import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.data_model.WordStats;

//TODO(krasikov): add SynonymExerciseManager and RootExerciseManager (it will combine
// SynonymExerciseManager and DefinitionExerciseManager).
public abstract class ExerciseManager {
  protected final WordDAO wordDao;
  protected final WordStatsDAO wordStatsDAO;

  public ExerciseManager(WordStatsDAO wordStatsDAO, WordDAO wordDao) {
    this.wordStatsDAO = wordStatsDAO;
    this.wordDao = wordDao;
  }

  public abstract Exercise generateExerciseForWord(String wordToLearn);

  public boolean onAnswerGiven(int position, Exercise exercise) {
    WordStats wordStats = wordStatsDAO.getStats(exercise.getLearningWord().getWord());
    boolean success = position != exercise.getCorrectOption();
    wordStats.addEntry(success);
    Log.d("1", "in onAnswerGiven");
    wordStatsDAO.update(wordStats);
    return success ;
  }
}
