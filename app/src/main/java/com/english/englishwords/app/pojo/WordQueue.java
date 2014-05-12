package com.english.englishwords.app.pojo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdank on 4/26/14.
 */
// This class is singleton.
public class WordQueue {
  private static WordQueue instance = null;

  // Inits the word queue from res/original_word_order.txt or from previous application runs.
  public static void initialize(Context context) {
    WordQueue.instance = new WordQueue();
    try {
      WordQueue.instance.wordsInProgress =
          readWordListFromFile(context.openFileInput("wordsInProgress.txt"));
      WordQueue.instance.unknownWords =
          readWordListFromFile(context.openFileInput("unknownWords.txt"));
      WordQueue.instance.learnedWords =
          readWordListFromFile(context.openFileInput("learnedWords.txt"));
    } catch (FileNotFoundException e) {
      initializeFirstApplicationRun(context);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ArrayList<String> readWordListFromFile(FileInputStream wordlistFile) throws IOException {
    ArrayList<String> wordlist = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(wordlistFile));
    String word;

    while ((word = reader.readLine()) != null) {
      wordlist.add(word.trim());
    }

    wordlistFile.close();
    return wordlist;
  }

  private static void initializeFirstApplicationRun(Context context) {
    try {
      WordQueue.instance.unknownWords =
          readWordListFromFile(context.openFileInput("original_word_order.txt"));
      WordQueue.instance.wordsInProgress = new ArrayList<String>();
      WordQueue.instance.learnedWords = new ArrayList<String>();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //TODO(bogdank): call vocabulary size estimation activity.
  }

  public static WordQueue getInstance() {
    return instance;
  }

  // TODO(krasikov): consider joining this list with unknownWords.
  // Words that were shown to a student at least once.
  private List<String> wordsInProgress;
  // Words weren't shown to a student.
  private List<String> unknownWords;
  // Words that were learned by a student.
  private List<String> learnedWords;

  public List<String> getWordsInProgress() {
    return wordsInProgress;
  }

  public List<String> getUnknownWords() {
    return unknownWords;
  }

  public List<String> getLearnedWords() {
    return learnedWords;
  }

  private WordQueue() {
  }

  public void learnWord() {
    // TODO add logic of moving the word from the wordsInProgress to the learnedWords
    // and adding new word from unknownWords to the wordsInProgress
  }

  /**
   * @return the Word with the highest priority value
   */
  public Word getWordToLearn() {
    // TODO
    return null;
  }

  public void updateWordPriority(Word word, double priority) {
    // TODO
  }

  public void recalculateWordPriorities() {

  }
}
