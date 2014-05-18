package com.english.englishwords.app.dao;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import com.english.englishwords.app.pojo.Word;
import com.english.englishwords.app.pojo.WordSense;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.dictionary.Dictionary;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by rage on 11.05.14.
 */
public class WordNetWordDAO implements WordDAO {
  private Dictionary dictionary = null;
  private Context context;

  public WordNetWordDAO(Context context) {
    this.context = context;
    try {
      dictionary = Dictionary.getFileBackedInstance(context.getFilesDir().toString() + "/wordnet");
    } catch (JWNLException e) {
      e.printStackTrace();
    }
}

  @Override
  public Word getWord(String wordString) {
    Word word = new Word(wordString);
    try {
      IndexWordSet indexWordSet = dictionary.lookupAllIndexWords(wordString);
      for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
        System.out.println(indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
        ArrayList<String> synonyms = new ArrayList<String>();
        ArrayList<String> examples = new ArrayList<String>();
        String definition = indexWord.getSenses().get(0).getGloss();
        word.getSenses().add(new WordSense(wordString, definition, examples, synonyms));
      }
    } catch (JWNLException e) {
    }
    return word;
  }
}
