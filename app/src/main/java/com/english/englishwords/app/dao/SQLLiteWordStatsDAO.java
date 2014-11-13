package com.english.englishwords.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class SQLLiteWordStatsDAO implements WordStatsDAO {
  public static final String WORD_STATS_TABLE_NAME = "word_stats";
  public static final String WORD_COLUMN_NAME = "word";
  public static final String DATES_COLUMN_NAME = "dates";
  public static final String SUCCESSES_COLUMN_NAME = "successes";
  final String DELIMITER = ";";
  SQLiteOpenHelper dbHelper;

  private Hashtable<String, WordStats> wordsStats = new Hashtable<String, WordStats>();

  public SQLLiteWordStatsDAO(Context context) {
    this.dbHelper = new SQLLiteHelper(context);

    for (WordStats stats : this.getStatsForAllWords()) {
      wordsStats.put(stats.word, stats);
    }
  }

  public WordStats getStats(String word) {
    WordStats wordStats = wordsStats.get(word);
    if (wordStats == null) {
      wordStats = new WordStats(word);
    }
    return wordStats;
  }

  @Override
  public void update(WordStats wordStats) {
    ContentValues values = getContentValues(wordStats.history);
    values.put(WORD_COLUMN_NAME, wordStats.word);
    dbHelper.getWritableDatabase().replace(WORD_STATS_TABLE_NAME, null, values);
  }

  private ContentValues getContentValues(ArrayList<Pair<Date, Boolean>> history) {
    StringBuilder dates = new StringBuilder();
    StringBuilder successes = new StringBuilder();
    for (Pair<Date, Boolean> entry : history) {
      if (dates.length() > 0) {
        dates.append(DELIMITER);
      }
      dates.append(entry.first.getTime());
      successes.append(entry.second ? "1" : "0");
    }
    ContentValues contentValues = new ContentValues();
    contentValues.put(DATES_COLUMN_NAME, dates.toString());
    contentValues.put(SUCCESSES_COLUMN_NAME, successes.toString());
    return contentValues;
  }

  private List<WordStats> getStatsForAllWords() {
    List<WordStats> wordsStats = new ArrayList<WordStats>();
    Cursor cursor = dbHelper.getReadableDatabase().query(
        WORD_STATS_TABLE_NAME,
        new String[]{WORD_COLUMN_NAME, DATES_COLUMN_NAME, SUCCESSES_COLUMN_NAME},
        null, null, null, null, null, null);
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      wordsStats.add(parseWordStat(cursor));
      cursor.moveToNext();
    }
    return wordsStats;
  }

//  @Override
//  public WordStats getStats(String word) {
//    WordStats wordStats = new WordStats(word);
//    Cursor cursor = dbHelper.getReadableDatabase().query(
//        WORD_STATS_TABLE_NAME,
//        new String[]{WORD_COLUMN_NAME, DATES_COLUMN_NAME, SUCCESSES_COLUMN_NAME},
//        WORD_COLUMN_NAME + "=?", new String[]{word}, null, null, null, null);
//    if (cursor.moveToFirst()) {
//      wordStats = parseWordStat(cursor);
//    }
//    cursor.close();
//    return wordStats;
//  }

  private WordStats parseWordStat(Cursor cursor) {
    WordStats wordStats = new WordStats(cursor.getString(cursor.getColumnIndex(WORD_COLUMN_NAME)));
    String[] dates = cursor.getString(cursor.getColumnIndex(DATES_COLUMN_NAME)).split(DELIMITER);
    String successes = cursor.getString(cursor.getColumnIndex(SUCCESSES_COLUMN_NAME));
    assert dates.length == successes.length();
    for (int i = 0; i < dates.length; i++) {
      long milliseconds = Long.parseLong(dates[i]);
      Boolean success = successes.charAt(i) != '0';
      wordStats.history.add(new Pair<Date, Boolean>(new Date(milliseconds), success));
    }
    return wordStats;
  }

  public static class SQLLiteHelper extends SQLiteOpenHelper {

    private static final String WORDSTATS_TABLE_CREATE =
        String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT)",
            WORD_STATS_TABLE_NAME, WORD_COLUMN_NAME,
            DATES_COLUMN_NAME, SUCCESSES_COLUMN_NAME);

    public SQLLiteHelper(Context context) {
      super(context, "EnglishLearner", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL(WORDSTATS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    }
  }
}
