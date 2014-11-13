package com.english.englishwords.app.dao;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class FileWordListsDAO implements WordListsDAO {
  private final Context context;
  private final ArrayList<String> originalWordList;
  private final Hashtable<String, Integer> originalWordListToPos;
  private final HashSet<String> learnedWords;

  public FileWordListsDAO(Context context) {
    this.context = context;

    ArrayList<String> originalWordList = null;
    try {
      originalWordList = readWordListFromInput(context.getAssets().open("words_sorted_by_usage.txt"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.originalWordList = originalWordList;
    originalWordListToPos = new Hashtable<String, Integer>(originalWordList.size());
    for (int i = 0; i < originalWordList.size(); i++) {
      originalWordListToPos.put(originalWordList.get(i), i);
    }

    HashSet<String> learnedWords = new HashSet<String>();
    try {
      learnedWords = new HashSet<String>(
          FileWordListsDAO.readWordListFromInput(context.openFileInput("learnedWords.txt")));
    } catch (FileNotFoundException e) {
      // Probably first application run - create empty learnedWords.txt.
      Log.d(this.getClass().getCanonicalName(), "learnedWords.txt is missing");
      learnedWords = new HashSet<String>();
      saveLearnedWords(learnedWords);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.learnedWords = learnedWords;
  }

  public static ArrayList<String> readWordListFromInput(InputStream input) throws IOException {
    ArrayList<String> wordList = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String word;

    while ((word = reader.readLine()) != null) {
      wordList.add(word.trim());
    }

    input.close();
    return wordList;
  }

  @Override
  public void saveLearnedWords(Set<String> learnedWords) {
    try {
      OutputStreamWriter output =
          new OutputStreamWriter(context.openFileOutput("learnedWords.txt", Context.MODE_PRIVATE));
      for(String learnedWord : learnedWords) {
        output.write(learnedWord + "\n");
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Set<String> getLearnedWords() {
    return learnedWords;
  }

  @Override
  public List<String> getWordOrderByUsage() {
    return originalWordList;
  }

  @Override
  public int getPositionInUsageFrequencyList(String word) {
    return originalWordListToPos.get(word);
  }
}
