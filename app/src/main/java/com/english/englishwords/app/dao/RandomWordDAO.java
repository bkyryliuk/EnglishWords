package com.english.englishwords.app.dao;

import com.english.englishwords.app.data_model.LearningManager;
import com.english.englishwords.app.data_model.Word;
import com.english.englishwords.app.data_model.WordSense;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by rage on 11.05.14.
 */
public class RandomWordDAO implements WordDAO {
  @Override
  public Word getWord(String word) {
    Word wordValue = GenerateRandomWord(word);
    Random r = new Random();
    List<String> synonyms = new ArrayList<String>();
    for (int j = 0; j < r.nextInt(5); ++j) {
      synonyms.add(LearningManager.getInstance().getWordsInProgress()[new Random().nextInt(10000)]);
    }
    wordValue.getSenses().get(0).setSynonyms(synonyms);
    return wordValue;
  }

  private Word GenerateRandomWord(String word) {
    String definition = "Definition for " + word;
    ArrayList<String> examples = new ArrayList<String>();
    for (int j = 0; j < 6; ++j) {
      examples.add("Usage example for " + word);
    }

    ArrayList<WordSense> wordSenses = new ArrayList<WordSense>();
    wordSenses.add(new WordSense(word, definition, examples, new ArrayList<String>()));
    return new Word(word, wordSenses);
  }
}
