package com.english.englishwords.app.pojo;

import java.util.ArrayList;

/**
 * Created by bogdank on 4/26/14.
 */
public class WordQueue {
  // TODO(krasikov): consider joining this list with unknownWords.
  // list of words that were shown to a student at least once
  private ArrayList<WordValue> wordsInProgress;
  // words weren't shown to a student
  private ArrayList<String> unknownWords;
  // words that were learned by a student
  private ArrayList<String> learnedWords;

  //TODO(krasikov): init the word queue from res/original_word_order.txt or from previous application runs.
  public WordQueue(ArrayList<WordValue> wordsInProgress, ArrayList<String> learnedWords,
                   ArrayList<String> unknownWords) {
    this.wordsInProgress = new ArrayList<WordValue>(wordsInProgress);
    this.learnedWords = new ArrayList<String>(learnedWords);
    this.unknownWords = new ArrayList<String>(unknownWords);
  }

  public void learnWord() {
    // TODO add logic of moving the word from the wordsInProgress to the learnedWords
    // and adding new word from unknownWords to the wordsInProgress
  }

  /**
   * @return the WordValue with the highest priority value
   */
  public WordValue getWordToLearn() {
    // TODO
    return null;
  }

  public void updateWordPriority(WordValue wordValue, double priority) {
    // TODO
  }

  public void recalculateWordPriorities() {

  }


}
