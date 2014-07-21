package com.english.englishwords.app.dao;

import android.content.Context;

import com.english.englishwords.app.pojo.Word;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rage on 11.05.14.
 */
public abstract class WordDAO {
  private Context context;

  public WordDAO(Context context) {
    this.context = context;
  }

  private static ArrayList<String> readWordListFromInput(InputStream input) throws IOException {
    ArrayList<String> wordlist = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String word;
    while ((word = reader.readLine()) != null) {
      wordlist.add(word.trim());
    }
    input.close();
    return wordlist;
  }

  public abstract Word getWord(String word);

  public List<String> getWordsInProgress() {
    try {
      List<String> wordsInProgress =
          readWordListFromInput(context.openFileInput("wordsInProgress.txt"));
      if (wordsInProgress.size() == 0) {
        wordsInProgress = readWordListFromInput(context.getAssets().open("original_word_order.txt"));
      }
      return wordsInProgress;
    } catch (IOException e) {
      e.printStackTrace();
      // in case there is no file yet, we return original word list
      try {
        return readWordListFromInput(context.getAssets().open("original_word_order.txt"));
      } catch (IOException e1) {
        e1.printStackTrace();
        return new ArrayList<String>();
      }
    }
  }

  public List<String> getLearnedWords() {
    try {
      return readWordListFromInput(context.openFileInput("learnedWords.txt"));
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<String>();
    }
  }

}
