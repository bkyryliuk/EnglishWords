package com.english.englishwords.app.excercise_providers;

import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.data_model.WordStats;

//TODO(krasikov): add SynonymExerciseProvider and RootExerciseProvider (it will combine
// SynonymExerciseProvider and DefinitionExerciseProvider).
public abstract class ExerciseProvider {
  protected final WordDAO wordDao;
  protected final WordStatsDAO wordStatsDAO;

  public ExerciseProvider(WordStatsDAO wordStatsDAO, WordDAO wordDao) {
    this.wordStatsDAO = wordStatsDAO;
    this.wordDao = wordDao;
  }

  public abstract Exercise generateExerciseForWord(String wordToLearn);

  public boolean onAnswerGiven(int position, Exercise exercise) {
    WordStats wordStats = wordStatsDAO.getStats(exercise.getLearningWord().getWord());
    boolean success = position != exercise.getCorrectOption();
    wordStats.addEntry(success);
    wordStatsDAO.update(wordStats);
    return success ;
  }
}
