package com.english.englishwords.app.dao;

import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.WordSense;
import com.english.englishwords.app.pojo.WordValue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by bogdank on 4/6/14.
 */
public class ExerciseProvider {
    private int counter = 0;

    // TODO to be replaced. Just mock function
    public Exercise getNextExercise() {
      ++counter;
      Exercise exercise = new Exercise();
      exercise.setWord(GenerateRandomWordSenseWithSynonyms(counter));
      exercise.setRelatedWordValues(GetRelatedWordSenses(exercise.getWordSense(), 6, counter));
      exercise.setRandomRightAnswer();
      return exercise;
    }

    // TODO to be replaced. Just mock function
    private WordValue GenerateRandomWordSense(int i) {
        Random r = new Random();
        String definition = "Word definition number " + Integer.toString(i);
        String word = "Word number " + Integer.toString(i);
        ArrayList<String> examples = new ArrayList<String>();
        for (int j = 0; j < 6; ++j) {
            examples.add("Word usage example number " + Integer.toString(j) + " for "
                    + word);
        }

        WordSense wordSense = new WordSense(word, definition, examples, new ArrayList<WordValue>());
        ArrayList<WordSense> wordSenses = new ArrayList<WordSense>();
        wordSenses.add(wordSense);
        WordValue wordValue = new WordValue(word, i, wordSenses);

        return wordValue;
    }

    // TODO to be replaced. Just mock function
    private WordValue GenerateRandomWordSenseWithSynonyms(int i) {
        WordValue wordValue = GenerateRandomWordSense(i);
        Random r = new Random();
        ArrayList<WordValue> synonyms = new ArrayList<WordValue>();
        for (int j = 0; j < r.nextInt(5); ++j) {
            synonyms.add(GenerateRandomWordSense(i+j+1));
        }
        wordValue.getSenses().get(0).setSynonyms(synonyms);
        return wordValue;
    }

    // May be reused
    private boolean WordIsInWordSynonyms(String word, WordValue wordValue) {
        for (WordValue sense: wordValue.getSenses().get(0).getSynonyms()) {
            if (word.equals(sense.getWord())) {
                return true;
            }
        }
        return false;
    }

    // TODO to be replaced. Just mock function
    private WordValue[] GetRelatedWordSenses(WordValue wordValue, int n, int wordNumber) {
        WordValue[] relatedWordValues = new WordValue[n];
        int relatedWordCounter = wordNumber - n;
        if (relatedWordCounter < 0) {
            relatedWordCounter = 0;
        }
        for (int i = 0; i< n; ++i) {
            WordValue relatedWordValue = GenerateRandomWordSenseWithSynonyms(relatedWordCounter);
            while (WordIsInWordSynonyms(wordValue.getWord(), relatedWordValue)) {
                ++relatedWordCounter;
                if (relatedWordCounter == wordNumber) {
                    continue;
                }
                relatedWordValue = GenerateRandomWordSenseWithSynonyms(relatedWordCounter);
            }
            relatedWordValues[i] = relatedWordValue;
            ++relatedWordCounter;
            if (relatedWordCounter == wordNumber) {
                continue;
            }
        }
        return relatedWordValues;
    }
}
