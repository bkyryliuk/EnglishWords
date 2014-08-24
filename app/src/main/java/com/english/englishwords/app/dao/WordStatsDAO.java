package com.english.englishwords.app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import com.english.englishwords.app.data_model.WordStats;

import java.util.ArrayList;
import java.util.Date;

public class WordStatsDAO {
  SQLiteOpenHelper dbHelper;
  final String DELIMITER = ";;";
  public static final String WORD_STATS_TABLE_NAME = "word_stats";
  public static final String WORD_COLUMN_NAME = "word";
  public static final String DATES_COLUMN_NAME = "dates";
  public static final String SUCCESSES_COLUMN_NAME = "successes";


  public WordStatsDAO(Context context) {
    this.dbHelper = new SQLLiteHelper(context);
  }

  public void update(WordStats wordStats) {
    ContentValues values = getContentValues(wordStats.history);
    values.put(WORD_COLUMN_NAME, wordStats.word);
    dbHelper.getWritableDatabase().replace(
        WORD_STATS_TABLE_NAME, null, values);
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

  public WordStats getStats(String word) {
    WordStats wordStats = new WordStats(word);
    Cursor cursor = dbHelper.getReadableDatabase().query(
        WORD_STATS_TABLE_NAME,
        new String[] {DATES_COLUMN_NAME, SUCCESSES_COLUMN_NAME},
        WORD_COLUMN_NAME + "=?", new String[] { word }, null, null, null, null);
    if (cursor == null) {
      return wordStats;
    }
    cursor.moveToFirst();
    if (!cursor.isAfterLast()) {
      String[] dates = cursor.getString(0).split(DELIMITER);
      String[] successes = cursor.getString(1).split("");
      assert dates.length == successes.length;
      for (int i = 0; i < dates.length; i++) {
        int milliseconds = Integer.parseInt(dates[i]);
        Boolean success = Integer.parseInt(successes[i]) != 0;
        wordStats.history.add(new Pair<Date, Boolean>(new Date(milliseconds), success));
      }
    }
    cursor.close();
    return wordStats;
  }
}
