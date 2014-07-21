package com.english.englishwords.app.pojo;

import android.content.Context;

import com.english.englishwords.app.InitializationHelper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bogdank on 4/26/14.
 */
// This class is singleton.
public class WordQueue {
  private static WordQueue instance = null;
    // Words that weren't yet learned or weren't even show to the user.
    private List<String> wordsInProgress;
    // Words that considered to be learned.
    private List<String> learnedWords;


    private WordQueue() {
  }

    // Inits the word queue from res/original_word_order.txt or from previous application runs.
    public static void initialize(Context context) {
        WordQueue.instance = new WordQueue();
        try {
            WordQueue.instance.wordsInProgress =
                    readWordListFromInput(context.openFileInput("wordsInProgress.txt"));
            WordQueue.instance.learnedWords =
                    readWordListFromInput(context.openFileInput("learnedWords.txt"));
        } catch (FileNotFoundException e) {
            initializeFirstApplicationRun(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

  public static WordQueue getInstance() {
      return instance;
  }

    private static void initializeFirstApplicationRun(Context context) {
        String data_storage_root = context.getFilesDir().toString();
        InitializationHelper.copyAsset(context, data_storage_root, "wordnet");
        try {
            instance.wordsInProgress =
                    readWordListFromInput(context.getAssets().open("original_word_order.txt"));
            instance.learnedWords = new ArrayList<String>();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO(bogdank): call user vocabulary size estimation activity.
  }

    public List<String> getWordsInProgress() {
        return wordsInProgress;
  }

    public List<String> getLearnedWords() {
        return learnedWords;
  }
}
