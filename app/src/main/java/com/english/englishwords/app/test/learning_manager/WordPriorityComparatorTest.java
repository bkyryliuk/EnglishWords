package com.english.englishwords.app.test.learning_manager;

import android.test.InstrumentationTestCase;
import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;
import com.english.englishwords.app.learning_manager.LearningManager;
import com.english.englishwords.app.learning_manager.WordPriorityComparator;

import java.util.Arrays;
import java.util.Date;

public class WordPriorityComparatorTest extends InstrumentationTestCase {

  final Date rightNow = new Date();

  private WordPriorityComparator getComparator(WordStats[] stats, String[] wordOrder) {
    LearningManager.initialize(
        new DummyWordStatsDAO(Arrays.asList(stats)),
        new DummyWordListsDAO(Arrays.asList(wordOrder)));
    return new WordPriorityComparator(LearningManager.getInstance());
  }

  public void testGetWordMemorizationDelay() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.history.add(new Pair<Date, Boolean>(rightNow, true));
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(
        new WordStats[]{stat1, stat2}, new String[] {"word1", "word2"});

    assertEquals(wordPriorityComparator.compare("word1", "word2"), -1);
  }

  public void testGetWordMemorizationDelay2() throws Exception {
    assertTrue(true);
  }

}