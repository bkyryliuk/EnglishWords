package com.english.englishwords.app.excerciseproviders;

import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.WordValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bogdank on 4/6/14.
 */
// TODO(krasikov): make this class work with real words, not numbers instead of words (use WordQueue)
public class DefinitionExerciseProvider implements ExerciseProvider {
  private WordDAO wordDao;

  public DefinitionExerciseProvider(WordDAO wordDao) {
    this.wordDao = wordDao;
  }

  public Exercise getExerciseForWord(String wordToLearn) {
    Exercise exercise = new Exercise();
    exercise.setLearningWord(wordDao.getWord(wordToLearn));
    exercise.setQuestion(exercise.getLearningWord().getWord());
    exercise.setOptionWords(GetOptions(exercise.getLearningWord(), 6));

    List<String> options = new ArrayList<String>();
    for (WordValue option : exercise.getOptionWords()) {
      options.add(option.getSenses().get(0).getDefinition());
    }
    exercise.setOptions(options.toArray(new String[0]));

    setRandomRightAnswer(exercise);
    return exercise;
  }

  public void setRandomRightAnswer(Exercise exercise) {
    Random random = new Random();
    exercise.setRightAnswer(random.nextInt(exercise.getOptionWords().length));
    exercise.getOptionWords()[exercise.getRightAnswer()] = exercise.getLearningWord();
    exercise.getOptions()[exercise.getRightAnswer()] =
        exercise.getLearningWord().getSenses().get(0).getDefinition();

  }

  private WordValue[] GetOptions(WordValue wordValue, int optionsNumber) {
    WordValue[] optionWords = new WordValue[optionsNumber];
    // TODO(krasikov): make this method to work with real words, not numbers instead of words (use
    // WordQueue).
    int optionCandidateWord = 0;
    optionWords[0] = wordValue;
    for (int i = 1; i < optionsNumber; ++i) {
      WordValue optionCandidateWordValue = wordDao.getWord(Integer.toString(optionCandidateWord));
      while (Integer.toString(optionCandidateWord).equals(wordValue.getWord()) ||
             optionCandidateWordValue.IsSynonymOf(wordValue.getWord())) {
        ++optionCandidateWord;
        optionCandidateWordValue = wordDao.getWord(Integer.toString(optionCandidateWord));
      }

      optionWords[i] = optionCandidateWordValue;
      ++optionCandidateWord;
    }

    return optionWords;
  }
}
