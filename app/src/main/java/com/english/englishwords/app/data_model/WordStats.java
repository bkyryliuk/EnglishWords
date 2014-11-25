package com.english.englishwords.app.data_model;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class WordStats {
  public String word;
  // TODO(krasikov): maybe replace Date with long.
  public ArrayList<Pair<Long, Boolean>> history = new ArrayList<Pair<Long, Boolean>>();

  public WordStats(String word) {
    this.word = word;
  }

  public void addEntry(Boolean success) {
    addEntry(new Date().getTime(), success);
  }

  public void addEntry(Long time, Boolean success) {
    history.add(new Pair<Long, Boolean>(time, success));
  }

  public boolean allExercisesAreSuccessful() {
    for (Iterator<Pair<Long, Boolean>> iterator = history.iterator(); iterator.hasNext(); ) {
      Pair<Long, Boolean> exercise = iterator.next();
      if (!exercise.second) {
        return false;
      }
    }
    return true;
  }

  public void copyFrom(WordStats wordStats) {
    word = wordStats.word;
    history = wordStats.history;
  }
}
