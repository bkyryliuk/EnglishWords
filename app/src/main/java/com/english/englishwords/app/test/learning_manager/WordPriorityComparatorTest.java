package com.english.englishwords.app.test.learning_manager;

import android.test.InstrumentationTestCase;
import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;
import com.english.englishwords.app.learning_manager.WordPriorityComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WordPriorityComparatorTest extends InstrumentationTestCase {
  final Long rightNow = new Date().getTime();

  private WordPriorityComparator getComparator(WordStats[] wordsStats) {
    List<String> wordOrder = new ArrayList<String>();
    for (WordStats wordStats : wordsStats) {
      wordOrder.add(wordStats.word);
    }
    return new WordPriorityComparator(
        new DummyWordListsDAO(wordOrder), new DummyWordStatsDAO(Arrays.asList(wordsStats)));
  }

  // ================================== 0 exercises ==================================

  public void testUnknownWordsFollowUsageFrequency() throws Exception {
    WordStats stat1 = new WordStats("word1");
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  // ================================== 1 exercise ==================================

  public void testSingleSuccess5HoursAgoDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 5 * WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSingleSuccess6DaysAgoRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 6 * WordPriorityComparator.DAY, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSingleFailure7MinsAgoRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 7 * WordPriorityComparator.MINUTE, false);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSingleFailure3MinsAgoDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 3 * WordPriorityComparator.MINUTE, false);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }

  // ================================== 2 exercises ==========================================

  public void testFailure22HoursAgoAndSuccessBeforeThatRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 21 * WordPriorityComparator.HOUR, true);
    stat1.addEntry(rightNow - 22 * WordPriorityComparator.HOUR, false);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess21DaysAnd27DaysAgoRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 27 * WordPriorityComparator.DAY, true);
    stat1.addEntry(rightNow - 21 * WordPriorityComparator.DAY, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess20DaysAnd26DaysAgoDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 26 * WordPriorityComparator.DAY, true);
    stat1.addEntry(rightNow - 20 * WordPriorityComparator.DAY, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess30HoursAgoAndFailedBeforeThatRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 31 * WordPriorityComparator.HOUR, false);
    stat1.addEntry(rightNow - 30 * WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess22HoursAgoAndFailedBeforeThatDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 22 * WordPriorityComparator.HOUR, false);
    stat1.addEntry(rightNow - 21 * WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess3DaysAgoAndSuccessAndFailureBeforeThatDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 3 * WordPriorityComparator.DAY, false);
    stat1.addEntry(rightNow - 2 * WordPriorityComparator.DAY, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }


  // ================================== 3 exercises ==========================================

  public void testSuccess24HoursAgoAndSuccessAndFailureBeforeThatRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 90 * WordPriorityComparator.HOUR, false);
    stat1.addEntry(rightNow - 25 * WordPriorityComparator.HOUR, true);
    stat1.addEntry(rightNow - 24 * WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(-1, wordPriorityComparator.compare("word1", "word2"));
  }

  public void testSuccess22HoursAgoAndSuccessAndFailureBeforeThatDontRepeat() throws Exception {
    WordStats stat1 = new WordStats("word1");
    stat1.addEntry(rightNow - 90 * WordPriorityComparator.HOUR, false);
    stat1.addEntry(rightNow - 28 * WordPriorityComparator.HOUR, true);
    stat1.addEntry(rightNow - 22 * WordPriorityComparator.HOUR, true);
    WordStats stat2 = new WordStats("word2");
    WordPriorityComparator wordPriorityComparator = getComparator(new WordStats[]{stat1, stat2});

    assertEquals(1, wordPriorityComparator.compare("word1", "word2"));
  }

}