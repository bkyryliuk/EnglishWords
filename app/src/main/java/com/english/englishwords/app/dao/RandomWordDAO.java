package com.english.englishwords.app.dao;

import com.english.englishwords.app.pojo.WordSense;
import com.english.englishwords.app.pojo.WordValue;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by rage on 11.05.14.
 */
public class RandomWordDAO implements WordDAO {
  @Override
  public WordValue getWord(String word) {
    WordValue wordValue = GenerateRandomWord(word);
    Random r = new Random();
    ArrayList<WordValue> synonyms = new ArrayList<WordValue>();
    for (int j = 0; j < r.nextInt(5); ++j) {
      synonyms.add(GenerateRandomWord(Integer.toString(Integer.parseInt(word) + j + 1)));
    }
    wordValue.getSenses().get(0).setSynonyms(synonyms);
    return wordValue;
  }

  private WordValue GenerateRandomWord(String i) {
    Random r = new Random();
    String definition = "Word definition number " + i;
    String word = "Word number " + i;
    ArrayList<String> examples = new ArrayList<String>();
    for (int j = 0; j < 6; ++j) {
      examples.add("Word usage example number " + Integer.toString(j) + " for "
          + word);
    }

    WordSense wordSense = new WordSense(word, definition, examples, new ArrayList<WordValue>());
    ArrayList<WordSense> wordSenses = new ArrayList<WordSense>();
    wordSenses.add(wordSense);
    WordValue wordValue = new WordValue(word, wordSenses);

    return wordValue;
  }
}
