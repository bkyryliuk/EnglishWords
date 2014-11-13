package com.english.englishwords.app.test.learning_manager;

import android.test.InstrumentationTestCase;
import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;
import com.english.englishwords.app.learning_manager.WordPriorityComparator;

import java.util.Arrays;
import java.util.Date;

public class WordPriorityComparatorTest extends InstrumentationTestCase {
  final Long rightNow = new Date().getTime();

  private WordPriorityComparator getComparator(WordStats[] stats, String[] wordOrder) {
    return new WordPriorityComparator(
        new DummyWordListsDAO(Arrays.asList(wordOrder)),
        new DummyWordStatsDAO(Arrays.asList(stats)));
  }

  public void testGetWordMemorizationDelay() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(
        new WordStats[]{stat1, stat2}, new String[]{"word1", "word2"});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testGetWordMemorizationDelay2() throws Exception {
    assertTrue(true);
  }

}