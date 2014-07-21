package com.english.englishwords.app.pojo;

/**
 * Created by bogdank on 4/6/14.
 */
public class Exercise {
  private Word word;
  private String question;
  private Word[] optionWords;
  private String[] options;
  private int correctOption;

  public Word getLearningWord() {
    return word;
  }

  public void setLearningWord(Word word) {
    this.word = word;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public Word[] getOptionWords() {
    return optionWords;
  }

  public void setOptionWords(Word[] relatedWords) {
    this.optionWords = relatedWords;
    // update their definitions
    options = new String[relatedWords.length];
    for (int i = 0; i < relatedWords.length; ++i) {
      // TODO(Bogdan) fail here
      if (relatedWords[i].getSenses().size() == 0) {
        options[i] = "Definition not found: " + relatedWords[i].getWord();
      } else {
        options[i] = relatedWords[i]
            .getSenses().get(0).getDefinition();
      }
    }
  }

  public String[] getOptions() {
    return options;
  }

  public void setOptions(String[] options) {
    this.options = options;
  }

  public int getCorrectOption() {
    return correctOption;
  }

  public void setCorrectOption(int correctOption) {
    this.correctOption = correctOption;
  }
}
