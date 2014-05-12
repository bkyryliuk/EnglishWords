package com.english.englishwords.app.excerciseproviders;

import android.util.Log;

import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.Word;

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
    exercise.setOptionWords(GenerateOptions(exercise.getLearningWord(), 6));

    List<String> options = new ArrayList<String>();
    for (Word option : exercise.getOptionWords()) {
      // TODO(krasikov): use other than first sense.
      options.add(option.getSenses().get(0).getDefinition());
    }
    exercise.setOptions(options.toArray(new String[0]));

    // Originally correct answer is at position 0.
    moveCorrectOptionToRandomPosition(exercise);

    return exercise;
  }

  public void moveCorrectOptionToRandomPosition(Exercise exercise) {
    Random random = new Random();
    exercise.setCorrectOption(random.nextInt(exercise.getOptionWords().length));

    Word tmpWord = exercise.getOptionWords()[exercise.getCorrectOption()];
    exercise.getOptionWords()[exercise.getCorrectOption()] = exercise.getOptionWords()[0];
    exercise.getOptionWords()[0] = tmpWord;

    String tmpOption = exercise.getOptions()[exercise.getCorrectOption()];
    exercise.getOptions()[exercise.getCorrectOption()] = exercise.getOptions()[0];
    exercise.getOptions()[0] = tmpOption;
  }

  // TODO(krasikov): make this method to work with real words, not numbers instead of words (use
  // WordQueue).
  private Word[] GenerateOptions(Word word, int optionsNumber) {
    Word[] optionWords = new Word[optionsNumber];
    // Later correct option will be moved to a random position.
    optionWords[0] = word;
    int candidateOptionRawWord = -1;
    for (int i = 1; i < optionsNumber; ++i) {
      Word candidateOptionWord = wordDao.getWord(Integer.toString(++candidateOptionRawWord));
//      Log.e(getClass().getSimpleName(), "start with id:" + candidateOptionRawWord + "," +
//          candidateOptionRawWord + "," + word.getWord() + "," + Integer.toString(candidateOptionRawWord).equals(word.getWord()));
      while (("Word number " + Integer.toString(candidateOptionRawWord)).equals(word.getWord()) ||
             candidateOptionWord.IsSynonymOf(word.getWord())) {
        candidateOptionWord = wordDao.getWord(Integer.toString(++candidateOptionRawWord));
      }

      optionWords[i] = candidateOptionWord;
    }
    return optionWords;
  }
}
