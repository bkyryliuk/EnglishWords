package com.english.englishwords.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bogdank on 4/6/14.
 */
public class WordValue implements Serializable {
  private String word;
  private double learningPriority;
  private ArrayList<WordSense> senses;

  //TODO(krasikov): Let's uncomment this when we decide to use this data.
  // for first 5k we may use translations instead of definitions
  // the structure may be updated
  // HashMap<LangCode, Translation>
//  private HashMap<String, String> translations;
//  private HashMap<String, String>[] exampleTranslations;


  public WordValue(String word) {
    this.word = word;
    this.senses = new ArrayList<WordSense>();
  }

  public WordValue(String word, ArrayList<WordSense> senses) {
    this.word = word;
    // TODO(krasikov): do we need copy here?
    this.senses = new ArrayList<WordSense>(senses);
  }

  public String getWord() {
    return word;
  }


  public double getLearningPriority() {
    return learningPriority;
  }

  public void setLearningPriority(double learningPriority) {
    this.learningPriority = learningPriority;
  }

  public ArrayList<WordSense> getSenses() {
    return senses;
  }

  public boolean IsSynonymOf(String word) {
    for (WordSense sense : getSenses()) {
      for (WordValue synonym : sense.getSynonyms()) {
        if (word.equals(synonym.getWord())) {
          return true;
        }
      }
    }
    return false;
  }
}
