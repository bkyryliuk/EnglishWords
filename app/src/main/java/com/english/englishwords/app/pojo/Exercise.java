package com.english.englishwords.app.pojo;

import java.util.List;

/**
 * Created by bogdank on 4/6/14.
 */
public class Exercise {
  public WordValue getLearningWord() {
    return word;
  }

  public void setLearningWord(WordValue word) {
    this.word = word;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public WordValue[] getOptionWords() {
    return optionWords;
  }

  public void setOptionWords(WordValue[] relatedWordValues) {
    this.optionWords = relatedWordValues;
    // update their definitions
    options = new String[relatedWordValues.length];
    for (int i = 0; i < relatedWordValues.length; ++i) {
      options[i] = relatedWordValues[i]
          .getSenses().get(0).getDefinition();
    }
  }

  public void setOptions(String[] options) {
    this.options = options;
  }

  public String[] getOptions() {
    return options;
  }

  public int getRightAnswer() {
    return rightAnswer;
  }

  public void setRightAnswer(int rightAnswer) {
    this.rightAnswer = rightAnswer;
  }

  private WordValue word;
  private String question;
  private WordValue[] optionWords;
  private String[] options;
  private int rightAnswer;

}
