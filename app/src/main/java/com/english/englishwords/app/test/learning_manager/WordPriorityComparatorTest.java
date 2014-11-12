package com.english.englishwords.app.test.learning_manager;

import android.test.InstrumentationTestCase;

import com.english.englishwords.app.dao.FileWordListsDAO;
import com.english.englishwords.app.dao.SQLLiteHelper;
import com.english.englishwords.app.dao.SQLLiteWordStatsDAO;

import java.util.ArrayList;

public class WordPriorityComparatorTest extends InstrumentationTestCase {

//  private WordPriorityComparator wordPriorityComparator;
//  private LearningManager learningManager;

  public WordPriorityComparatorTest() {
//    LearningManager.initialize(new SQLLiteWordStatsDAO(new SQLLiteHelper(this.getInstrumentation().getContext())), new FileWordListsDAO(this.getInstrumentation().getContext()));
//    learningManager = LearningManager.getInstance();
  }

  protected void setUp() {
//    ArrayList<String> wordOrder = new ArrayList<String>();
//    wordOrder.add('word1');
//    wordOrder.add('word2');
//    wordPriorityComparator = new WordPriorityComparator(wordOrder);
  }

  public void testGetWordMemorizationDelay() throws Exception {
    assertTrue(true);
  }

  public void testGetWordMemorizationDelay2() throws Exception {
    assertTrue(true);
  }
}