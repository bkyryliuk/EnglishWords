package com.english.englishwords.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bogdank on 4/26/14.
 */
public class WordSense implements Serializable {
  private String word;
  private String definition;
  private ArrayList<String> examples;
  private ArrayList<WordValue> synonyms;

  public WordSense(String word, String definition, ArrayList<String> examples,
                   ArrayList<WordValue> synonyms) {
    this.word = word;

    this.definition = definition;
    this.examples = new ArrayList<String>(examples);
    this.synonyms = new ArrayList<WordValue>(synonyms);
  }

  public String getDefinition() {
    return definition;
  }

  public String getConcatenatedExamples() {
    String concatenatedExamples = "";
    for (String example : examples) {
      concatenatedExamples += example;
      concatenatedExamples += "\n";
    }
    return concatenatedExamples;
  }

  public String getConcatenatedSynonyms() {
    String concatenatedExamples = "";
    for (WordValue synonym : synonyms) {
      concatenatedExamples += synonym.getWord();
      if (!synonym.getWord().equals(synonyms.get(synonyms.size() - 1).getWord())) {
        concatenatedExamples += ", ";
      }
    }
    return concatenatedExamples;
  }

  public ArrayList<WordValue> getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(ArrayList<WordValue> synonyms) {
    this.synonyms = new ArrayList<WordValue>(synonyms);
  }

  public String getWord() {
    return word;
  }
}
