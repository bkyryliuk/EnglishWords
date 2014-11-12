package com.english.englishwords.app.excercise_providers;

import com.english.englishwords.app.algo.DistanceWordSelector;
import com.english.englishwords.app.algo.LevenshteinDistanceCalculator;
import com.english.englishwords.app.algo.WordSelector;
import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.data_model.Word;
import com.english.englishwords.app.data_model.LearningManager;

import java.util.ArrayList;
import java.util.List;

public class DefinitionExerciseManager extends ExerciseManager {
  private static final int DEFINITION_EXERCISE_OPTIONS_NUM = 6;

  protected final WordDAO wordDao;

  public DefinitionExerciseManager(WordDAO wordDao, LearningManager learningManager) {
    super(learningManager);
    this.wordDao = wordDao;
  }

  public Exercise generateExerciseForWord(String wordToLearn) {
    // TODO(Bogdan) make some sort of mixed selector to be used here
    // (pick related words based on multiple factors)
    WordSelector wordSelector = new DistanceWordSelector(
        new LevenshteinDistanceCalculator());
    // TODO(Bogdan) pass here actual word instead of empty one just with the name
    List<String> relatedWords = wordSelector.SelectNClosestWords(
        wordDao.getWord(wordToLearn).getWord(),
        LearningManager.getInstance().getWordsInProgress(), DEFINITION_EXERCISE_OPTIONS_NUM);

    Exercise exercise = new Exercise();
    exercise.setLearningWord(wordDao.getWord(wordToLearn));
    exercise.setQuestion(exercise.getLearningWord().getWord());
    exercise.setOptionWords(generateOptions(exercise.getLearningWord(), relatedWords));

    List<String> options = new ArrayList<String>();
    for (Word option : exercise.getOptionWords()) {
      // TODO(krasikov): use other than first sense.
      // TODO(Bogdan): fail here
      if (option.getSenses().size() == 0) {
        options.add("Definition not found: " + option.getWord());
      } else {
        options.add(option.getSenses().get(0).getDefinition());
      }
    }
    exercise.setOptions(options.toArray(new String[0]));

    // Originally correct answer is at position 0.
    moveCorrectOptionToRandomPosition(exercise);

    return exercise;
  }

  private Word[] generateOptions(Word word, List<String> relatedWords) {
    Word[] optionWords = new Word[DEFINITION_EXERCISE_OPTIONS_NUM];
    // Later correct option will be moved to a random position.
    optionWords[0] = word;
    for (int i = 1; i < DEFINITION_EXERCISE_OPTIONS_NUM; ++i) {
      optionWords[i] = wordDao.getWord(relatedWords.get(i));
    }
    return optionWords;
  }
}