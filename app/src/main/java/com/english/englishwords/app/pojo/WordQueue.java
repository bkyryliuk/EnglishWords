package com.english.englishwords.app.pojo;

import android.content.Context;

import com.english.englishwords.app.InitializationHelper;
import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.dao.WordNetWordDAO;

import java.util.List;

/**
 * The class is resposible for providing interfaces to work with the word queue like
 * initializing the queue, reshuffling, updating it from the results of the learning.
 */
// This class is singleton.
public class WordQueue {
  private static WordQueue instance = null;
  private WordDAO wordDao;

  private WordQueue(Context context) {
    wordDao = new WordNetWordDAO(context);
  }

  // Inits the word queue from res/original_word_order.txt or from previous application runs.
  public static void initialize(Context context) {
    instance = new WordQueue(context);
    initializeFirstApplicationRun(context);
  }

  public static WordQueue getInstance() {
    return instance;
  }

  private static void initializeFirstApplicationRun(Context context) {
    String data_storage_root = context.getFilesDir().toString();
    InitializationHelper.copyAsset(context, data_storage_root, "wordnet");
    //TODO(bogdank): call user vocabulary size estimation activity.
  }

  public List<String> getWordsInProgress() {
    return wordDao.getWordsInProgress();
  }

  public List<String> getLearnedWords() {
    return wordDao.getLearnedWords();
  }
}
