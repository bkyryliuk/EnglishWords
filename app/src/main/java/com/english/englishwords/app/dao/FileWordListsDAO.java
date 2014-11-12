package com.english.englishwords.app.dao;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Set;

public class FileWordListsDAO implements WordListsDAO {
  private final Context context;

  public FileWordListsDAO(Context context) {
    this.context = context;
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
  public InputStream getLearnedWordsStream() throws FileNotFoundException {
    return context.openFileInput("learnedWords.txt");
  }

  @Override
  public InputStream getOriginalWordOrderStream() throws IOException {
    return context.getAssets().open("original_word_order.txt");
  }
}
