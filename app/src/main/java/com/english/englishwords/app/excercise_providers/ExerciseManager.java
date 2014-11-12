package com.english.englishwords.app.excercise_providers;

import android.util.Log;

import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.learning_manager.LearningManager;
import com.english.englishwords.app.data_model.Word;

import java.util.Random;

//TODO(krasikov): add SynonymExerciseManager and RootExerciseManager (it will combine
// SynonymExerciseManager and DefinitionExerciseManager).
public abstract class ExerciseManager {
  protected final LearningManager learningManager;

  public ExerciseManager(LearningManager learningManager) {
    this.learningManager = learningManager;
  }

  public abstract Exercise generateExerciseForWord(String wordToLearn);

  public boolean onAnswerGiven(int position, Exercise exercise) {
    Log.d("1", "in onAnswerGiven");
    boolean success = position == exercise.getCorrectOption();
    learningManager.memorizeExerciseResult(exercise.getLearningWord().getWord(), success);
    return success;
  }

  void moveCorrectOptionToRandomPosition(Exercise exercise) {
    Random random = new Random();
    exercise.setCorrectOption(random.nextInt(exercise.getOptionWords().length));

    Word tmpWord = exercise.getOptionWords()[exercise.getCorrectOption()];
    exercise.getOptionWords()[exercise.getCorrectOption()] = exercise.getOptionWords()[0];
    exercise.getOptionWords()[0] = tmpWord;

    String tmpOption = exercise.getOptions()[exercise.getCorrectOption()];
    exercise.getOptions()[exercise.getCorrectOption()] = exercise.getOptions()[0];
    exercise.getOptions()[0] = tmpOption;
  }
}
