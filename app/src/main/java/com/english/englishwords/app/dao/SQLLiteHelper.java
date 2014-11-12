package com.english.englishwords.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLLiteHelper extends SQLiteOpenHelper {

  private static final String WORDSTATS_TABLE_CREATE =
      String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, %s TEXT, %s TEXT)",
          SQLLiteWordStatsDAO.WORD_STATS_TABLE_NAME, SQLLiteWordStatsDAO.WORD_COLUMN_NAME,
          SQLLiteWordStatsDAO.DATES_COLUMN_NAME, SQLLiteWordStatsDAO.SUCCESSES_COLUMN_NAME);

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