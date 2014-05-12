package com.english.englishwords.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdank on 4/26/14.
 */
public class WordSense implements Serializable {
  private String word;
  private String definition;
  private List<String> examples;
  private List<String> synonyms;

  public WordSense(String word, String definition, ArrayList<String> examples,
                   ArrayList<String> synonyms) {
    this.word = word;

    this.definition = definition;
    this.examples = new ArrayList<String>(examples);
    this.synonyms = new ArrayList<String>(synonyms);
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
    for (String synonym : synonyms) {
      concatenatedExamples += synonym;
      if (!synonym.equals(synonyms.get(synonyms.size() - 1))) {
        concatenatedExamples += ", ";
      }
    }
    return concatenatedExamples;
  }

  public List<String> getSynonyms() {
    return synonyms;
  }

  public void setSynonyms(List<String> synonyms) {
    this.synonyms = new ArrayList<String>(synonyms);
  }

  public String getWord() {
    return word;
  }
}
