package com.english.englishwords.app.data_model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;

public class WordStats {
  public String word;
  // TODO(krasikov): maybe replace Date with long.
  public ArrayList<Pair<Date, Boolean>> history = new ArrayList<Pair<Date, Boolean>>();

  public WordStats(String word) {
    this.word = word;
  }

  public void addEntry(Boolean success) {
    history.add(new Pair<Date, Boolean>(new Date(), success));
  }

//  public ArrayList<Date> getDates() {
//    ArrayList<Date> dates = new ArrayList<Date>();
//    for (Pair<Date, Boolean> entry : history) {
//      dates.add(entry.first);
//    }
//    return dates;
//  }
//
//  public ArrayList<Boolean> getSuccesses() {
//    ArrayList<Boolean> successes = new ArrayList<Boolean>();
//    for (Pair<Date, Boolean> entry : history) {
//      successes.add(entry.second);
//    }
//    return successes;
//  }
}
