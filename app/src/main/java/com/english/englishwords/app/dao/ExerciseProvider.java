package com.english.englishwords.app.dao;

import android.content.Intent;

import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.WordSense;

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
      exercise.setRelatedWordSenses(GetRelatedWordSenses(exercise.getWordSense(), 6, counter));
      exercise.setRandomRightAnswer();
      return exercise;
    }

    // TODO to be replaced. Just mock function
    private WordSense GenerateRandomWordSense(int i) {
        WordSense wordSense = new WordSense();
        Random r = new Random();
        wordSense.setDefinition("Word definition number " + Integer.toString(i));
        wordSense.setWord("Word number " + Integer.toString(i));
        String[] examples = new String[r.nextInt(4) + 1];
        for (int j = 0; j < examples.length; ++j) {
            examples[j] = "Word usage example number " + Integer.toString(j) + " for "
                    + wordSense.getWord();
        }
        wordSense.setExamples(examples);
        return wordSense;
    }

    // TODO to be replaced. Just mock function
    private WordSense GenerateRandomWordSenseWithSynonyms(int i) {
        WordSense wordSense = GenerateRandomWordSense(i);
        Random r = new Random();
        WordSense[] synonyms = new WordSense[r.nextInt(5)];
        for (int j = 0; j < synonyms.length; ++j) {
            synonyms[j] = GenerateRandomWordSense(i+j+1);
        }
        wordSense.setSynonyms(synonyms);
        return wordSense;
    }

    // May be reused
    private boolean WordIsInWordSynonyms(String word, WordSense wordSense) {
        for (WordSense sense: wordSense.getSynonyms()) {
            if (word.equals(sense.getWord())) {
                return true;
            }
        }
        return false;
    }

    // TODO to be replaced. Just mock function
    private WordSense[] GetRelatedWordSenses(WordSense wordSense, int n, int wordNumber) {
        WordSense[] relatedWordSenses = new WordSense[n];
        int relatedWordCounter = wordNumber - n;
        if (relatedWordCounter < 0) {
            relatedWordCounter = 0;
        }
        for (int i = 0; i< n; ++i) {
            WordSense relatedWordSense = GenerateRandomWordSenseWithSynonyms(relatedWordCounter);
            while (WordIsInWordSynonyms(wordSense.getWord(), relatedWordSense)) {
                ++relatedWordCounter;
                if (relatedWordCounter == wordNumber) {
                    continue;
                }
                relatedWordSense = GenerateRandomWordSenseWithSynonyms(relatedWordCounter);
            }
            relatedWordSenses[i] = relatedWordSense;
            ++relatedWordCounter;
            if (relatedWordCounter == wordNumber) {
                continue;
            }
        }
        return relatedWordSenses;
    }
}
