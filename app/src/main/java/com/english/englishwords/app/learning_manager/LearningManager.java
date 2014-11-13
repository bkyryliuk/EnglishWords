package com.english.englishwords.app.learning_manager;

import android.util.Log;

import com.english.englishwords.app.dao.WordListsDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.WordStats;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class LearningManager {
  private static LearningManager instance = null;

  // Words that weren't yet learned or weren't even show to the user.
  private PriorityQueue<String> wordsInProgress;

  private WordListsDAO wordListsDAO;

  private WordStatsDAO wordStatsDAO;

  private LearningManager(WordListsDAO wordListsDAO, WordStatsDAO wordStatsDAO) {
    this.wordStatsDAO = wordStatsDAO;
    this.wordListsDAO = wordListsDAO;

    List<String> wordOrderByUsage = wordListsDAO.getWordOrderByUsage();
    wordsInProgress = new PriorityQueue<String>(
        wordOrderByUsage.size(), new WordPriorityComparator(wordListsDAO, wordStatsDAO));
    for (String word : wordOrderByUsage) {
      if (!wordListsDAO.getLearnedWords().contains(word)) {
        wordsInProgress.add(word);
      }
    }
  }

  // Inits the word queue from res/words_sorted_by_usage.txt or from previous application runs.
  public static void initialize(WordStatsDAO wordStatsDAO, WordListsDAO wordListsDAO) {
    if (instance == null) {
      instance = new LearningManager(wordListsDAO, wordStatsDAO);
    }
  }

  // TODO(krasikov): can we convert this class to a non-singleton?
  public static LearningManager getInstance() {
    return instance;
  }

  public WordListsDAO getWordListsDAO() {
    return wordListsDAO;
  }

  public WordStatsDAO getWordStatsDAO() {
    return wordStatsDAO;
  }

  public String popWord() {
    return wordsInProgress.poll();
  }

  public void addWord(String word) {
    wordsInProgress.add(word);
  }

  public String[] getWordsInProgress() {
    // TODO(krasikov): maybe rewrite this to be real words in progress i.e. remove words which
    // wasn't yet show.
    return wordsInProgress.toArray(new String[0]);
  }

  public int getLearnedWordsNum() {
    return wordListsDAO.getLearnedWords().size();
  }

  public void pretendUserLearnedFirstNWords(int learnedWordsNum) {
    Log.e(getClass().getCanonicalName(),
        "setting learned words to " + Integer.toString(learnedWordsNum));

    wordListsDAO.saveLearnedWords(
        new HashSet<String>(wordListsDAO.getWordOrderByUsage().subList(0, learnedWordsNum)));
    for (String learnedWord : wordListsDAO.getLearnedWords()) {
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
