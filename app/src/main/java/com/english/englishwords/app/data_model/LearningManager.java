package com.english.englishwords.app.data_model;

import android.content.Context;
import android.util.Log;

import com.english.englishwords.app.InitializationHelper;
import com.english.englishwords.app.dao.WordStatsDAO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;

// TODO(krasikov): This class is singleton - but it will be converted to a regular one soon.
public class LearningManager {
  private static LearningManager instance = null;

  // Used to save learned words to a disk.
  Context context;

  // Words that weren't yet learned or weren't even show to the user.
  private PriorityQueue<String> wordsInProgress;

  // Words that considered to be learned.
  private List<String> learnedWords;

  private WordStatsDAO wordStatsDAO;
  // In-memory copy of all WordsStats stored by WordStatsDAO. It is used to rank words by learning
  // priority.
  private Dictionary<String, WordStats> wordsStats;
  private List<String> original_word_list;

  private LearningManager(Context _context) {
    context = _context;
    initializeWordStats(context);
    initializeOriginalWordList(context);
    initializeLearnedWords(context);
    initializeWordsInProgress();
  }

  // Inits the word queue from res/original_word_order.txt or from previous application runs.
  public static void initialize(Context context) {
   LearningManager.instance = new LearningManager(context);
  }

  private void initializeOriginalWordList(Context context) {
    try {
      original_word_list =
          readWordListFromInput(context.getAssets().open("original_word_order.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initializeWordStats(Context context) {
    wordStatsDAO = new WordStatsDAO(context);
    wordsStats = new Hashtable<String, WordStats>();
    for (WordStats stats : wordStatsDAO.getStatsForAllWords()) {
      wordsStats.put(stats.word, stats);
    }
  }

  private void initializeLearnedWords(Context context) {
    try {
      learnedWords = readWordListFromInput(context.openFileInput("learnedWords.txt"));
    } catch (FileNotFoundException e) {
      // TODO(krasikov): Do we need this? move this to another place.
      InitializationHelper.copyAsset(context, context.getFilesDir().toString(), "wordnet");
      learnedWords = new ArrayList<String>();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void initializeWordsInProgress() {
    wordsInProgress =
        new PriorityQueue<String>(original_word_list.size(), new WordPriorityComparator());
    for (String word : original_word_list) {
      if (!learnedWords.contains(word)) {
        wordsInProgress.add(word);
      }
    }
  }

  private static ArrayList<String> readWordListFromInput(InputStream input) throws IOException {
    ArrayList<String> wordlist = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String word;

    while ((word = reader.readLine()) != null) {
      wordlist.add(word.trim());
    }

    input.close();
    return wordlist;
  }

  public static LearningManager getInstance() {
    return instance;
  }

  public String popWord() { return wordsInProgress.poll(); }

  public void addWord(String word) { wordsInProgress.add(word); }

  public String[] getWordsInProgress() {
    // TODO(krasikov): maybe rewrite this to be real words in progress i.e. remove words which
    // wasn't yet show.
    return wordsInProgress.toArray(new String[0]);
  }

  public WordStatsDAO getWordStatsDAO() {
    return wordStatsDAO;
  }

  public int getLearnedWordsNum() {
    return learnedWords.size();
  }

  public void pretendUserLearnedNFirstWords(int learnedWordsNum) {
    Log.e(getClass().getCanonicalName(), "setting " + Integer.toString(learnedWordsNum));
    Log.e(getClass().getCanonicalName(), "learned words " + Integer.toString(learnedWords.size()));

    learnedWords = original_word_list.subList(0, learnedWordsNum);
    saveLearnedWords();
    for (String learnedWord : learnedWords) {
      wordsInProgress.remove(learnedWord);
    }
  }

  private void saveLearnedWords() {
    try {
      OutputStreamWriter output =
          new OutputStreamWriter(context.openFileOutput("learnedWords.txt", Context.MODE_PRIVATE));
      for(String learnedWord : learnedWords) {
        output.write(learnedWord + "\n");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public int accomplishedExercisesNum() {
    int exercisesNum = 0;
    Enumeration<WordStats> elements = wordsStats.elements();
    while (elements.hasMoreElements()) {
      exercisesNum += elements.nextElement().history.size();
    }
    return exercisesNum;
  }

  public void memorizeExerciseResult(String word, boolean success) {
    WordStats wordStats = wordStatsDAO.getStats(word);
    wordStats.addEntry(success);
    wordStatsDAO.update(wordStats);
  }

  private static class WordPriorityComparator implements Comparator<String> {
    @Override
    public int compare(String x, String y) {
      if (x.length() < y.length()) {
        return -1;
      }
      if (x.length() > y.length()) {
        return 1;
      }
      return 0;
    }
  }
}
