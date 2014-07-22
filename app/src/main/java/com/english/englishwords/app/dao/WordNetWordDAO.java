package com.english.englishwords.app.dao;

import android.content.Context;
import android.util.Log;

import com.english.englishwords.app.data_model.Word;
import com.english.englishwords.app.data_model.WordSense;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * Created by rage on 11.05.14.
 */
public class WordNetWordDAO implements WordDAO {
  private Dictionary dictionary = null;

  public WordNetWordDAO(Context context) {
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
        // TODO(krasikov): add synonyms.
        synonyms.add("synonym " + wordString);
        ArrayList<String> examples = new ArrayList<String>();
        examples.add("example " + wordString);
        // TODO(krasikov): add examples.
        String definition = wordString + " definition.";
        if (indexWord.getSenses().size() > 0) {
          definition = indexWord.getSenses().get(0).getGloss();
        } else {
          Log.v(this.getClass().toString(), "no senses in the word" + wordString);
        }
        word.getSenses().add(new WordSense(wordString, definition, examples, synonyms));
      }
    } catch (JWNLException e) {
      e.printStackTrace();
      assert false;
    }
    return word;
  }
}
