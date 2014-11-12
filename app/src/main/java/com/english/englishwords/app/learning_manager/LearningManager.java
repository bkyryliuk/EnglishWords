package com.english.englishwords.app.learning_manager;

import android.util.Log;

import com.english.englishwords.app.dao.FileWordListsDAO;
import com.english.englishwords.app.dao.WordListsDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.WordStats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class LearningManager implements WordStatsRepository {
  private static LearningManager instance = null;

  // Words that weren't yet learned or weren't even show to the user.
  private PriorityQueue<String> wordsInProgress;

  // Words that considered to be learned.
  private Set<String> learnedWords;

  private List<String> originalWordList;

  // In-memory copy of all WordsStats stored by WordStatsDAO. It is used to rank words by learning
  // priority.
  private Hashtable<String, WordStats> wordsStats;

  private WordListsDAO wordListsDAO;

  private WordStatsDAO wordStatsDAO;
  private Hashtable<String, Integer> originalWordListToPos;

  // Inits the word queue from res/original_word_order.txt or from previous application runs.
  public static void initialize(WordStatsDAO wordStatsDAO, FileWordListsDAO wordListsDAO) {
    if (instance == null) {
      instance = new LearningManager(wordListsDAO, wordStatsDAO);
    }
  }

  // TODO(krasikov): this class probably should be converted to a non-singleton class.
  public static LearningManager getInstance() {
    return instance;
  }

  private LearningManager(WordListsDAO wordListsDAO, WordStatsDAO wordStatsDAO) {
    this.wordStatsDAO = wordStatsDAO;
    this.wordListsDAO = wordListsDAO;

    initializeWordStats();
    initializeOriginalWordList();
    initializeLearnedWords();
    initializeWordsInProgress();
  }

  private void initializeWordStats() {
    wordsStats = new Hashtable<String, WordStats>();
    for (WordStats stats : this.wordStatsDAO.getStatsForAllWords()) {
      wordsStats.put(stats.word, stats);
    }
  }

  private void initializeOriginalWordList() {
    try {
      originalWordList = readWordListFromInput(wordListsDAO.getOriginalWordOrderStream());
      originalWordListToPos = new Hashtable<String, Integer>(originalWordList.size());
      for (int i = 0; i < originalWordList.size(); i++) {
        originalWordListToPos.put(originalWordList.get(i), i);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initializeLearnedWords() {
    try {
      learnedWords = new HashSet<String>(
          readWordListFromInput(wordListsDAO.getLearnedWordsStream()));
    } catch (FileNotFoundException e) { // TODO(krasikov): don't rely on FileNotFoundException.
      // Probably first application run - create empty learnedWords.txt.
      Log.d(this.getClass().getCanonicalName(), "learnedWords.txt is missing");
      learnedWords = new HashSet<String>();
      wordListsDAO.saveLearnedWords(learnedWords);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initializeWordsInProgress() {
    wordsInProgress =
        new PriorityQueue<String>(originalWordList.size(), new WordPriorityComparator(this));
    for (String word : originalWordList) {
      if (!learnedWords.contains(word)) {
        wordsInProgress.add(word);
      }
    }
  }

  private static ArrayList<String> readWordListFromInput(InputStream input) throws IOException {
    ArrayList<String> wordList = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String word;

    while ((word = reader.readLine()) != null) {
      wordList.add(word.trim());
    }

    input.close();
    return wordList;
  }

  // TODO(krasikov): move this to WordListDAO maybe.
  public int getPositionInOriginalWordList(String word) {
    return originalWordListToPos.get(word);
  }

  public String popWord() { return wordsInProgress.poll(); }

  public void addWord(String word) { wordsInProgress.add(word); }

  public String[] getWordsInProgress() {
    // TODO(krasikov): maybe rewrite this to be real words in progress i.e. remove words which
    // wasn't yet show.
    return wordsInProgress.toArray(new String[0]);
  }

  @Override
  public WordStats getStats(String word) {
    WordStats wordStats = wordsStats.get(word);
    if (wordStats == null) {
      wordStats = new WordStats(word);
    }
    return wordStats;
  }

  public int getLearnedWordsNum() {
    return learnedWords.size();
  }

  public void pretendUserLearnedFirstNWords(int learnedWordsNum) {
    Log.e(getClass().getCanonicalName(), "setting " + Integer.toString(learnedWordsNum));
    Log.e(getClass().getCanonicalName(), "learned words " + Integer.toString(learnedWords.size()));

    learnedWords = new HashSet<String>(originalWordList.subList(0, learnedWordsNum));
    wordListsDAO.saveLearnedWords(learnedWords);
    for (String learnedWord : learnedWords) {
      wordsInProgress.remove(learnedWord);
    }
  }

// NOTE(krasikov): keep it here - most likely we will use it.
//  public int accomplishedExercisesNum() {
//    int exercisesNum = 0;
//    Enumeration<WordStats> elements = wordsStats.elements();
//    while (elements.hasMoreElements()) {
//      exercisesNum += elements.nextElement().history.size();
//    }
//    return exercisesNum;
//  }

  public void memorizeExerciseResult(String word, boolean success) {
    WordStats wordStats = wordStatsDAO.getStats(word);
    wordStats.addEntry(success);
    wordStatsDAO.update(wordStats);
  }

}
